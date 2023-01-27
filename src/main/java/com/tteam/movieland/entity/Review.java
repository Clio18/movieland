package com.tteam.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Long getMovie() {
        return movie.getId();
    }

    public String getUser() {
        return user.getNickname();
    }


}
