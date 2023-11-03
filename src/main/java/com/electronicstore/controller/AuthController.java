package com.electronicstore.controller;


import com.electronicstore.exceptions.IdNotFoundException;
import com.electronicstore.exceptions.Response;
import com.electronicstore.model.RefreshToken;
import com.electronicstore.model.Role;
import com.electronicstore.model.User;
import com.electronicstore.repository.RoleRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.security.JwtHelper;
import com.electronicstore.service.RefreshTokenService;
import com.electronicstore.service.UserService;
import com.electronicstore.dto.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${normal.role.id}")
    private String normalRoleId;

    @Value("${admin.role.id}")
    private String adminRoleId;



    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto request){
        this.doAuthenticate(request.getEmail(),request.getPassword());

        //token generate
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        //token store

        UserDto userDto = modelMapper.map(userDetails,UserDto.class);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());


        JwtResponseDto response = JwtResponseDto.builder().jwtToken(token)
                .refreshToken(refreshToken.getRefreshToken()).user(userDto).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            Authentication authentication = manager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }


    @PostMapping("/refresh")
    public JwtResponseDto refreshJwtToken(@RequestBody RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        String token = this.helper.generateToken(user);
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());

        return JwtResponseDto.builder().refreshToken(refreshToken.getRefreshToken())
                .jwtToken(token).user(userDto).build();
    }


    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            User user = modelMapper.map(userDto, User.class);
            user.setUserId(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

//            Role role = roleRepository.findById(normalRoleId).get();
//            user.getRole().add(role);

            Role role1 = roleRepository.findById(adminRoleId).get();
            user.getRole().add(role1);

            User createdUser = userRepository.save(user);
            UserDto createdUserDto = modelMapper.map(createdUser, UserDto.class);


            Set<RoleDto> roleDto = createdUser.getRole().stream().map((element) -> modelMapper.map(element, RoleDto.class)).collect(Collectors.toSet());
            createdUserDto.setRole(roleDto);
            ResponseEntity<UserDto> userDtoResponseEntity = new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
            return new Response<>("User saved Successfully", "1", userDtoResponseEntity).getData();
        }catch (Exception e){
            String errorMsg = MessageFormat.format("Exception caught in createUser of AuthController class : {0}", e);
            logger.error(errorMsg);
            e.printStackTrace();
            throw new IdNotFoundException(e.getMessage());

        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        UserDto userDto = modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class);
        return ResponseEntity.ok(userDto);
    }




}
