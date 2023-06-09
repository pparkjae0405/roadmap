package com.example.roadmap.repository;

import com.example.roadmap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    /* 이메일이 Login ID의 역할을 하기 때문에 이메일로 계정 찾는 메소드 */
    Optional<User> findByEmail(String email);
    /* userId로 User를 찾아 리턴하는 메소드 */
    User findByUserId(Long userId);
}
