package com.example.nodebackend.data.dao;

import com.example.nodebackend.data.dto.SignDto.SignUpDto;
import com.example.nodebackend.data.dto.SignDto.SignUpResultDto;
import com.example.nodebackend.data.entity.User;


public interface SignDao {
    User SignUp(User user);
}
