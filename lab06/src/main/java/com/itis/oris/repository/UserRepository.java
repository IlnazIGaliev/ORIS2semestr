package com.itis.oris.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.itis.oris.model.User;
import java.util.Optional;

@Repository
public class UserRepository {
    public Optional<User> findByUserName(String username) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return Optional.of(new User(username, encoder.encode(username)));
    }
}
