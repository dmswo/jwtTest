package com.homework.jwtTest.service;

import com.homework.jwtTest.domain.Member;
import com.homework.jwtTest.dto.LoginDto;
import com.homework.jwtTest.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository repository;

    @Transactional
    public void save(Member member) {
        repository.save(member);
    }

    public Member find(String username) {
        return repository.findByUsername(username);
    }

    @Transactional
    public Member login(LoginDto loginDto) {
        return repository.findByUsername(loginDto.getUsername());
    }
}
