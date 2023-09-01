package com.logicea.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cards.config.ConfigProperties;
import com.logicea.cards.controller.AuthenticationController;
import com.logicea.cards.controller.CardsController;
import com.logicea.cards.enums.CardStatus;
import com.logicea.cards.enums.UserRole;
import com.logicea.cards.exception.CustomExceptionHandler;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.model.entity.UserEntity;
import com.logicea.cards.repository.CardsRepository;
import com.logicea.cards.repository.UsersRepository;
import com.logicea.cards.security.AuthenticationEntryPointImpl;
import com.logicea.cards.security.JwtTokenProvider;
import com.logicea.cards.security.SecurityConfig;
import com.logicea.cards.security.UserDetailsServiceImpl;
import com.logicea.cards.service.LoginService;
import com.logicea.cards.service.impl.CardsServiceImpl;
import com.logicea.cards.service.impl.LoginServiceImpl;
import com.logicea.cards.stub.CardsStub;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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
        SecurityConfig.class,
        CustomExceptionHandler.class
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

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(configProperties.getSecretKey()).thenReturn("O6ZT1viQJcORNox2VVmQvOIqdnDGUCVOyIQTa7wjkn5oUxyVuCceWjibwsov47yz");
        when(configProperties.getValidityPeriod()).thenReturn(3600);

        loginService = new LoginServiceImpl(authenticationManager, tokenProvider, configProperties, usersRepository);
    }

    @SneakyThrows
    @DisplayName("Login with Valid Credentials")
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
    @DisplayName("Login with Invalid Credentials")
    @Test
    void authenticateUser_shouldReturnUnauthorized_whenInvalidCredentials() {
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
    @DisplayName("Member Fetching Own Cards")
    @Test
    void getCards_shouldReturnSuccess_whenCardsFound() {
        //given
        final UserRole role = UserRole.MEMBER;
        final int page = 1;
        final int size = 2;
        final List<CardEntity> cards = CardsStub.getCards();
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<CardEntity> pageResponse = new PageImpl<>(cards, pageRequest, cards.size());

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(CardsStub.getUserEntity(role)));
        when(cardsRepository.findByUserId(anyLong(), any())).thenReturn(pageResponse);

        //then
        mockMvc.perform(get("/api/1.0/cards")
                        .header(HttpHeaders.AUTHORIZATION, authToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.body.content.length()").value(5));
    }

    @SneakyThrows
    @DisplayName("Member Fetching Own Card")
    @Test
    void getCard_shouldReturnSuccess_whenCardFound() {
        //given
        final UserRole role = UserRole.MEMBER;
        final CardEntity cardEntity = CardsStub.getCardEntity(1, 10L, CardStatus.TODO);

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(CardsStub.getUserEntity(role)));
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));

        //then
        mockMvc.perform(get("/api/1.0/cards/2")
                        .header(HttpHeaders.AUTHORIZATION, authToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @SneakyThrows
    @DisplayName("Member Fetching Other Member's Card")
    @Test
    void getCard_shouldReturnNotFound_whenCardNotFound() {
        //given
        final UserRole role = UserRole.MEMBER;
        final CardEntity cardEntity = CardsStub.getCardEntity(1, 5L, CardStatus.TODO);

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(CardsStub.getUserEntity(10L)));
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));

        //then
        mockMvc.perform(get("/api/1.0/cards/2")
                        .header(HttpHeaders.AUTHORIZATION, authToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.code").value("404"));
    }

    @SneakyThrows
    @DisplayName("Admin Fetching Member's Card")
    @Test
    void getCard_shouldReturnNotFound_whenAdminFetchesCard() {
        //given
        final CardEntity cardEntity = CardsStub.getCardEntity(1, 5L, CardStatus.TODO);

        //when
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(CardsStub.getUserEntity(UserRole.ADMIN)));
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));

        //then
        mockMvc.perform(get("/api/1.0/cards/2")
                        .header(HttpHeaders.AUTHORIZATION, authToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.code").value("200"));
    }

    private String authToken() {
        final LoginRequestDto loginRequestDto = CardsStub.getLoginRequestDto("test@test.com", "Test!123");
        return "Bearer " + loginService.authenticateUser(loginRequestDto).getBody().getAccessToken();
    }

}