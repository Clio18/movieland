package com.tteam.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_id_seq")
    @SequenceGenerator(name = "genres_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100)
    private String genreName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Genre genre = (Genre) o;
        return genreName != null && Objects.equals(genreName, genre.genreName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}