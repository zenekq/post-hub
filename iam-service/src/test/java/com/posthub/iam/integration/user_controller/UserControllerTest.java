package com.posthub.iam.integration.user_controller;

import com.posthub.iam.factory.TestDataFactory;
import com.posthub.iam.model.dto.user.UserDTO;
import com.posthub.iam.model.entity.User;
import com.posthub.iam.model.request.user.NewUserRequest;
import com.posthub.iam.model.request.user.UpdateUserRequest;
import com.posthub.iam.model.responce.ApiResult;
import com.posthub.iam.repository.UserRepository;
import com.posthub.iam.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

import static com.posthub.iam.util.TestUtil.parseDTOResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class UserControllerTest {

    @Autowired @Setter
    private MockMvc mockMvc;

    @Autowired @Setter
    private JwtTokenProvider jwtTokenProvider;

    @Autowired @Setter
    private UserRepository userRepository;

    private final TestDataFactory testDataFactory = new TestDataFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String adminJwt;
    private String userJwt;

    private final String prefix = "/users";

    @Value("${end.points.all}")
    private String endPointAll;

    @Value("${end.points.create}")
    private String endPointCreate;

    @BeforeAll
    @Transactional
    void authorize() {

        User admin = userRepository.findByIdAndDeletedFalseOrThrow(1);

        adminJwt = "Bearer " + jwtTokenProvider.generateToken(admin);

        User user = userRepository.findByIdAndDeletedFalseOrThrow(3);

        userJwt = "Bearer " + jwtTokenProvider.generateToken(user);
    }

    @Test
    void getAllUsers_200_OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(prefix + endPointAll)
                        .header(HttpHeaders.AUTHORIZATION, adminJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllUsers_Unauthorized_401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(prefix + endPointAll))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    void createUser_AsAdmin_OK_200() throws Exception {
        NewUserRequest request = testDataFactory.createTestNewUserRequest();

        MvcResult requestResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix + endPointCreate)
                        .header(HttpHeaders.AUTHORIZATION, adminJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ApiResult<UserDTO> response = parseDTOResponse(requestResult.getResponse().getContentAsByteArray(), UserDTO.class);

        UserDTO resultBody = Objects.nonNull(response.getPayload()) ? response.getPayload() : null;
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(resultBody);
        Assertions.assertEquals(request.email(), resultBody.getEmail());
        Assertions.assertEquals(request.username(), resultBody.getUsername());
    }

    @Test
    @Transactional
    void createUser_asUser_withoutAccess_403() throws Exception {

        NewUserRequest request = testDataFactory.createTestNewUserRequest();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(prefix + endPointCreate)
                        .header(HttpHeaders.AUTHORIZATION, userJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void updateUser_AsAdmin_OK_200() throws Exception {
        UpdateUserRequest request = testDataFactory.createTestUpdateUserRequest();

        mockMvc.perform(MockMvcRequestBuilders
                        .put(prefix +"/3")
                        .header(HttpHeaders.AUTHORIZATION, adminJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    void deleteUser_AsUser_withoutAccess_403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(prefix +"/4")
                        .header(HttpHeaders.AUTHORIZATION, userJwt)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
