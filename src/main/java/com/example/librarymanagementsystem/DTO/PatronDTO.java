package com.example.librarymanagementsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
public class PatronDTO {

    private Long id;
    private String name;
    private String contactInformation;

}