package com.borsam.service.pub;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.token.AccountToken;

import java.util.Map;

/**
 * Service - 邮件
 * Created by Sebarswee on 2015/6/23.
 */
public interface MailService {

    /**
     * SMTP协议发送邮件
     * @param host SMTP服务器地址
     * @param port SMTP服务器端口
     * @param username SMTP用户名
     * @param password SMTP密码
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param subject 主题
     * @param templatePath 模板路径
     * @param model 数据
     * @param async 是否异步
     */
    public void send(String host, Integer port, String username, String password, String from, String to,
                     String subject, String templatePath, Map<String, Object> model, boolean async);

    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param templatePath 模板路径
     * @param model 数据
     * @param async 是否异步
     */
    public void send(String to, String subject, String templatePath, Map<String, Object> model, boolean async);

    /**
     * 异步方式发送邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param templatePath 模板路径
     * @param model 数据
     */
    public void send(String to, String subject, String templatePath, Map<String, Object> model);

    /**
     * 异步方式发送邮件
     * @param to 收件人邮箱
     * @param subject 主题
     * @param templatePath 模板路径
     */
    public void send(String to, String subject, String templatePath);

    /**
     * 发送激活邮件
     * @param to 收件人邮箱
     * @param username 用户名
     * @param token token
     */
    public void sendActiveMail(String to, String username, AccountToken token);

    /**
     * 发送找回密码邮件
     * @param userType 用户类型
     * @param to 收件人邮箱
     * @param username 用户名
     * @param token token
     */
    public void sendFindPasswordMail(UserType userType, String to, String username, AccountToken token);
}
