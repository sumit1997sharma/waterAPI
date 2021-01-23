package com.water.jwt.api.controller;

import com.water.jwt.api.entity.AuthRequest;
import com.water.jwt.api.entity.User;
import com.water.jwt.api.repository.UserRepository;
import com.water.jwt.api.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class WelcomeController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @GetMapping("/")
    public String welcome() {
        return "Welcome to BACKEND API !!";
    }

    @GetMapping("/testapi")
    public String testapi() {
        return "BACKEND API working fine !!";
    }


    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        log.trace("----------------authenticate------------------");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) throws Exception {
        log.trace("-----------------signup----------------");
        String response="";
        try {
          User signUpUser=new User();
          signUpUser.setUserName(user.getUserName());
          signUpUser.setEmail(user.getEmail());
          signUpUser.setPassword(user.getPassword()); // not hashing for now => to do later if told.
          repository.save(user);
            response="Sign up Successfully";
        } catch (Exception ex) {
            log.trace(ex.getMessage());
            response="Something went wrong";
        }
        return response;
    }

}
