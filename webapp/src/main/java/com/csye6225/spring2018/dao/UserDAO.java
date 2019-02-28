package com.csye6225.spring2018.dao;

import com.csye6225.spring2018.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends CrudRepository<User,String>{
    User findUserByEmailAddress(String emailAddress);
}
