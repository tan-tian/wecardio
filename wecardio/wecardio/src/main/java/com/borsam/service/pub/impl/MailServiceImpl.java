package com.borsam.service.pub.impl;

import com.borsam.pub.UserType;
import com.borsam.repository.entity.token.AccountToken;
import com.borsam.service.pub.MailService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.web.I18Util;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * Service - 邮件
 * Created by Sebarswee on 2015/6/23.
 */
@Service("mailServiceImpl")
public class MailServiceImpl implements MailService {

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource(name = "javaMailSender")
    private JavaMailSenderImpl javaMailSender;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    /**
     * 添加邮件发送任务
     * @param mimeMessage mimeMessage
     */
    private void addSendTask(final MimeMessage mimeMessage) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                javaMailSender.send(mimeMessage);
            }
        });
    }

    @Override
    public void send(String host, Integer port, String username, String password, String from, String to, String subject, String templatePath, Map<String, Object> model, boolean async) {
        Assert.hasText(host);
        Assert.notNull(port);
        Assert.hasText(username);
        Assert.hasText(password);
        Assert.hasText(from);
        Assert.hasText(to);
        Assert.hasText(subject);
        Assert.hasText(templatePath);

        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate(templatePath);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            javaMailSender.setHost(host);
            javaMailSender.setPort(port);
            javaMailSender.setUsername(username);
            javaMailSender.setPassword(password);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            if (async) {
                addSendTask(mimeMessage);
            } else {
                javaMailSender.send(mimeMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String to, String subject, String templatePath, Map<String, Object> model, boolean async) {
        String host = ConfigUtils.config.getProperty("smtpHost");
        Integer port = Integer.valueOf(ConfigUtils.config.getProperty("smtpPort"));
        String username = ConfigUtils.config.getProperty("smtpUsername");
        String password = ConfigUtils.config.getProperty("smtpPassword");
        String from = ConfigUtils.config.getProperty("smtpFromMail");
        this.send(host, port, username, password, from, to, subject, templatePath, model, async);
    }

    @Override
    public void send(String to, String subject, String templatePath, Map<String, Object> model) {
        String host = ConfigUtils.config.getProperty("smtpHost");
        Integer port = Integer.valueOf(ConfigUtils.config.getProperty("smtpPort"));
        String username = ConfigUtils.config.getProperty("smtpUsername");
        String password = ConfigUtils.config.getProperty("smtpPassword");
        String from = ConfigUtils.config.getProperty("smtpFromMail");
        this.send(host, port, username, password, from, to, subject, templatePath, model, true);
    }

    @Override
    public void send(String to, String subject, String templatePath) {
        String host = ConfigUtils.config.getProperty("smtpHost");
        Integer port = Integer.valueOf(ConfigUtils.config.getProperty("smtpPort"));
        String username = ConfigUtils.config.getProperty("smtpUsername");
        String password = ConfigUtils.config.getProperty("smtpPassword");
        String from = ConfigUtils.config.getProperty("smtpFromMail");
        this.send(host, port, username, password, from, to, subject, templatePath, null, true);
    }

    @Override
    public void sendActiveMail(String to, String username, AccountToken token) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("username", username);
        model.put("token", token);
        String subject = I18Util.getMessage("org.register.mailSubject");
        String activeMail = ConfigUtils.config.getProperty("activeMail");
        send(to, subject, activeMail, model);
    }

    @Override
    public void sendFindPasswordMail(UserType userType, String to, String username, AccountToken token) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("userType", userType.getPath());
        model.put("username", username);
        model.put("token", token);
        String subject = I18Util.getMessage("common.password.mailSubject");
        String findPasswordMail = ConfigUtils.config.getProperty("findPasswordMail");
        send(to, subject, findPasswordMail, model);
    }
}
