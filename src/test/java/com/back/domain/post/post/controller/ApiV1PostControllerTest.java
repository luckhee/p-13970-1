package com.back.domain.post.post.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    @DisplayName("글 쓰기")
    void t1() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/v1/posts")
                        .contentType("application/json")
                        .content("""
                                {
                                    "title": "테스트 글 제목",
                                    "content": "테스트 글 내용"
                                }
                                """)
        )
                .andDo(print());

        resultActions.
                andExpect(handler().handlerType(ApiV1PostController.class)).
                andExpect(handler().methodName("write")).
                andExpect(status().isCreated());
    }

    @Test
    @DisplayName("글 수정")
    void t2() throws Exception {
        // 회원가입 요청을 보냅니다.
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/posts/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "제목 new",
                                            "content": "내용 new"
                                        }
                                        """)
                )
                .andDo(print()); // 응답결과를 출력합니다.

        resultActions
                .andExpect(status().isOk());
    }
}
