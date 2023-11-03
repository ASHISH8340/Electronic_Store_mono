package com.electronicstore.service;

import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.UserDto;
import com.electronicstore.model.Role;
import com.electronicstore.model.User;
import com.electronicstore.repository.RoleRepository;
import com.electronicstore.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {

//    @Autowired
//    private ModelMapper mapper;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private UserService userService;
//
//
//
//    User user;
//
//    Role role;
//
//    String roleId;
//
//    @BeforeEach
//    public void init(){
//        role = Role.builder()
//                .roleId("abc")
//                .roleName("NORMAL")
//                .build();
//
//        user = User.builder()
//                .name("Ashish")
//                .email("ashish@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("ashish.png")
//                .password("ashish")
//                .role(Set.of(role))
//                .build();
//
//        roleId = "abc";
//    }
//
//
//    //Create User Test
//    @Test
//    public void createUserTest(){
//        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
//        Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
//
//
//        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
//
//        Assertions.assertNotNull(user1);
//
//
//    }
//
//    //Update User Test
//    @Test
//    public void updateUserTest(){
//        String userId ="hhhhhhhiiiiii";
//        UserDto userDto = UserDto.builder()
//                .name("Amith Patel")
//                .about("Senior Software Developer")
//                .gender("Male")
//                .imageName("amith.png")
//                .password("amith")
//                .build();
//
//
//        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
//        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
//
//        UserDto updatedUser = userService.updateUser(userDto, userId);
//        System.out.println(updatedUser.getName());
//        System.out.println(updatedUser.getPassword());
//
//        Assertions.assertNotNull(userDto);
//
//    }
//
//    //Delete User Test
////    @Test
////    public void deleteUserTest() throws IOException {
////        String userId="asdhidh123";
////        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
////
////        Object deleteUser = userService.deleteUser(userId);
////        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
////
////        Assertions.assertNotNull(deleteUser);
////
////
////    }
//
//    @Test
//    public void getAllUserTest(){
//
//      User user1 = User.builder()
//                .name("Abhishek")
//                .email("abhishek@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("abhishek.png")
//                .password("abhishek")
//                .role(Set.of(role))
//                .build();
//
//      User user2 = User.builder()
//                .name("Amith")
//                .email("amith@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("amith.png")
//                .password("amith")
//                .role(Set.of(role))
//                .build();
//
//        List<User> userList = Arrays.asList(user,user1,user2);
//        Page<User> page = new PageImpl<>(userList);
//        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
//        PageableResponse<UserDto> allUser = userService
//                .getAllUser(1, 2, "name", "asc");
//        Assertions.assertEquals(3,allUser.getContent().size());
//    }
//
//    @Test
//    public void getUserByIdTest(){
//        String userId = "ashish";
//        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        UserDto userDto = userService.getUserById(userId);
//        Assertions.assertNotNull(userDto);
//        Assertions.assertEquals(user.getName(),userDto.getName(),"Name Not matched !!");
//    }
//
//    @Test
//    public void getUserByEmail(){
//        String emailId = "ashish@gmail.com";
//        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));
//        UserDto userDto = userService.getUserByEmail(emailId);
//        Assertions.assertNotNull(userDto);
//        Assertions.assertEquals(user.getEmail(),userDto.getEmail(),"Email not matched !!");
//    }
//
//    @Test
//    public void searchUserTest(){
//        User user1 = User.builder()
//                .name("Abhishek")
//                .email("abhishek@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("abhishek.png")
//                .password("abhishek")
//                .role(Set.of(role))
//                .build();
//
//        User user2 = User.builder()
//                .name("Amith Kumar")
//                .email("amith@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("amith.png")
//                .password("amith")
//                .role(Set.of(role))
//                .build();
//
//
//        User user3 = User.builder()
//                .name("Manish Kumar")
//                .email("manish@gmail.com")
//                .about("Software Developer")
//                .gender("Male")
//                .imageName("manish.png")
//                .password("manish")
//                .role(Set.of(role))
//                .build();
//
//        String keywords ="Kumar";
//
//        Mockito.when(userRepository.findByNameContaining(keywords))
//                .thenReturn(Arrays.asList(user,user1,user2,user3));
//
//        List<UserDto> userDtos = userService.searchUser(keywords);
//
//        Assertions.assertEquals(4,userDtos.size(),"size not matched !!");
//
//    }
}
