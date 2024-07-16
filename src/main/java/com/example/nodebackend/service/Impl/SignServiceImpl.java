package com.example.nodebackend.service.impl;

import com.example.nodebackend.data.dao.SignDao;
import com.example.nodebackend.data.dto.CommonResponse;
import com.example.nodebackend.data.dto.SignDto.SignInResultDto;
import com.example.nodebackend.data.dto.SignDto.SignUpDto;
import com.example.nodebackend.data.dto.SignDto.SignUpResultDto;
import com.example.nodebackend.data.dto.SignDto.SmsCertificationDto;
import com.example.nodebackend.data.entity.User;
import com.example.nodebackend.data.repository.UserRepository;
import com.example.nodebackend.jwt.JwtProvider;
import com.example.nodebackend.service.SignService;
import com.example.nodebackend.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class SignServiceImpl implements SignService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final SignDao signDao;

    private Logger logger = LoggerFactory.getLogger(SignServiceImpl.class);

    public SignServiceImpl(UserRepository userRepository,
                           JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder,
                           SmsService smsService,
                           SignDao signDao) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
        this.signDao = signDao;
    }

    @Override
    public SignUpResultDto SignUpVerification(String certification, HttpServletRequest request) {
        String partialPhoneNum = (String) request.getSession().getAttribute("partial_phone_num");
        SignUpResultDto signUpResultDto = new SignUpResultDto();

        // SMS 인증번호 검증
        SmsCertificationDto smsCertificationDto = new SmsCertificationDto();
        smsCertificationDto.setPhone_num(partialPhoneNum);
        smsCertificationDto.setCertification_num(certification);
        if (smsService.verifySms(certification, request)) {
            // 인증 성공 시 RDBMS에 전화번호 저장
            User user = User.builder()
                    .phone_num(partialPhoneNum)
                    .certification_num(true)
                    .build();
            request.getSession().setAttribute("partialUser", user);

            signUpResultDto.setDetailMessage("인증 성공");
            setSuccess(signUpResultDto);
        } else {
            signUpResultDto.setDetailMessage("인증 실패");
            setFail(signUpResultDto);
        }
        return signUpResultDto;
    }

    @Override
    public SignUpResultDto SignUp(SignUpDto signUpDto, HttpServletRequest request) {
        User partialUser = (User) request.getSession().getAttribute("partialUser");

        SignUpResultDto signUpResultDto = new SignUpResultDto();
        if (partialUser != null) {
            User user = User.builder()
                    .userId(signUpDto.getUserId())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .name(signUpDto.getName())
                    .phone_num(partialUser.getPhone_num())
                    .certification_num(true)
                    .address(signUpDto.getAddress())
                    .birth(signUpDto.getBirth())
                    .gender(signUpDto.getGender())
                    .height(signUpDto.getHeight())
                    .guardian_name(signUpDto.getGuardian_name())
                    .guardian_address(signUpDto.getGuardian_address())
                    .guardian_phone_num(signUpDto.getGuardian_phone_num())
                    .roles(Collections.singletonList("MEMBER"))
                    .build();
            logger.info("[user 정보 입력] : {} ", user);

            signDao.SignUp(user);
            signUpResultDto.setDetailMessage("회원가입 성공");
            setSuccess(signUpResultDto);
        } else {
            signUpResultDto.setDetailMessage("회원가입 실패");
            setFail(signUpResultDto);
        }

        return signUpResultDto;
    }

    @Override
    public SignInResultDto SignIn(String userId, String password) {
        User user = userRepository.getByUserId(userId);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        logger.info("[getSignInResult] 패스워드 일치");

        logger.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = new SignInResultDto().builder()
                .token(jwtProvider.createToken(String.valueOf(user.getPhone_num()), user.getRoles()))
                .build();
        logger.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccess(signInResultDto);
        signInResultDto.setDetailMessage("로그인 성공");
        return signInResultDto;
    }

    private void setSuccess(SignUpResultDto signUpResultDto) {
        signUpResultDto.setSuccess(true);
        signUpResultDto.setCode(CommonResponse.SUCCESS.getCode());
        signUpResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFail(SignUpResultDto signUpResultDto) {
        signUpResultDto.setSuccess(false);
        signUpResultDto.setCode(CommonResponse.Fail.getCode());
        signUpResultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
