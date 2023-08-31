package com.logicea.cards.service.impl;

import com.logicea.cards.config.ConfigProperties;
import com.logicea.cards.enums.UserRole;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.UserDetailsDto;
import com.logicea.cards.security.JwtTokenProvider;
import com.logicea.cards.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final ConfigProperties properties;

    @Override
    public ApiResponseDto<UserDetailsDto> authenticateUser(final LoginRequestDto loginRequestDto) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto
                .getEmail(), loginRequestDto.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(UserRole.MEMBER.toString()))));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String accessToken = tokenProvider.generateToken(authentication);

        return ApiResponseDto.<UserDetailsDto>builder()
                .code("200")
                .response("User login successfully")
                .body(UserDetailsDto.builder().accessToken(accessToken).expiresIn(properties.getValidityPeriod())
                        .type("Bearer")
                        .build())
                .build();
    }

}
