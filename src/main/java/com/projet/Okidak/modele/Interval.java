package com.projet.Okidak.modele;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Interval {

    private final LocalDateTime start;
    private final LocalDateTime end;
    
}