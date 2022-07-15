package com.homework.jwtTest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.jwtTest.domain.Member;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtCustomFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final String secret = "szsTest";

    public JwtCustomFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/szs/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //인증 시도
        ObjectMapper om = new ObjectMapper();
        Member member = null;
        try {
            member = om.readValue(request.getInputStream(), Member.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

        // 인증이 되었는지 테스트
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        System.out.println("로그인 완료됨 : "+details.getMember().getId()); // 로그인이 정상적으로 되었다는 뜻.

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails principalDetails = (CustomUserDetails) authResult.getPrincipal();


        String jwt = jwtTokenProvider.generateToken(principalDetails.getUsername());

        response.addHeader("Authorization", "Bearer "+jwt);

        System.out.println("jwt = Bearer " + jwt);

    }
}
