package com.example.nodebackend.service;

import com.example.nodebackend.data.dto.SignDto.SignInResultDto;
import com.example.nodebackend.data.dto.SignDto.SignUpDto;
import com.example.nodebackend.data.dto.SignDto.SignUpResultDto;

import javax.servlet.http.HttpServletRequest;

public interface SignService {
    SignUpResultDto SignUpVerification(String certification_number, HttpServletRequest request);
    SignUpResultDto SignUp(SignUpDto signUpDto,HttpServletRequest request);
    SignInResultDto SignIn(String userId, String password);


}
