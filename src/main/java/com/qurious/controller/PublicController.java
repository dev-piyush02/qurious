package com.qurious.controller;

import com.idleauth.auth.AuthFacade;
import com.idleauth.pojo.AuthResponse;
import com.idleauth.pojo.VerifiedUser;
import com.qurious.DTO.QuestionDTO;
import com.qurious.entity.QuizQuestion;
import com.qurious.entity.User;
import com.qurious.DTO.UserLogin;
import com.qurious.repository.QuestionRepo;
import com.qurious.service.QuizSessionService;
import com.qurious.service.UserDetailServiceImpl;
import com.qurious.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final AuthFacade authFacade;
    private final QuestionRepo questionRepo;
    private final QuizSessionService quizSessionService;

    PublicController(UserService userService, AuthenticationManager authenticationManager,
                     UserDetailServiceImpl userDetailService, AuthFacade authFacade, QuestionRepo questionRepo, QuizSessionService quizSessionService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.authFacade = authFacade;
        this.questionRepo = questionRepo;
        this.quizSessionService = quizSessionService;
    }

    @PostMapping("/add-user")
    ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLoginDetail, HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDetail.getUserId(), userLoginDetail.getPassword()));
            User user = userService.loadUserById(userLoginDetail.getUserId());
            VerifiedUser verifiedUser= new VerifiedUser();
            verifiedUser.setUserId(user.getUserId());
            List<String> roles= Collections.singletonList(user.getUserRole().toString());
            verifiedUser.setRoles(roles);
            AuthResponse authResponse= authFacade.authenticate(verifiedUser, response);
            System.out.println(authResponse.toString());
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
        }
    }
}
