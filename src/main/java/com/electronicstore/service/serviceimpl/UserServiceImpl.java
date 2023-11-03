package com.electronicstore.service.serviceimpl;

import com.electronicstore.dto.PageableResponse;
import com.electronicstore.dto.RoleDto;
import com.electronicstore.dto.UserDto;
import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.helper.Helper;
import com.electronicstore.model.Role;
import com.electronicstore.model.User;
import com.electronicstore.repository.RoleRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Value("${normal.role.id}")
    private String normalRoleId;
    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            String userId = UUID.randomUUID().toString();
            userDto.setUserId(userId);

            User user = dtoToEntity(userDto);
            user.getRole();
            Role role = roleRepository.findById(normalRoleId).get();
            user.getRole().add(role);
            User savedUser = userRepository.save(user);
            UserDto newDto = entityToDto(savedUser);
            //map roles to roleDto object
            Set<RoleDto> roleDto = savedUser.getRole().stream().map((element) -> mapper.map(element,RoleDto.class)).collect(Collectors.toSet());
            newDto.setRole(roleDto);

            return new Response<>("User saved Successfully", "1", newDto).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in createUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        try {
            logger.info("Inside updateUser of UserServiceImpl");
            Optional<User> findById = userRepository.findById(userId);
            if (findById.isPresent()) {
                User user = findById.get();
                user.setName(userDto.getName());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                user.setGender(userDto.getGender());
                user.setAbout(userDto.getAbout());
                user.setImageName(userDto.getImageName());

                User updatedUser = userRepository.save(user);

                UserDto updatedDto = entityToDto(updatedUser);
                return new Response<>("User updated", "1", updatedDto).getData();
            }
            throw new IdNotFoundException("User not found for given Id");
        } catch (Exception e) {
            String errorMsg = MessageFormat.format("Exception caught in updateUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());
        }
    }


    @Override
    public Object deleteUser(String userId) {
        try{
            logger.info("Inside deleteUser of UserServiceImpl");
            Optional<User> users = userRepository.findById(userId);
            if(users.isEmpty()){
                throw  new IdNotFoundException("User doesn't exist");
            }
            User user = users.get();
            //delete user profile image
            String fullPath = imagePath + user.getImageName();
            Path path = Paths.get(fullPath);
            Files.delete(path);

            user.getRole().clear();
            userRepository.delete(user);
           return new Response<>("User deleted successfully", "1", user);
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in deleteUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try {
            logger.info("Inside getAllUser of UserServiceImpl");
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
            Page<User> page = userRepository.findAll(pageable);
            PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

            return new Response<>("User fetched successfully", "1",response).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getAllUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public UserDto getUserById(String userId) {
        try{
            logger.info("Inside getUserById of UserServiceImpl");
            Optional<User> findById = userRepository.findById(userId);
            if(findById.isEmpty()){
                throw new IdNotFoundException("UserId doesn't exist");
            }
            User user =findById.get();
            UserDto userDto = entityToDto(user);
            return new Response<>("User fetched successfully by userId","1",userDto).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getUserById of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }

    }

    @Override
    public UserDto getUserByEmail(String email) {
        try {
            logger.info("Inside getUserByEmail of UserServiceImpl");
            Optional<User> findByEmail = userRepository.findByEmail(email);
            if(findByEmail.isEmpty()){
                throw new IdNotFoundException("Email doesn't exist");
            }
            User user = findByEmail.get();
            UserDto userDto = entityToDto(user);
            return new Response<>("User fetched successfully by email","1",userDto).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in getUserByEmail of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        try {
            logger.info("Inside searchUser of UserServiceImpl");
            List<User> users = userRepository.findByNameContaining(keyword);
            List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

            return new Response<>("User search successfully by Name","1",dtoList).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in searchUser of UserServiceImpl class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDto entityToDto(User savedUser) {
        UserDto userDto = UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .gender(savedUser.getGender())
                .about(savedUser.getAbout())
                .imageName(savedUser.getImageName())
                .role(savedUser.getRole().stream()
                        .map(role -> mapper.map(role, RoleDto.class))
                        .collect(Collectors.toSet())).build();

        return userDto;
    }

    private User dtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .gender(userDto.getGender())
                .about(userDto.getAbout())
                .imageName(userDto.getImageName())
                .role(new HashSet<>())
                .build();


        return user;
    }
}
