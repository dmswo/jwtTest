package com.homework.jwtTest.controller;

import com.homework.jwtTest.domain.Member;
import com.homework.jwtTest.dto.LoginDto;
import com.homework.jwtTest.dto.MemberDto;
import com.homework.jwtTest.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = {"멤버 API"})
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/szs/signup")
    @ApiOperation(value = "회원가입")
    public ResponseEntity<?> signup(@RequestBody MemberDto memberDto) {

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(bCryptPasswordEncoder.encode(memberDto.getPassword()))
                .build();

        memberService.save(member);

        return new ResponseEntity<MemberDto>(memberDto, HttpStatus.OK);
    }

    @PostMapping("/szs/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        //필터에서 id 및 비밀번호 확인했음
        return new ResponseEntity<>(loginDto, HttpStatus.OK);
    }
}
