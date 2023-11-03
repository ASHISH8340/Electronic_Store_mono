package com.electronicstore.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtResponseDto {

    private String jwtToken;

    private String refreshToken;
    private UserDto user;
}
