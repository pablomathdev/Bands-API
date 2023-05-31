package com.github.pablomathdev.domain.entities;

import java.time.LocalDate;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Release {

    private String title;

    private LocalDate releaseDate;


}