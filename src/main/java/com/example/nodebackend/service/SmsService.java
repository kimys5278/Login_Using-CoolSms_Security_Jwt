package com.example.nodebackend.service;

import com.example.nodebackend.data.dto.SignDto.SmsCertificationDto;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface SmsService {
    HashMap<String, String> makeprams(String to, String randomNum);
    String createRandomNumber();
    String sendSMS(String phoneNum,HttpServletRequest request);
    boolean verifySms(String certification,HttpServletRequest request);

}
