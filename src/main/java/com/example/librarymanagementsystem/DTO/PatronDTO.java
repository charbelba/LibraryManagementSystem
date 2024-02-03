package com.example.librarymanagementsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String contactInformation;

}
