package com.elevata.marketplace.auth;

import com.elevata.marketplace.auth.domain.OtpEmailRequest;
import com.elevata.marketplace.auth.domain.OtpRequest;
import com.elevata.marketplace.user.AddAppUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String registerCustomer(HttpServletResponse response, AddAppUser appUser);

    String login(OtpEmailRequest otpEmailRequest);

    String verify(HttpServletResponse response, OtpRequest otpRequest);
}
