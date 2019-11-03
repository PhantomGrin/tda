package com.project.tda.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.tda.models.security.User;
import com.project.tda.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class UserFunctionService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public void editUser(User user, String username){
        User dbuser =  userRepository.findByUsername(username);
        dbuser.setEmail(user.getEmail());
        dbuser.setFirstname(user.getFirstname());
        dbuser.setTeam(user.getTeam());
        dbuser.setLastname(user.getLastname());
        userRepository.save(dbuser);
        return;
    }

    public ObjectNode addUser(User user){
        ObjectNode jsonObject = mapper.createObjectNode();
        userService.save(user);
        User dbuser =  userRepository.findByUsername(user.getUsername());
        dbuser.setTeam(user.getTeam());
        userRepository.save(dbuser);
        jsonObject.put("status", "User created.");
        return jsonObject;
    }
}
