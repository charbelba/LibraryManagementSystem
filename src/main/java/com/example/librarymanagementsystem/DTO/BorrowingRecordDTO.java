package com.example.librarymanagementsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BorrowingRecordDTO {

    private Long id;
    private Long bookId;
    private Long patronId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;

}