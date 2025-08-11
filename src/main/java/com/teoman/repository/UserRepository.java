package com.teoman.repository;

import com.teoman.model.User;
import com.teoman.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuth(UserAuth auth);


}
