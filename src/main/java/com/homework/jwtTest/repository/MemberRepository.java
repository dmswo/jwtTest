package com.homework.jwtTest.repository;

import com.homework.jwtTest.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String userId);

}
