package edu.ut.sales.sales_analyst.services.impl;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface IPasswordService {

    void verifyEmailAndSendOTP(String email) throws MessagingException, IOException;

    void verifyOTP(String email, Integer otp);
}
