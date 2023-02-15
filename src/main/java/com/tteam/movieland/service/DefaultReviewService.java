package com.tteam.movieland.service;

import com.tteam.movieland.dto.ReviewDto;
import com.tteam.movieland.dto.mapper.ReviewMapper;
import com.tteam.movieland.entity.Review;
import com.tteam.movieland.entity.User;
import com.tteam.movieland.exception.ReviewNotFoundException;
import com.tteam.movieland.repository.ReviewRepository;
import com.tteam.movieland.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public List<ReviewDto> getReviewsByMovieId(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream().map(reviewMapper::toReviewDto).toList();
    }

    @Override
    public Review getById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Could not find review by id: " + reviewId));
    }

    @Override
    public Review save(ReviewRequest reviewRequest, User currentUser) {
        Review review = reviewMapper.toReview(reviewRequest);
        review.setUser(currentUser);
        return reviewRepository.save(review);
    }
}
