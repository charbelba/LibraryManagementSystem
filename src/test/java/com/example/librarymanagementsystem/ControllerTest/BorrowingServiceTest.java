package com.example.librarymanagementsystem.ControllerTest;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
import com.example.librarymanagementsystem.Entity.Book;
import com.example.librarymanagementsystem.Entity.BorrowingRecord;
import com.example.librarymanagementsystem.Entity.Patron;
import com.example.librarymanagementsystem.Exception.BookNotFoundException;
import com.example.librarymanagementsystem.Exception.BorrowingRecordNotFoundException;
import com.example.librarymanagementsystem.Exception.PatronNotFoundException;
import com.example.librarymanagementsystem.Repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Repository.PatronRepository;
import com.example.librarymanagementsystem.Service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BorrowingServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    @Test
    public void borrowBook_validBorrowingRecordDTO_shouldReturnBorrowingRecordDTO() {
        BorrowingRecordDTO borrowingRecordDTO = new BorrowingRecordDTO(1L, 1L, LocalDate.now(), null);
        BorrowingRecord savedRecord = new BorrowingRecord(1L, new Book(), new Patron(), LocalDate.now(), null);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(new Patron()));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(savedRecord);

        Optional<BorrowingRecordDTO> result = borrowingService.borrowBook(borrowingRecordDTO);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals(borrowingRecordDTO.getBookId(), result.get().getBookId());
        assertEquals(borrowingRecordDTO.getPatronId(), result.get().getPatronId());
        assertEquals(borrowingRecordDTO.getBorrowingDate(), result.get().getBorrowingDate());
        assertNull(result.get().getReturnDate());
        // Add more assertions as needed
        verify(bookRepository, times(1)).findById(anyLong());
        verify(patronRepository, times(1)).findById(anyLong());
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verifyNoMoreInteractions(bookRepository, patronRepository, borrowingRecordRepository);
    }

    @Test
    public void returnBook_existingBorrowingRecord_shouldReturnTrue() {
        Long borrowingRecordId = 1L;
        BorrowingRecord borrowingRecord = new BorrowingRecord(borrowingRecordId, new Book(), new Patron(), LocalDate.now(), null);

        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        boolean result = borrowingService.returnBook(borrowingRecordId);

        assertTrue(result);
        assertNotNull(borrowingRecord.getReturnDate());
        // Add more assertions as needed
        verify(borrowingRecordRepository, times(1)).findById(borrowingRecordId);
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
        verifyNoMoreInteractions(borrowingRecordRepository);
    }

    @Test
    public void returnBook_nonExistingBorrowingRecord_shouldThrowException() {
        Long borrowingRecordId = 1L;
        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.empty());

        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingService.returnBook(borrowingRecordId));
        verify(borrowingRecordRepository, times(1)).findById(borrowingRecordId);
        verifyNoMoreInteractions(borrowingRecordRepository);
    }

    @Test
    public void returnBook_alreadyReturnedBorrowingRecord_shouldThrowException() {
        Long borrowingRecordId = 1L;
        BorrowingRecord borrowingRecord = new BorrowingRecord(borrowingRecordId, new Book(), new Patron(), LocalDate.now(), LocalDate.now());

        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.of(borrowingRecord));

        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingService.returnBook(borrowingRecordId));
        verify(borrowingRecordRepository, times(1)).findById(borrowingRecordId);
        verifyNoMoreInteractions(borrowingRecordRepository);
    }

}
