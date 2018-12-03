package org.turnera.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.turnera.server.entity.User;
import org.turnera.server.repository.jpa.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findOne(Long id){
        return userRepository.findOne(id);
    }
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public User save(User user){
        return userRepository.save(user);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }
}
