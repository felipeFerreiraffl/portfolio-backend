package com.portfolio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drawing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drawing {

    @Id
    private String id;

    @NotBlank(message = "Cannot accept blank URLs")
    @URL(message = "URL is not valid")
    private String imgSrc;
}
