package com.example.nodebackend.data.dto.SignDto;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCertificationDto {
    private String phone_num;
    private String certification_num;
}
