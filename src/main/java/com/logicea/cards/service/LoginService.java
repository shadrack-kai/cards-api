package com.logicea.cards.service;

import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.dto.response.UserDetailsDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;

public interface LoginService {

    ApiResponseDto<UserDetailsDto> authenticateUser(LoginRequestDto loginRequestDto);

}
