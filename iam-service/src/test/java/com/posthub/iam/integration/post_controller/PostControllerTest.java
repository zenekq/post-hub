package com.posthub.iam.integration.post_controller;

import com.posthub.iam.IamServiceApplication;
import com.posthub.iam.factory.PostTestData;
import com.posthub.iam.model.dto.post.PostDTO;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.post.NewPostRequest;
import com.posthub.iam.model.request.post.UpdatePostRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import com.posthub.iam.util.TestUtil;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = IamServiceApplication.class)
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class PostControllerTest {

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
    void getAllPosts_OK_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts/all")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void createPosts_OK_200() throws Exception {

        NewPostRequest request = PostTestData.createTestNewPostRequest();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts/create")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void updatePosts_OK_200() throws Exception {

        UpdatePostRequest request = PostTestData.createTestUpdatePostRequest();

        MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ApiResult<PostDTO> result = TestUtil.parseDTOResponse
                (requestResult.getResponse().getContentAsByteArray(), PostDTO.class);

        PostDTO postDTO = Objects.nonNull(result.getPayload()) ? result.getPayload() : null;

        Assertions.assertNotNull(postDTO);
        Assertions.assertEquals(request.title(), postDTO.title());
        Assertions.assertEquals(request.content(), postDTO.content());

    }

    @Test
    @Transactional
    void deletePosts_OK_200() throws Exception {

        UpdatePostRequest request = new UpdatePostRequest("Update title", "Update content", 0);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/posts/1")
                        .header(HttpHeaders.AUTHORIZATION, currentJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
