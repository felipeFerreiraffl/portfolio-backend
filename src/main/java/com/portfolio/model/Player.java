package com.portfolio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "player")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotBlank(message = "Image source cannot be blank")
    @URL(message = "Image URL is not valid")
    private String imgSrc;

    @NotBlank(message = "Player link cannot be blank")
    @URL(message = "Link URL is not valid")
    private String link;

    @NotNull
    private PlayerCategory category;
}
