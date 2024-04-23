package com.manil.dailywise.util;

import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.mail.MailType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StreamUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@Transactional
@Data
@Slf4j
public class MailUtil {

    private String nickname = "";
    private String channelName = "";
    private String withdrawAt = "";
    private String inviteUserEmail = "";
    private String changeRole = "";
    private String domain = "";

    private String targetEmail;
    private String LogoPath;
    private MailType type;
    private String text;


    private JavaMailSender mailSender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    public MailUtil(JavaMailSender mailSender) throws KkeaException {
        try {
            this.mailSender = mailSender;
            message = this.mailSender.createMimeMessage();
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    public void setType(MailType type){
        try {
            String htmlFile = "";

            if (type == MailType.INVITE) {
                htmlFile = "invite.html";
            } else if (type == MailType.ROLE) {
                htmlFile = "changeRole.html";
            } else if (type == MailType.WITHDRAW) {
                htmlFile = "withdraw.html";
            }
            if(htmlFile != "") {
                String html = StreamUtils.copyToString(new ClassPathResource("static/mail/"+htmlFile).getInputStream(), Charset.defaultCharset());
                this.type = type;
                this.text = html;
            }
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }

    public void setFrom(String email, String name) throws UnsupportedEncodingException, MessagingException {
        messageHelper.setFrom(email, name);
    }

    public void setTo(String email) throws MessagingException {
        this.targetEmail = email;
        messageHelper.setTo(email);
    }

    @Async
    public void send() {
        try {
            String replaceHtml = this.text;
            replaceHtml = replaceHtml.replace("{{nickname}}", this.nickname);
            replaceHtml = replaceHtml.replace("{{channelName}}", this.channelName);
            replaceHtml = replaceHtml.replace("{{withdrawAt}}", this.withdrawAt);
            replaceHtml = replaceHtml.replace("{{inviteUserEmail}}", this.inviteUserEmail);
            replaceHtml = replaceHtml.replace("{{changeRole}}", this.changeRole);
            replaceHtml = replaceHtml.replace("{{domain}}", this.domain);

            messageHelper.setText(replaceHtml, true);
            mailSender.send(message);
        } catch (Exception e) {
            MDC.put("mailType",this.type.name());
            MDC.put("targetEmail",this.targetEmail);
            log.info("Mail transmission failed");
        }
    }
}
