package com.portfolio.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    @Size(max = 3, min = 2, message = "A sigla deve ter 2 ou 3 letras!")
    private PositionAbbr abbr;

    @NotNull
    private String desc;
}
