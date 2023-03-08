package com.tteam.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_id_seq")
    @SequenceGenerator(name = "movies_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name_ukr")
    private String nameUkr;

    @Column(name = "name_native")
    private String nameNative;

    @Column(name = "year")
    private Integer yearOfRelease;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "price", scale = 2)
    @JdbcTypeCode(SqlTypes.NUMERIC)
    private Double price;

    @Column(name = "rating", scale = 1)
    @JdbcTypeCode(SqlTypes.NUMERIC)
    private Double rating;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "movie_country",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<Country> countries = new LinkedHashSet<>();

    @Column(name = "picture_path", length = 500)
    private String poster;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new LinkedHashSet<>();

    @OneToMany(mappedBy = "movie")
    @ToString.Exclude
    private Set<Review> reviews;

    /*For testing purposes
    VM option -Xmx150m
    @ToString.Exclude
    @Transient
    private byte [] weight = new byte[10 * 1024 *1024];*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movie movie = (Movie) o;
        return id != null && Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameUkr, nameNative, yearOfRelease, description, price, rating, countries, poster, genres);
    }
}