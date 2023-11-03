package com.electronicstore.dto;

import com.electronicstore.model.RefreshToken;
import com.electronicstore.validate.ImageNameValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    @Size(min = 3,max = 20,message = "Invalid name !!")
    @ApiModelProperty(value = "user_name", name = "username", required = true, notes = "user name of new user !!")
    private String name;
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email !!")
//    @Email(message = "Invalid User Email !!")
    @NotBlank(message = "Email is required !!")
    private String email;
    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4,max = 11, message = "Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid
    private String imageName;
    private Set<RoleDto> role = new HashSet<>();

    private RefreshToken refreshToken;
}
