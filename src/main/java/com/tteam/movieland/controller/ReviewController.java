package com.tteam.movieland.controller;

import com.tteam.movieland.dto.ReviewDto;
import com.tteam.movieland.dto.mapper.ReviewMapper;
import com.tteam.movieland.entity.Review;
import com.tteam.movieland.entity.User;
import com.tteam.movieland.request.ReviewRequest;
import com.tteam.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;


    @GetMapping("/movie/{movieId}")
    protected ResponseEntity<List<ReviewDto>> getAllReviewsByMovieId(@PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    protected ResponseEntity<ReviewDto> getReviewById(@PathVariable Long reviewId) {
        Review review = reviewService.getById(reviewId);
        ReviewDto reviewDto = reviewMapper.toReviewDto(review);
        return ResponseEntity.ok(reviewDto);
    }

    @PostMapping
    @Secured("USER")
    protected ResponseEntity<ReviewDto> addReviewToMovie(@RequestBody ReviewRequest reviewRequest,
                                                         @AuthenticationPrincipal User currentUser) {
        System.out.println(currentUser);
        Review review = reviewService.save(reviewRequest, currentUser);
        ReviewDto reviewDto = reviewMapper.toReviewDto(review);
        return ResponseEntity.ok(reviewDto);
    }
}
