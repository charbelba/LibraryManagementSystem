package com.example.librarymanagementsystem.Controller;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
import com.example.librarymanagementsystem.Service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingRecordDTO> borrowBook(@RequestBody BorrowingRecordDTO borrowingRecordDTO) {
        return borrowingService.borrowBook(borrowingRecordDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Void> returnBook(@PathVariable Long id) {
        if (borrowingService.returnBook(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
