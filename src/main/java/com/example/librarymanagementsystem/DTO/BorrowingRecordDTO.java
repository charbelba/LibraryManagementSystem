package com.example.librarymanagementsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordDTO {

    private Long id;

    @NotNull
    private Long bookId;

    @NotNull
    private Long patronId;

}
