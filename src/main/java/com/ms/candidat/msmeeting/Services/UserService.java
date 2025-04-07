package com.ms.candidat.msmeeting.Services;

import com.ms.candidat.msmeeting.Entities.User;
import com.ms.candidat.msmeeting.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }
    public User adduser(User user) {
        return userRepository.save(user);
    }


}
