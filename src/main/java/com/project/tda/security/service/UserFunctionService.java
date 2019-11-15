package com.project.tda.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.tda.models.ThreadDumps;
import com.project.tda.models.UsersDto;
import com.project.tda.models.security.User;
import com.project.tda.repositories.ThreadDumpsRepo;
import com.project.tda.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class UserFunctionService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadDumpsRepo threadDumpsRepo;

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

    public List<UsersDto> getTeamMembers(String username){
     String team= userRepository.findByUsername(username).getTeam();
        List<User> members=userRepository.findAllByTeam(team);

        List<UsersDto> dtomembers=new ArrayList<>();
        members.forEach(member->{
            dtomembers.add(new UsersDto(member.getUsername(),member.getFirstname()));
        });
        return dtomembers;
    }

    public String shareAnalyze(String username,int threadid){
        User shared=userRepository.findByUsername(username);
        Set<ThreadDumps> dumps=shared.getSharedThreads();

        Optional<ThreadDumps> threadDump=threadDumpsRepo.findById(threadid);
        if (threadDump.isPresent()){
            dumps.add(threadDump.get());
        }
        return "shared";
    }

    public Set<ThreadDumps> getsharedtome(String username){
        User user=userRepository.findByUsername(username);
        return user.getSharedThreads();
    }


    public String deleteAnalyze(int threadid){
        Optional<ThreadDumps> threadDump=threadDumpsRepo.findById(threadid);
        if (threadDump.isPresent()){
            threadDumpsRepo.delete(threadDump.get());
            return "sucess";

        }
        return"error";
    }
}
