package com.example.nodebackend.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertification {
    private final String PREFIX = "sms : "; // 키 값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 3*60; // 인증 번호 유효 시간

    private final StringRedisTemplate stringRedisTemplate;

    //레디스에 저장
    public void createSmsCertification(String phone_num,String certification){
        stringRedisTemplate.opsForValue()
                .set(PREFIX + phone_num,certification, Duration.ofSeconds(LIMIT_TIME));
    }

    //휴대전화 번호에 해당하는 인증번호 불러오기
    public String getSmsCertification(String phone_num){
        return stringRedisTemplate.opsForValue().get(PREFIX + phone_num);
    }

    public void deleteSmsCertification(String phone_num){
        stringRedisTemplate.delete(PREFIX+phone_num);
    }

    public boolean hasKey(String phone_num){
        return stringRedisTemplate.hasKey(PREFIX+phone_num);
    }

}
