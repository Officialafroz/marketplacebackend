package com.elevata.marketplace.commons;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void send(String to, String subject, String text) throws MailException;
}
