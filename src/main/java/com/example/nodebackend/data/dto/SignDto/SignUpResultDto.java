package com.example.nodebackend.data.dto.SignDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResultDto {
    private boolean success;

    private int code;

    private String msg;
    private String detailMessage; // 추가된 필드
}
