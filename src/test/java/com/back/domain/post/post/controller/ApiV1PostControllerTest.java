package com.back.domain.post.post.controller;


import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;


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

        Post post = postService.findLatest().get();


        resultActions.
                andExpect(handler().handlerType(ApiV1PostController.class)).
                andExpect(handler().methodName("write")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.resultCode").value("200-1")).
                andExpect(jsonPath("$.msg").value("%d번 글이 생성되었습니다.".formatted(post.getId()))).
                andExpect(jsonPath("$.data.id").value(post.getId())).
                andExpect(jsonPath("$.data.title").value("테스트 글 제목")).
                andExpect(jsonPath("$.data.content").value("테스트 글 내용")).
                andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(post.getCreateDate().toString().substring(0, 20)))).
                andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(post.getModifyDate().toString().substring(0, 20))));

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


        Post post = postService.findById(1).get();

        resultActions.
                andExpect(status().isOk()).
                andExpect(handler().handlerType(ApiV1PostController.class)).
                andExpect(handler().methodName("modify")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.resultCode").value("200-1")).
                andExpect(jsonPath("$.msg").value("%d번 글이 수정되었습니다.".formatted(post.getId())));

    }
}
