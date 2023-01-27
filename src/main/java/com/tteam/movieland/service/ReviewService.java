package com.tteam.movieland.service;

import com.tteam.movieland.dto.ReviewDto;
import com.tteam.movieland.entity.Review;
import com.tteam.movieland.entity.User;
import com.tteam.movieland.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    List<ReviewDto> getReviewsByMovieId(Long movieId);
    Review getById(Long reviewId);
    Review save(ReviewRequest reviewRequest, User currentUser);
}
