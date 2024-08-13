package me.amlu.repository;

import me.amlu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import java.time.Instant;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String username);

    User findByFullName(String fullName);

    @NonNull
    Optional<User> findById(@NonNull Long userId);
    
    void deleteAllByDeletedAtBefore(Instant threshold);


}
