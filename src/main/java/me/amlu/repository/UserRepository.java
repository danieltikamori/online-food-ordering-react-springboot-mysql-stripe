package me.amlu.repository;

import me.amlu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String username);

    User findByFullName(String fullName);


}
