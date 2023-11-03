package com.electronicstore.service;

import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.UserDto;
import com.electronicstore.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);
    //delete
    Object deleteUser(String userId);
    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get single user by id
    UserDto getUserById(String userId);
    //get single user by email
    UserDto getUserByEmail(String email);
    //search user
    List<UserDto> searchUser(String keyword);

    Optional<User> findUserByEmailOptional(String email);
}
