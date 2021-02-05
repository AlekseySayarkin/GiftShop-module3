package com.epam.esm.web.controller;

import com.epam.esm.model.User;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.dto.AuthRequestDto;
import com.epam.esm.web.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthRequestDto requestDto) {
        try {
            User user = new User();
            user.setLogin(requestDto.getLogin());
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            user = userService.addUser(user);

            return ResponseEntity.ok(getResponse(requestDto, user));
        } catch (ServiceException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    requestDto.getLogin(), requestDto.getPassword())
            );
            User user = userService.getUserByLogin(requestDto.getLogin());

            return ResponseEntity.ok(getResponse(requestDto, user));
        } catch (ServiceException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    private Map<String, Object> getResponse(AuthRequestDto requestDto, User user) {
        String token = jwtTokenProvider.createJwtToken(
                requestDto.getLogin(), user.getRole().getRoleType().toString()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", UserDto.of(user));

        return response;
    }
}
