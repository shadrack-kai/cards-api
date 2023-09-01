package com.logicea.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cards.config.ConfigProperties;
import com.logicea.cards.controller.AuthenticationController;
import com.logicea.cards.controller.CardsController;
import com.logicea.cards.enums.UserRole;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.entity.UserEntity;
import com.logicea.cards.repository.CardsRepository;
import com.logicea.cards.repository.UsersRepository;
import com.logicea.cards.security.AuthenticationEntryPointImpl;
import com.logicea.cards.security.JwtTokenProvider;
import com.logicea.cards.security.SecurityConfig;
import com.logicea.cards.security.UserDetailsServiceImpl;
import com.logicea.cards.service.impl.CardsServiceImpl;
import com.logicea.cards.service.impl.LoginServiceImpl;
import com.logicea.cards.stub.CardsStub;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        CardsServiceImpl.class,
        LoginServiceImpl.class,
        JwtTokenProvider.class,
        UserDetailsServiceImpl.class,
        AuthenticationEntryPointImpl.class,
        SecurityConfig.class
})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class
})
@Import({AuthenticationController.class, CardsController.class})
class CardsApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardsRepository cardsRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private ConfigProperties configProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(configProperties.getSecretKey()).thenReturn("O6ZT1viQJcORNox2VVmQvOIqdnDGUCVOyIQTa7wjkn5oUxyVuCceWjibwsov47yz");
        when(configProperties.getValidityPeriod()).thenReturn(3600);
    }

    @SneakyThrows
    @Test
    void authenticateUser_shouldReturnSuccess_whenValidCredentials() {
        //given
        final Long userId = 10L;
        final UserEntity userEntity = CardsStub.getUserEntity(userId);

        final LoginRequestDto loginRequestDto = CardsStub.getLoginRequestDto("test@test.com", "Test!123");

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        //then
        mockMvc.perform(post(("/api/1.0/login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @SneakyThrows
    @Test
    void authenticateUser_shouldReturnFail_whenInValidCredentials() {
        //given
        final Long userId = 10L;
        final UserEntity userEntity = CardsStub.getUserEntity(userId);

        final LoginRequestDto loginRequestDto = CardsStub.getLoginRequestDto("test@test.com", "Test!1234");

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        //then
        mockMvc.perform(post(("/api/1.0/login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"));
    }

    @SneakyThrows
    @Test
    void getCards_shouldReturnSuccess_whenCardsFound() {
        //given
        final Long userId = 10L;
        final UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.com");
        userEntity.setRole(UserRole.MEMBER);

        final LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@test.com");
        loginRequestDto.setPassword("Test!123");

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        //then
        mockMvc.perform(get("/api/1.0/cards")
                        .headers(getHttpHeaders()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJpZCI6NCwicm9sZSI6WyJNRU1CRVIiXSwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTY5MzU1MDU4NSwiZXhwIjoxNjkzNTU0MTg1fQ.AWyfV_yKfz9e6plGvFJj2_PaP0CdMq_R8-1vSZM5s6mruLl3FIyE7GO8QnGjPFtbILfKu21TGCcFC8slGxszIw");
        return httpHeaders;
    }

}
