package com.project.tda.security.service;

import com.project.tda.customexceptions.UsernameAlreadyExistsException;
import com.project.tda.models.security.Authority;
import com.project.tda.models.security.User;
import com.project.tda.security.repository.AuthorityRepository;
import com.project.tda.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;

    }

    public User save(User user) {

        List<Long> longList = new ArrayList<>();
        longList.add(1L);

        if (userWithThatUsernameAlreadyExists(user))
            throw new UsernameAlreadyExistsException();

        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setAuthorities(new ArrayList<Authority>(authorityRepository.findAllById(longList)));
        newUser.setEnabled(true);
        newUser.setLastPasswordResetDate(new Date());

        return userRepository.save(newUser);

    }

    private boolean userWithThatUsernameAlreadyExists(User user) {

        return userRepository.findByUsername(user.getUsername()) != null;
    }

}
