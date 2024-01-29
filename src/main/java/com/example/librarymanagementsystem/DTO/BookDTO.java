package com.example.librarymanagementsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@AllArgsConstructor
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private String isbn;
}