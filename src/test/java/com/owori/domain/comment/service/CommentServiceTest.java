package com.owori.domain.comment.service;

import com.owori.domain.comment.dto.request.AddCommentRequest;
import com.owori.domain.comment.dto.request.UpdateCommentRequest;
import com.owori.domain.comment.entity.Comment;
import com.owori.domain.comment.repository.CommentRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.domain.story.entity.Story;
import com.owori.domain.story.repository.StoryRepository;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.support.database.DatabaseTest;
import com.owori.support.database.LoginTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DatabaseTest
@DisplayName("Comment 서비스의")
public class CommentServiceTest extends LoginTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired StoryRepository storyRepository;
    @Autowired AuthService authService;

    @Autowired private EntityManager em;

    @Test
    @DisplayName("부모 댓글이 없는 경우 댓글 작성이 이루어지는가")
    void addComment() {
        //given
        String content = "댓글 내용 입니다.";
        Member member = authService.getLoginUser();
        Story story = new Story("댓글 작성이 제대로", "될까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);

        //when
        commentService.addComment(new AddCommentRequest(story.getId(), null, content));

        //then
        List<Comment> commentList = commentRepository.findAll();
        Story saveStory = storyRepository.findById(story.getId()).get();

        assertThat(commentList.get(0).getParent()).isEqualTo(null);
        assertThat(saveStory.getComments().get(0).getParent()).isEqualTo(null);
        assertThat(saveStory.getComments().get(0).getMember()).isEqualTo(member);
        assertThat(commentList.get(0).getContent()).isEqualTo(content);
        assertThat(saveStory.getComments().get(0).getContent()).isEqualTo(content);

    }

    @Test
    @DisplayName("대댓글 작성이 이루어지는가")
    void addCommentWithParent() {
        //given
        String content = "댓글 내용 입니다.";
        Member member = authService.getLoginUser();
        Story story = new Story("댓글 작성이 제대로", "될까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);
        Comment parent = new Comment(member, story, null, "나는 부모 댓글");
        commentRepository.save(parent);

        //when
        commentService.addComment(new AddCommentRequest(story.getId(), parent.getId(), content));

        //then
        List<Comment> commentList = commentRepository.findAll();
        Story saveStory = storyRepository.findById(story.getId()).get();

        assertThat(commentList.get(1).getParent()).isEqualTo(parent);
        assertThat(saveStory.getComments().get(0).getParent()).isEqualTo(parent);
        assertThat(saveStory.getComments().get(0).getMember()).isEqualTo(member);
        assertThat(commentList.get(1).getContent()).isEqualTo(content);
        assertThat(saveStory.getComments().get(0).getContent()).isEqualTo(content);

    }

    @Test
    @DisplayName("댓글 삭제가 제대로 실행되는가")
    void deleteComment() {
        //given
        Member member = authService.getLoginUser();
        Story story = new Story("댓글이 제대로", " 삭제 될까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);

        Comment comment = new Comment(member, story, null, "나는 댓글");
        commentRepository.save(comment);

        //when
        commentService.removeComment(story.getId(), comment.getId());
        em.flush();
        em.clear();

        //then
        assertThrows(EntityNotFoundException.class, () -> commentService.loadEntity(comment.getId()));

    }

    @Test
    @DisplayName("댓글 수정이 제대로 되는가")
    void updateComment() {
        //given
        String content = "댓글 수정했습니다";

        Member member = authService.getLoginUser();
        Story story = new Story("댓글이 제대로", " 삭제 될까요 ?", LocalDate.of(2000, 4, 22), LocalDate.of(2022, 8, 22), member);
        storyRepository.save(story);

        Comment comment = new Comment(member, story, null, "나는 댓글");
        commentRepository.save(comment);

        //when
        commentService.updateComment(comment.getId(),new UpdateCommentRequest(content));

        //then
        Comment updateComment = commentRepository.findById(comment.getId()).get();
        assertThat(updateComment.getContent()).isEqualTo(content);

    }

}
