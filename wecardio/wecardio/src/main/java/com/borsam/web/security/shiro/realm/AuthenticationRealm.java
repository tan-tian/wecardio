package com.borsam.web.security.shiro.realm;

import com.borsam.pojo.security.Principal;
import com.borsam.pub.UserType;
import com.borsam.repository.entity.admin.Admin;
import com.borsam.repository.entity.doctor.DoctorAccount;
import com.borsam.repository.entity.doctor.DoctorProfile;
import com.borsam.repository.entity.patient.PatientAccount;
import com.borsam.repository.entity.patient.PatientProfile;
import com.borsam.service.admin.AdminService;
import com.borsam.service.doctor.DoctorAccountService;
import com.borsam.service.doctor.DoctorProfileService;
import com.borsam.service.patient.PatientAccountService;
import com.borsam.service.patient.PatientProfileService;
import com.borsam.web.security.shiro.token.UserTypeToken;
import com.hiteam.common.service.CaptchaService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.WebUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 权限认证
 * Created by Sebarswee on 2015/6/17.
 */
public class AuthenticationRealm extends AuthorizingRealm {

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "doctorAccountServiceImpl")
    private DoctorAccountService doctorAccountService;

    @Resource(name = "doctorProfileServiceImpl")
    private DoctorProfileService doctorProfileService;

    @Resource(name = "patientAccountServiceImpl")
    private PatientAccountService patientAccountService;

    @Resource(name = "patientProfileServiceImpl")
    private PatientProfileService patientProfileService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) principalCollection.fromRealm(getName()).iterator().next();
        if (principal != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.addRole(principal.getUserType().name());
            // 医生区分不同角色权限
            if (UserType.doctor.equals(principal.getUserType())) {
                DoctorAccount doctorAccount = doctorAccountService.find(principal.getId());
                DoctorProfile doctorProfile = doctorAccount.getDoctorProfile();
                if ((doctorProfile.getRoles() & 1) == 1) {
                    authorizationInfo.addRole("org");
                }
                if ((doctorProfile.getRoles() & 2) == 2) {
                    authorizationInfo.addRole("editDoctor");
                }
                if ((doctorProfile.getRoles() & 4) == 4) {
                    authorizationInfo.addRole("auditDoctor");
                }
                if ((doctorProfile.getRoles() & 8) == 8) {
                    authorizationInfo.addRole("operateDoctor");
                }
            }
            return authorizationInfo;
        }
        return null;
    }

    /**
     * 获取认证信息
     * @param authenticationToken 令牌
     * @return 认证信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserTypeToken token = (UserTypeToken) authenticationToken;

        String username = token.getUsername();
        String password = (token.getPassword() == null ? "" : new String(token.getPassword()));
        String captchaId = token.getCaptchaId();
        String captcha = token.getCaptcha();
        UserType userType = token.getUserType();
        String host = token.getHost();

        /**
         * 用户认证：
         * 1. 首页验证验证码，验证错误直接返回
         * 2. 根据登录账号查找用户，不存在则返回UnknownAccountException
         * 3. 判断用户是否有效，无效返回DisabledAccountException
         * 4. 进行密码验证，验证失败，返回IncorrectCredentialsException
         * 5. 验证成功
         */
        if (!captchaService.isValid(captchaId, captcha)) {
            throw new UnsupportedTokenException();
        }

        if (username != null && StringUtils.isNotEmpty(password)) {
            // 根据不同用户类型，进行登录验证
            switch (userType) {
                case admin :
                    Admin admin = adminService.findByUsername(username);
                    this.validateAdmin(admin, password);
                    admin.setLoginIp(host);
                    admin.setLoginDate(new Date().getTime() / 1000);
                    admin.setLoginFailureCount(0);
                    adminService.update(admin);
                    WebUtil.setSessionData("username",username);
                    return new SimpleAuthenticationInfo(new Principal(admin.getId(), userType, username, admin.getName()), password, getName());
                case org :
                    DoctorAccount orgAccount = doctorAccountService.findByUsername(username);
                    this.validateOrg(orgAccount, password);
                    DoctorProfile doctorProfile = orgAccount.getDoctorProfile();
                    if (doctorProfile != null) {
                        doctorProfile.setLoginState(DoctorProfile.LoginState.onLine);
                        doctorProfile.setLastTime(new Date().getTime() / 1000);
                        doctorProfileService.update(doctorProfile);
                    }
                    WebUtil.setSessionData("username",username);
                    return new SimpleAuthenticationInfo(new Principal(orgAccount.getId(), userType, username, doctorProfile != null ? doctorProfile.getName() : username), password, getName());
                case doctor :
                    DoctorAccount doctorAccount = doctorAccountService.findByUsername(username);
                    this.validateDoctor(doctorAccount, password);
                    DoctorProfile profile = doctorAccount.getDoctorProfile();
                    if (profile != null) {
                        profile.setLoginState(DoctorProfile.LoginState.onLine);
                        profile.setLastTime(new Date().getTime() / 1000);
                        doctorProfileService.update(profile);
                    }
                    WebUtil.setSessionData("username",username);
                    return new SimpleAuthenticationInfo(new Principal(doctorAccount.getId(), userType, username, profile != null ? profile.getName() : username), password, getName());
                case patient :
                    PatientAccount patientAccount = patientAccountService.findByUsername(username);
                    this.validatePatient(patientAccount, password);
                    PatientProfile patientProfile = patientAccount.getPatientProfile();
                    if (patientProfile != null) {
                        patientProfile.setLoginTime(new Date().getTime() / 1000);
                        patientProfileService.update(patientProfile);
                    }
                    WebUtil.setSessionData("username",username);
                    return new SimpleAuthenticationInfo(new Principal(patientAccount.getId(), userType, username, patientProfile != null ? patientProfile.getName() : username), password, getName());
            }
        }
        throw new UnknownAccountException();
    }

    /**
     * 验证平台管理员账号
     * @param admin 平台管理员账号
     * @param password 密码
     */
    private void validateAdmin(Admin admin, String password) {
        if (admin == null) {
            throw new UnknownAccountException();
        }
        if (!admin.getIsEnabled()) {
            throw new DisabledAccountException();
        }
        if (admin.getIsLocked()) {
            // 获取配置的锁定时长，0为永久锁定
            int loginFailureLockTime = Integer.parseInt(ConfigUtils.config.getProperty("accountLockTime", "0"));
            if (loginFailureLockTime == 0) {
                throw new LockedAccountException();
            }
            Date lockedDate = new Date(admin.getLockedDate());
            Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
            // 超过锁定时长自动解锁
            if (new Date().after(unlockDate)) {
                admin.setLoginFailureCount(0);
                admin.setIsLocked(false);
                admin.setLockedDate(null);
                adminService.update(admin);
            } else {
                throw new LockedAccountException();
            }
        }
        if (!DigestUtils.md5Hex(password).equals(admin.getPassword())) {
            int loginFailureCount = admin.getLoginFailureCount() + 1;
            // 获取配置的连续登录失败次数
            int accountLockCount = Integer.parseInt(ConfigUtils.config.getProperty("accountLockCount", "1"));
            // 登录失败超过配置次数则锁定账号
            if (loginFailureCount >= accountLockCount) {
                admin.setIsLocked(true);
                admin.setLockedDate(new Date().getTime());
            }
            admin.setLoginFailureCount(loginFailureCount);
            adminService.update(admin);
            throw new IncorrectCredentialsException();
        }
    }

    /**
     * 验证机构账号
     * @param orgAccount 机构账号
     * @param password 密码
     */
    private void validateOrg(DoctorAccount orgAccount, String password) {
        if (orgAccount == null) {
            throw new UnknownAccountException();
        }
        if (!orgAccount.getIsActive()) {
            throw new DisabledAccountException();
        }
        if (orgAccount.getIsDelete()) {
            throw new LockedAccountException();
        }
        DoctorProfile doctorProfile = orgAccount.getDoctorProfile();
        if (doctorProfile == null) {
            throw new UnknownAccountException();
        } else {
            // 判断是否有管理员角色
            int role = doctorProfile.getRoles();
            if ((role & 1) != 1) {
                throw new UnknownAccountException();
            }
        }
        if (!DigestUtils.md5Hex(password).equals(orgAccount.getPassword())) {
            throw new IncorrectCredentialsException();
        }
    }

    /**
     * 验证医生账号
     * @param doctorAccount 医生账号
     * @param password 密码
     */
    private void validateDoctor(DoctorAccount doctorAccount, String password) {
        if (doctorAccount == null) {
            throw new UnknownAccountException();
        }
        // 医生未审核通过前，还是能登录系统修改基本信息 !doctorAccount.getIsActive()
        if (doctorAccount.getIsDelete()) {
            throw new DisabledAccountException();
        }
        if (!DigestUtils.md5Hex(password).equals(doctorAccount.getPassword())) {
            throw new IncorrectCredentialsException();
        }
    }

    /**
     * 验证患者账号
     * @param patientAccount 患者账号
     * @param password 密码
     */
    private void validatePatient(PatientAccount patientAccount, String password) {
        if (patientAccount == null) {
            throw new UnknownAccountException();
        }
        if (patientAccount.getIsDelete()) {
            throw new DisabledAccountException();
        }
        if (!DigestUtils.md5Hex(password).equals(patientAccount.getPassword())) {
            throw new IncorrectCredentialsException();
        }
    }
}
