package com.posthub.iam.service;

import com.posthub.iam.mapper.PostMapper;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.repository.PostRepository;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.service.impl.PostServiceImpl;
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
public class PostServiceTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private PostRepository postRepository;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private UserRepository userRepository;

    @Mock()
    private PostMapper postMapper;

    @Mock()
    private ApiUtils apiUtils;

    @InjectMocks
    private PostServiceImpl postService;

    private Post testPost;
    private PostDTO testPostDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("TestUser");

        testPost = new Post();
        testPost.setId(1);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setUser(testUser);

        testPostDTO = new PostDTO();
        testPostDTO.setId(testPost.getId());
        testPostDTO.setTitle(testPost.getTitle());
        testPostDTO.setContent(testPost.getContent());
    }

    @Test
    void getById_PostExists_ReturnsPostDTO() {
        when(postRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testPost));
        when(postMapper.toPostDTO(testPost)).thenReturn(testPostDTO);

        PostDTO result = postService.getById(1).getPayload();

        assertNotNull(result);
        assertEquals(testPost.getId(), result.getId());
        assertEquals(testPost.getTitle(), result.getTitle());
        assertEquals(testPost.getContent(), result.getContent());

        verify(postRepository, times(1)).findByIdAndDeletedFalse(1);
        verify(postMapper, times(1)).toPostDTO(testPost);
    }

    @Test
    void getById_PostExists_ThrowsException() {
        final int NON_EXISTENT_POST_ID = 999;

        when(postRepository.findByIdAndDeletedFalse(NON_EXISTENT_POST_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> postService.getById(NON_EXISTENT_POST_ID));

        assertTrue(exception.getMessage().contains("not found"));

        verify(postRepository, times(1)).findByIdAndDeletedFalse(NON_EXISTENT_POST_ID);
        verify(postMapper, never()).toPostDTO(any(Post.class));
    }

}
