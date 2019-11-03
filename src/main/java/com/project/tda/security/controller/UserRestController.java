package com.project.tda.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.tda.models.security.User;
import com.project.tda.security.JwtTokenUtil;
import com.project.tda.security.JwtUser;
import com.project.tda.security.repository.UserRepository;
import com.project.tda.security.service.UserFunctionService;
import com.project.tda.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserFunctionService userFunctionService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public JwtUser editAuthenticatedUser(HttpServletRequest request,@RequestBody User user) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        userFunctionService.editUser(user, username);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @Transactional
    @PostMapping("auth/registration")
    public ResponseEntity<ObjectNode> createUser(@RequestBody User user) {
        ObjectNode jsonObject = userFunctionService.addUser(user);
        return new ResponseEntity<>(jsonObject, HttpStatus.CREATED);
    }

}
