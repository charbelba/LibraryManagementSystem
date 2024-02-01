package com.example.librarymanagementsystem.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BorrowingRecordDTO {

    private Long id;
    private Long bookId;
    private Long patronId;
    private LocalDate borrowingDate;
    private LocalDate returnDate;

}