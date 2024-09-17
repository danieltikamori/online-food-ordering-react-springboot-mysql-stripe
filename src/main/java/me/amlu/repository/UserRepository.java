package me.amlu.repository;

import me.amlu.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :username AND u.deletedAt IS NULL")
    User findByEmail(@Param(value = "username") String username);

    @Query("SELECT u FROM User u WHERE u.fullName = :fullName AND u.deletedAt IS NULL")
    User findByFullName(@Param(value = "fullName") String fullName);

    @NonNull
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.deletedAt IS NULL")
    Optional<User> findById(@NonNull @Param(value = "userId") Long userId);


    void deleteAllByDeletedAtBefore(Instant threshold);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    boolean existsByEmail(@Param(value = "email") String email);

    @Query("SELECT u FROM User u WHERE u.deletedAt < :anonymizationThreshold")
    List<User> findByDeletedAtBefore(Instant anonymizationThreshold);

}
