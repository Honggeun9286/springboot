package com.global.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AccountRepository accountRepository;

  @DisplayName("회원 가입 화면 테스트")
  @Test
  void signUpForm() throws Exception{
    mockMvc.perform(get("sign-up"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(view().name("account/sign-up"))
           .andExpect(model().attributeExists("signUpForm"))
           .andExpect(unauthenticated());

  }

  // csrf (Cross Site Request Forgery) : Spring Security 에서 보안 수준을 향상시키는 기능
  @DisplayName("회원 가입 처리 확인하기 - 입력값 오류 테스트")
  @Test
  void signUpSubmit_with_wrong_input() throws Exception{
    mockMvc.perform(post("/sign-up")
           .param("nickName", "global")
           .param("email", "email......")
           .param("password", "1234")
           .with(csrf()))
           .andExpect(status().isOk())
           .andExpect(view().name("account/sign-up"));
  }

  @DisplayName("회원 가입 처리 확인하기 - 입력값 정상인 경우")
  @Test
  void signUpSubmit_with_correct_input() throws Exception{
    mockMvc.perform(post("/sign-up")
           .param("nickName", "test")
           .param("email", "test@global.com")
           .param("password", "12345678")
           .with(csrf()))
           .andExpect(status().is3xxRedirection())
           .andExpect(view().name("redirect:/"));

    assertTrue(accountRepository.existsByEmail("test@global.com"));
  }


}