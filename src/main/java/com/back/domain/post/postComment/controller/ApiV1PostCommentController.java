package com.back.domain.post.postComment.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class ApiV1PostCommentController {
    private final PostService postService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<PostCommentDto> getItems(@PathVariable int postId) {
        Post post = postService.findById(postId).get();

        return post
                .getComments()
                .stream()
                .map(PostCommentDto::new) // PostCommentDto로 변환
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostCommentDto getItem(@PathVariable int id , @PathVariable int postId) {
        Post post = postService.findById(postId).get();

        PostComment postComment = post.findCommentById(id).get();


        return new PostCommentDto(postComment);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> delete(@PathVariable int id , @PathVariable int postId) {
        Post post = postService.findById(postId).get();



        PostComment postComment = post.findCommentById(id).get();

        postService.deleteComment(post, postComment);

        return new RsData<>("200-1", "댓글이 삭제되었습니다.");
    }

    record PostCommentModifyReqBody(
            String content
    ) {
    }

    @PutMapping("/{id}")
    @Transactional
    public RsData<Void> modify(@PathVariable int id , @PathVariable int postId, @RequestBody PostCommentModifyReqBody reqBody) {
        Post post = postService.findById(postId).get();



        PostComment postComment = post.findCommentById(id).get();

        postService.modifyComment(postComment, reqBody.content);

        return new RsData<>("200-1", "%d번 댓글이 수정되었습니다.".formatted(id));
    }

    record PostCommentWriteReqBody(
            @NotBlank
            @Size(min = 2, max = 100)
                    String content
    ) {
    }


    @PostMapping
    @Transactional
    public RsData<PostCommentDto> write(
            @PathVariable int postId,
            @Valid @RequestBody PostCommentWriteReqBody reqBody
    ) {
        Post post = postService.findById(postId).get();

        PostComment postComment = postService.writeComment(post, reqBody.content);

        // 트랜잭션 끝난 후 수행되어야 하는 더티체킹 및 여러가지 작업들을 지금 당장 수행해라.
        //postService.flush();

        return new RsData<>(
                "201-1",
                "%d번 댓글이 작성되었습니다.".formatted(postComment.getId()),
                new PostCommentDto(postComment)
        );
    }



}
