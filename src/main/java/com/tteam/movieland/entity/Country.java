package com.tteam.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_id_seq")
    @SequenceGenerator(name = "countries_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100)
    private String countryName;

    @JsonIgnore
    @ManyToMany(mappedBy = "countries")
    private Set<Movie> movies = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Country country = (Country) o;
        return countryName != null && Objects.equals(countryName, country.countryName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}