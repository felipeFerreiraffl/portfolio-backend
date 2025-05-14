package com.portfolio.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Modelo para posições do futebol
@Document(collection = "position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private PositionAbbr abbr;

    @NotNull
    private String desc;
}
