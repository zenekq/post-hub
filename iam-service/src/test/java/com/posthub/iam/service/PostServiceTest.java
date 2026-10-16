package com.posthub.iam.service;

import com.posthub.iam.mapper.PostMapper;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.Post;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.exception.NotFoundException;
import com.posthub.iam.model.request.post.NewPostRequest;
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

        testPostDTO = new PostDTO(
                testPost.getId(),
                testPost.getTitle(),
                testPost.getContent(),
                testPost.getLikes(),
                testPost.getCreated(),
                testPost.getCreatedBy()
        );
    }

    @Test
    void getById_PostExists_ReturnsPostDTO() {
        when(postRepository.findByIdAndDeletedFalse(1)).thenReturn(Optional.of(testPost));
        when(postMapper.toPostDTO(testPost)).thenReturn(testPostDTO);

        PostDTO result = postService.getById(1).getPayload();

        assertNotNull(result);
        assertEquals(testPost.getId(), result.id());
        assertEquals(testPost.getTitle(), result.title());
        assertEquals(testPost.getContent(), result.content());

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

    @Test
    void createPost_OK() {
        NewPostRequest newPostRequest = new NewPostRequest("New Title", "New Content", 100);

        when(apiUtils.getUserIdFromAuthentication()).thenReturn(testUser.getId());
        when(userRepository.findByIdAndDeletedFalse(testUser.getId())).thenReturn(Optional.of(testUser));
        when(postMapper.createPost(newPostRequest, testUser, testUser.getUsername())).thenReturn(testPost);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        when(postMapper.toPostDTO(testPost)).thenReturn(testPostDTO);

        PostDTO result = postService.createPost(newPostRequest).getPayload();

        assertNotNull(result);
        assertEquals(testPostDTO.id(), result.id());
        assertEquals(testPostDTO.title(), result.title());
        assertEquals(testPostDTO.content(), result.content());

        verify(apiUtils, times(1)).getUserIdFromAuthentication();
        verify(userRepository, times(1)).findByIdAndDeletedFalse(testUser.getId());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postMapper, times(1)).toPostDTO(any(Post.class));

    }

}
