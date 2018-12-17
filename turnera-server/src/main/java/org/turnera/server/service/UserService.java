package org.turnera.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.turnera.server.entity.User;
import org.turnera.server.repository.jpa.UserRepository;
import org.turnera.server.repository.mybatis.UserMapper;

import java.util.List;

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
    public Page<User> findAll(Pageable pageable){
        List<User> users = userMapper.findUserList(new User(), pageable);
        Long total = userMapper.findUserListTotal(new User());
        return new PageImpl<User>(users, pageable, total);
    }
}
