package com.example.nodebackend.data.dto.SignDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Data
@Getter
@Setter
public class SignUpDto {

    private String name;

    private String gender;

    private String birth;

    private int height;

    private int weight;

    private String address;

    private String userId;

    private String password;

    private String guardian_name;

    private int guardian_phone_num;

    private String guardian_address;
}
