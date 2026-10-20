package com.posthub.iam.integration.comment_controller;

import com.posthub.iam.IamServiceApplication;
import com.posthub.iam.factory.TestDataFactory;
import com.posthub.iam.model.dto.comment.CommentDTO;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.Comment;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.comment.CommentRequest;
import com.posthub.iam.model.request.comment.UpdateCommentRequest;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

import static com.posthub.iam.util.TestUtil.parseDTOResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = IamServiceApplication.class)
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class CommentControllerTest {

    @Autowired
    @Setter
    private MockMvc mockMvc;

    @Autowired
    @Setter
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String currentJwt;

    @BeforeAll
    @Transactional
    void authorize() {
        User user = userRepository.findByIdAndDeletedFalseOrThrow(1L);

        currentJwt = "Bearer " + jwtTokenProvider.generateToken(user);
    }

    @Test
    void getAllComments_OK_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/comments/all")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void createComment_OK_200() throws Exception {

        CommentRequest request = new CommentRequest(1L, "New Comment");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/comments/create")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void updateComment_OK_200() throws Exception {

        UpdateCommentRequest request = new UpdateCommentRequest(1L, "Update comment");

        MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/comments/1")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ApiResult<CommentDTO> result = parseDTOResponse
                (requestResult.getResponse().getContentAsByteArray(), CommentDTO.class);

        CommentDTO commentDTO = Objects.nonNull(result.getPayload()) ? result.getPayload() : null;

        Assertions.assertNotNull(commentDTO);
        Assertions.assertEquals(request.postId(), commentDTO.postId());
        Assertions.assertEquals(request.message(), commentDTO.message());

    }

    @Test
    @Transactional
    void deleteComment_OK_200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/comments/1")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
