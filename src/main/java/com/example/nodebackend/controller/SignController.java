package com.example.nodebackend.controller;

import com.example.nodebackend.data.dto.SignDto.SignInResultDto;
import com.example.nodebackend.data.dto.SignDto.SignUpDto;
import com.example.nodebackend.data.dto.SignDto.SignUpResultDto;
import com.example.nodebackend.service.SignService;
import com.example.nodebackend.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sign-api")
public class SignController {

    private final Logger logger = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;
    private final SmsService smsService;

    @Autowired
    public SignController(SignService signService, SmsService smsService) {
        this.signService = signService;
        this.smsService = smsService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSMS(String phone_num, HttpServletRequest request) {
        try {
            String randomNum = smsService.sendSMS(phone_num, request);
            logger.info("[문자 인증 진행중] phoneNumber: {}, randomNum: {}", phone_num, randomNum);
            return ResponseEntity.ok("문자 전송 완료: 인증번호 " + randomNum);
        } catch (Exception e) {
            logger.error("[문자 인증 실패] phoneNumber: {}, error: {}", phone_num, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문자 전송 실패");
        }
    }

    @PostMapping("/sms-verification")
    public ResponseEntity<SignUpResultDto> SignUpVerification(@RequestParam String certification_number, HttpServletRequest request) {
        SignUpResultDto signUpResultDto = signService.SignUpVerification(certification_number, request);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResultDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResultDto> SignUp(@RequestBody SignUpDto signUpDto, HttpServletRequest request) {
        SignUpResultDto signUpResultDto = signService.SignUp(signUpDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(signUpResultDto);
    }

    @PostMapping("/sign-in")
    public SignInResultDto SignIn(@RequestParam String userId, String password) {
        logger.info("[sign-in] 로그인을 시도하고 있습니다. id : {}, password : *****", userId);
        SignInResultDto signInResultDto = signService.SignIn(userId, password);
        if (signInResultDto.getCode() == 0) {
            logger.info("[sign-in] 정상적으로 로그인이 되었습니다. id: {}, token : {}", userId, signInResultDto.getToken());
        }
        return signInResultDto;
    }
}
