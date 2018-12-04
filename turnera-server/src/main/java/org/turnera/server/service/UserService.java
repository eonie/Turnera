package org.turnera.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.turnera.server.entity.User;
import org.turnera.server.repository.jpa.UserRepository;
import org.turnera.server.repository.mybatis.UserMapper;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    //@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User findOne(Long id){
        return userMapper.getUserById(id);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    //@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }
}
