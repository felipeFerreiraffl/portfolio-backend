package com.portfolio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotBlank(message = "Image source cannot be blank")
    @URL(message = "Image URL is not valid")
    private String imgSrc;

    @NotBlank(message = "Github link cannot be blank")
    @URL(message = "Github URL is not valid")
    private String github;
}
