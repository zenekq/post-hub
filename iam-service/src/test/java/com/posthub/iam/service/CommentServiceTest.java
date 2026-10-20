package com.posthub.iam.service;

import com.posthub.iam.mapper.CommentMapper;
import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.repository.CommentRepository;
import com.posthub.iam.repository.PostRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.impl.CommentServiceImpl;
import com.posthub.iam.utils.ApiUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CommentServiceTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private CommentRepository commentRepository;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserRepository userRepository;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ApiUtils apiUtils;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentDTO testCommentDTO;
    private User testUser;
    private Post testPost;

    private static final long COMMENT_ID = 1;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("TestUser");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("TestPost");

        testComment = new Comment();
        testComment.setId(COMMENT_ID);
        testComment.setMessage("Test Comment");
        testComment.setPost(testPost);
        testComment.setUser(testUser);

        testCommentDTO = new CommentDTO(
                testComment.getId(),
                testComment.getMessage(),
                null,
                null,
                null,
                null
        );
    }

    @Test
    void getCommentById_Comment_Exist_ReturnCommentDTO() {

        when(commentRepository.findByIdAndDeletedFalse(COMMENT_ID)).thenReturn(Optional.of(testComment));
        when(commentMapper.toDTO(testComment)).thenReturn(testCommentDTO);

        CommentDTO result = commentService.getCommentById(COMMENT_ID).getPayload();

        assertNotNull(result);
        assertEquals(testCommentDTO.id(), result.id());
        assertEquals(testCommentDTO.message(), result.message());

        verify(commentRepository, times(1)).findByIdAndDeletedFalse(COMMENT_ID);
        verify(commentMapper, times(1)).toDTO(testComment);
    }

    @Test
    void getCommentById_CommentNotFound_ThrowsException() {

        final long NON_EXISTENT_COMMENT_ID = 999;

        when(commentRepository.findByIdAndDeletedFalse(NON_EXISTENT_COMMENT_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> commentService.getCommentById(NON_EXISTENT_COMMENT_ID));

        assertTrue(exception.getMessage().contains("not found"));

        verify(commentRepository, times(1)).findByIdAndDeletedFalse(NON_EXISTENT_COMMENT_ID);
        verify(commentMapper, never()).toDTO(any(Comment.class));
    }

    @Test
    void createComment_OK() {

        CommentRequest request = new CommentRequest(1L, "New comment");

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(testUser.getId());
        when(userRepository.findByIdAndDeletedFalse(testUser.getId())).thenReturn(Optional.of(testUser));
        when(postRepository.findByIdAndDeletedFalse(testPost.getId())).thenReturn(Optional.of(testPost));
        when(commentMapper.createComment(request, testUser, testPost)).thenReturn(testComment);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(commentMapper.toDTO(testComment)).thenReturn(testCommentDTO);

        CommentDTO result = commentService.createComment(request).getPayload();

        assertNotNull(result);
        assertEquals(testCommentDTO.message(), result.message());

        verify(apiUtils, times(1)).getUserIdFromAuthentication();
        verify(userRepository, times(1)).findByIdAndDeletedFalse(testUser.getId());
        verify(postRepository, times(1)).findByIdAndDeletedFalse(testPost.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(commentMapper, times(1)).toDTO(any(Comment.class));
    }

}
