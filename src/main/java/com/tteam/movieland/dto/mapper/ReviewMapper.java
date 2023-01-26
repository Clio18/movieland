package com.tteam.movieland.dto.mapper;

import com.tteam.movieland.dto.ReviewDto;
import com.tteam.movieland.entity.Review;
import com.tteam.movieland.request.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    @Mapping(source = "movie", target = "movieId")
    @Mapping(source = "user", target = "userNickname")
    ReviewDto toReviewDto(Review review);

    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(source = "text", target = "content")
    Review toReview(ReviewRequest reviewRequest);
}
