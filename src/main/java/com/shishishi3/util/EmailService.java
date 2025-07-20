package com.shishishi3.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class EmailService {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream("mail.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find mail.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送验证码邮件
     * @param toEmail 接收者的邮箱地址
     * @param code    要发送的6位验证码
     */
    public static void sendVerificationCode(String toEmail, String code) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(props.getProperty("mail.sender.username"), props.getProperty("mail.sender.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.sender.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("您的登录验证码");
            message.setText("您好，\n\n您正在登录智能实验室管理平台，您的验证码是：\n\n" + code + "\n\n该验证码5分钟内有效，请勿泄露给他人。\n\n智能实验室团队");

            Transport.send(message);
            System.out.println("Verification code sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成一个6位的随机数字验证码
     * @return 6位数字字符串
     */
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}