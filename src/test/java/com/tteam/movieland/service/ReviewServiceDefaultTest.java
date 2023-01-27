package com.tteam.movieland.service;

import com.tteam.movieland.dto.ReviewDto;
import com.tteam.movieland.dto.mapper.ReviewMapper;
import com.tteam.movieland.entity.*;
import com.tteam.movieland.exception.ReviewNotFoundException;
import com.tteam.movieland.repository.ReviewRepository;
import com.tteam.movieland.request.ReviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceDefaultTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    private ReviewService reviewService;
    private Review review1;
    private Review review2;
    private ReviewDto reviewDto1;
    private ReviewDto reviewDto2;

    @BeforeEach
    void init() {
        reviewService = new ReviewServiceDefault(reviewRepository, reviewMapper);

        review1 = Review.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .movie(Movie.builder().id(1L).build())
                .content("Content 1")
                .build();
        review2 = Review.builder()
                .id(2L)
                .user(User.builder().id(1L).build())
                .movie(Movie.builder().id(1L).build())
                .content("Content 2")
                .build();
        reviewDto1 = ReviewDto.builder()
                .id(1L)
                .content("Content 1")
                .movieId(1L)
                .userNickname("Рональд Рейнольдс")
                .build();
        reviewDto2 = ReviewDto.builder()
                .id(2L)
                .content("Content 1")
                .movieId(1L)
                .userNickname("Дарлин Эдвардс")
                .build();
    }


    @Test
    @DisplayName("Test Get Reviews By Movie Id and check result is not null")
    void testReviewsByMovieId_AndCheckResultNotNull() {
        when(reviewRepository.findByMovieId(1L)).thenReturn(List.of(review1, review2));
        when(reviewMapper.toReviewDto(review1)).thenReturn(reviewDto1);
        when(reviewMapper.toReviewDto(review2)).thenReturn(reviewDto2);

        List<ReviewDto> actualReviews = reviewService.getReviewsByMovieId(1L);
        assertNotNull(actualReviews);
        assertEquals(reviewDto1, actualReviews.get(0));
        assertEquals(reviewDto2, actualReviews.get(1));
        verify(reviewRepository).findByMovieId(1L);
    }

    @Test
    @DisplayName("Test Get Review By Id and check result is not null")
    void testGetReviewById_AndCheckResultNotNull() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.ofNullable(review1));

        Review actualReview = reviewService.getById(1L);
        assertNotNull(actualReview);
        assertEquals(review1, actualReview);
        verify(reviewRepository).findById(1L);
    }

    @Test
    @DisplayName("Test Get Review By Id If Review Not Found")
    void testGetReviewByIdIfReviewNotFound() {
        when(reviewRepository.findById(1L)).thenThrow(ReviewNotFoundException.class);
        assertThrows(ReviewNotFoundException.class, () -> {
            reviewService.getById(1L);
        });
        verify(reviewRepository).findById(1L);
    }

    @Test
    @DisplayName("Test Save Review and check result is not null")
    void testSaveReview_AndCheckResultNotNull() {
        when(reviewRepository.save(review1)).thenReturn(review1);

        ReviewRequest reviewRequest = new ReviewRequest(1L, "text");
        when(reviewMapper.toReview(reviewRequest)).thenReturn(review1);

        User currentUser = User.builder().build();
        Review actualReview = reviewService.save(reviewRequest, currentUser);
        assertNotNull(actualReview);
        assertEquals(review1, actualReview);
        verify(reviewRepository).save(review1);
    }
}