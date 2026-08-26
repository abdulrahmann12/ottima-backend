package com.ottima.finishing_tracking.user.repository;

import com.ottima.finishing_tracking.role.entity.Role;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("""
    SELECT u FROM User u JOIN FETCH u.role r
    WHERE u.username = :value OR u.email = :value OR u.phoneNumber = :value
""")
    Optional<User> findByUsernameOrEmailOrPhoneNumberWithRole(@Param("value") String value);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role r WHERE r = :role",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Page<User> findByRole(@Param("role") Role role, Pageable pageable);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullNameAr) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullNameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            countQuery = "SELECT COUNT(u) FROM User u WHERE " +
                    "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(u.fullNameAr) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(u.fullNameEn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT u FROM User u JOIN FETCH u.role
        WHERE u.userId = :id
    """)
    Optional<User> findByIdWithRole(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"role"})
    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role WHERE u.active = true",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.active = true")
    Page<User> findAllActive(Pageable pageable);

    @Query(value = "SELECT u FROM User u JOIN FETCH u.role WHERE u.active = false",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.active = false")
    Page<User> findAllDeactivated(Pageable pageable);

    long countByActiveTrue();

    long countByActiveFalse();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT u FROM User u 
    WHERE u.username = :value OR u.email = :value OR u.phoneNumber = :value
""")
    Optional<User> findForUpdateByIdentifier(@Param("value") String value);

}
