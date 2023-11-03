package com.electronicstore.controller;

import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.UserDto;
import com.electronicstore.model.Role;
import com.electronicstore.model.User;
import com.electronicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ModelMapper mapper;
//
//    private User user;
//    private Role role;
//
//    @Autowired
//    private MockMvc mockMvc;
//
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
//    }
//
//    @Test
//    public void createUserTest() throws Exception {
////        /users +POST+ user data as json
//        //data as json+status created
//
//        UserDto dto = mapper.map(user, UserDto.class);
//
//        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);
//
//        //actual request for url
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2hpc2hAZ21haWwuY29tIiwiaWF0IjoxNjk0OTQ1ODg2LCJleHAiOjE2OTQ5NDk0ODZ9.qhnhgbY4bTNXqhAa5WyqTnDW4MUUBC9_XvoeOOA8snnLsLqDchNIHcMf2hDHYoFOURxr8tUqyiY5uTryQBinPw ")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(convertObjectToJsonString(user))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").exists());
//
//    }
//
//    @Test
//    public void updateUserTest() throws Exception {
//        String userId ="123";
//
//        UserDto dto = this.mapper.map(user, UserDto.class);
//        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
//                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2hpc2hAZ21haWwuY29tIiwiaWF0IjoxNjk0OTQ1ODg2LCJleHAiOjE2OTQ5NDk0ODZ9.qhnhgbY4bTNXqhAa5WyqTnDW4MUUBC9_XvoeOOA8snnLsLqDchNIHcMf2hDHYoFOURxr8tUqyiY5uTryQBinPw ")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(convertObjectToJsonString(user))
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").exists());
//
//
//    }
////    @Test
////    public void getAllUserTest() throws Exception {
////        UserDto object1 = UserDto.builder().name("Ashish").email("ashish@gmail.com").password("ashish").build();
////        UserDto object2 = UserDto.builder().name("Amith").email("amith@gmail.com").password("amith").build();
////        UserDto object3 = UserDto.builder().name("Abhishek").email("abhishek@gmail.com").password("abhishek").build();
////
////        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
////
////        pageableResponse.setContent(Arrays.asList(
////            object1,object2,object3
////        ));
////
////        pageableResponse.setLastPage(false);
////        pageableResponse.setPageNumber(10);
////        pageableResponse.setTotalPages(100);
////        pageableResponse.setPageSize(2);
////        pageableResponse.setTotalElements(5);
////
////        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString()))
////                .thenReturn(pageableResponse);
////
////        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
////                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk());
////
////
////    }
//
//    private String convertObjectToJsonString(Object user) {
//
//        try{
//            return new ObjectMapper().writeValueAsString(user);
//        }catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }


}
