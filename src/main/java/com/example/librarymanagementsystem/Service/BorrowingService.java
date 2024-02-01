package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
import com.example.librarymanagementsystem.Entity.BorrowingRecord;
import com.example.librarymanagementsystem.Exception.BookException;
import com.example.librarymanagementsystem.Exception.BookNotFoundException;
import com.example.librarymanagementsystem.Exception.BorrowingRecordNotFoundException;
import com.example.librarymanagementsystem.Exception.PatronNotFoundException;
import com.example.librarymanagementsystem.Repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.PatronRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional
    public Optional<BorrowingRecordDTO> borrowBook(BorrowingRecordDTO borrowingRecordDTO) {
        log.info("Processing borrowing request for bookId: {}", borrowingRecordDTO.getBookId());
        BorrowingRecord record = convertToEntity(borrowingRecordDTO);
        BorrowingRecord savedRecord = borrowingRecordRepository.save(record);
        return Optional.of(convertToDTO(savedRecord));
    }

    @Transactional
    public boolean returnBook(Long id) {
        log.info("Processing return request for borrowingRecordId: {}", id);
        Optional<BorrowingRecord> record = borrowingRecordRepository.findById(id);
        if (record.isPresent()) {
            BorrowingRecord borrowingRecord = record.get();
            borrowingRecord.setReturnDate(java.time.LocalDate.now());
            borrowingRecordRepository.save(borrowingRecord);
            return true;
        } else {
            throw new BorrowingRecordNotFoundException("Borrowing record not found with id: " + id);
        }
    }

    public BorrowingRecordDTO convertToDTO(BorrowingRecord record) {
        return new BorrowingRecordDTO(
                record.getId(),
                record.getBook().getId(),
                record.getPatron().getId(),
                record.getBorrowingDate(),
                record.getReturnDate()
        );
    }

    public BorrowingRecord convertToEntity(BorrowingRecordDTO dto) {
        BorrowingRecord record = new BorrowingRecord();

        record.setBook(
                bookRepository.findById(dto.getBookId())
                        .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + dto.getBookId()))
        );

        record.setPatron(
                patronRepository.findById(dto.getPatronId())
                        .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + dto.getPatronId()))
        );

        record.setBorrowingDate(dto.getBorrowingDate());
        record.setReturnDate(dto.getReturnDate());

        return record;
    }

}
