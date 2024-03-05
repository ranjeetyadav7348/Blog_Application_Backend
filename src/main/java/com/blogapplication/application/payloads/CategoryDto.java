package com.blogapplication.application.payloads;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;

    @NotBlank
    @Size(min = 6,message = "Character must be more than or equal to 6 character")
    private String categoryTilte;

    @NotBlank
    @Size(min = 10,message = "Character must be more than or equal to 10 character for Description")
    private String categoryDescription;
    
}
