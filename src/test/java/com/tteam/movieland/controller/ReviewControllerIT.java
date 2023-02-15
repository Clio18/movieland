package com.tteam.movieland.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.tteam.movieland.AbstractBaseITest;
import com.tteam.movieland.config.QueryCountTestConfig;
import com.tteam.movieland.request.ReviewRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DBRider
@AutoConfigureMockMvc
@Import({QueryCountTestConfig.class})
class ReviewControllerIT extends AbstractBaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test GetAll Reviews By Movie Id")
    void testGetAllReviewsByMovieId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/reviews/movie/{movieId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/reviews/get-all-reviews-by-movie-id.json")));
        assertSelectCount(4);
    }

    @Test
    @WithMockUser
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Get Review By Id")
    void testGetReviewById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/reviews/{reviewId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResponseAsString("response/reviews/get-review-by-id.json")));
        assertSelectCount(1);
    }

    //TODO: principal is null in controller!!!
//    @Test
//    //@WithMockUser(username = "user", roles = "USER")
//    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
//    @DisplayName("Test Add Review To Movie")
//    void testAddReviewToMovie() throws Exception {
//        ReviewRequest reviewRequest = new ReviewRequest(1L, "Das ist fantastisch!");
//
//        User user = User.builder()
//                .id(1L)
//                .email("ronald.reynolds66@example.com")
//                .password("$2a$10$BdidX2ydzIypVqAbqb4.muZhCa8RG7Vp.qDqQH8Wj5DweSIOiHu8W")
//                .nickname("Рональд Рейнольдс")
//                .role(Role.USER)
//                .grantedAuthorities(Set.of(new SimpleGrantedAuthority(Role.USER.name())))
//                .isAccountNonLocked(true)
//                .isCredentialsNonExpired(true)
//                .isAccountNonExpired(true)
//                .isEnabled(true)
//                .build();
//        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/reviews")
//                        .principal(testingAuthenticationToken)
////                        .with(user("user").roles("USER").authorities(Set.of(new SimpleGrantedAuthority(Role.USER.name()))))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reviewRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(getResponseAsString("response/reviews/add-review-to-movie.json")));
//    }

    @Test
    @DataSet(value = "all_dataset.yml", cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @DisplayName("Test Add Review To Movie If Not Authorized")
    void testAddReviewToMovieIfNotAuthorized() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest(1L, "Das ist fantastisch!");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isForbidden());
    }
}