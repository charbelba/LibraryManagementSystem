package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
import com.example.librarymanagementsystem.Service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Borrowing Controller", description = "The Borrowing API provides operations for borrowing and returning books.")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @Operation(summary = "Borrow a book", description = "Borrow a book using a borrowing record", responses = {
            @ApiResponse(description = "Borrowing successful", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BorrowingRecordDTO.class))),
            @ApiResponse(description = "Borrowing failed", responseCode = "400")
    })
    @PostMapping("/borrow")
    public ResponseEntity<BorrowingRecordDTO> borrowBook(@RequestBody @Valid BorrowingRecordDTO borrowingRecordDTO) {
        return borrowingService.borrowBook(borrowingRecordDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Return a book", description = "Return a borrowed book by its ID", responses = {
            @ApiResponse(description = "Return successful", responseCode = "200"),
            @ApiResponse(description = "Return failed", responseCode = "400")
    })
    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnBook(@PathVariable @NotNull Long id) {
        if (borrowingService.returnBook(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
