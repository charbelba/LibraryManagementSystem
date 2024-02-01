package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
import com.example.librarymanagementsystem.Entity.Book;
import com.example.librarymanagementsystem.Entity.BorrowingRecord;
import com.example.librarymanagementsystem.Entity.Patron;
import com.example.librarymanagementsystem.Exception.*;
import com.example.librarymanagementsystem.Repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.PatronRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
@Service
@Slf4j
public class BorrowingService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional(rollbackFor = {BorrowingRecordNotFoundException.class, BookNotFoundException.class, PatronNotFoundException.class})
    public Optional<BorrowingRecordDTO> borrowBook(BorrowingRecordDTO borrowingRecordDTO) {
        log.info("Processing borrowing request for bookId: {}", borrowingRecordDTO.getBookId());

        Book book = bookRepository.findById(borrowingRecordDTO.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + borrowingRecordDTO.getBookId()));

        if (book.isBorrowed()) {
            throw new BookAlreadyBorrowedException("Book already borrowed");
        }

        Patron patron = patronRepository.findById(borrowingRecordDTO.getPatronId())
                .orElseThrow(() -> new PatronNotFoundException("Patron not found with id: " + borrowingRecordDTO.getPatronId()));

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowingDate(LocalDate.now());
        borrowingRecordRepository.save(record);

        book.setBorrowed(true);
        bookRepository.save(book);

        return Optional.ofNullable(convertToDTO(record));
    }

    @Transactional(rollbackFor = BorrowingRecordNotFoundException.class)
    public boolean returnBook(Long id) {
        log.info("Processing return request for borrowingRecordId: {}", id);
        Optional<BorrowingRecord> recordOptional = borrowingRecordRepository.findById(id);

        if (recordOptional.isPresent()) {
            BorrowingRecord borrowingRecord = recordOptional.get();
            borrowingRecord.setReturnDate(LocalDate.now());
            borrowingRecordRepository.save(borrowingRecord);

            Book book = borrowingRecord.getBook();
            book.setBorrowed(false);
            bookRepository.save(book);

            return true;
        } else {
            throw new BorrowingRecordNotFoundException("Borrowing record not found with id: " + id);
        }
    }

    public BorrowingRecordDTO convertToDTO(BorrowingRecord record) {
        return new BorrowingRecordDTO(
                record.getId(),
                record.getBook().getId(),
                record.getPatron().getId()

        );
    }
}
