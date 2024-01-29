package com.example.librarymanagementsystem.ServiceTest;

import com.example.librarymanagementsystem.DTO.BorrowingRecordDTO;
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
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void borrowBook_validRequest_shouldReturnBorrowingRecordDTO() {
        BorrowingRecordDTO requestDTO = new BorrowingRecordDTO();
        BorrowingRecord savedRecord = new BorrowingRecord();
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(savedRecord);

        Optional<BorrowingRecordDTO> result = borrowingService.borrowBook(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(savedRecord.getId(), result.get().getId());
    }

    @Test
    public void returnBook_existingBorrowingRecord_shouldReturnTrue() {
        Long borrowingRecordId = 1L;
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.of(borrowingRecord));

        boolean result = borrowingService.returnBook(borrowingRecordId);

        assertTrue(result);
        assertEquals(LocalDate.now(), borrowingRecord.getReturnDate());
        verify(borrowingRecordRepository, times(1)).save(borrowingRecord);
        verifyNoMoreInteractions(borrowingRecordRepository);
    }

    @Test
    public void returnBook_nonExistingBorrowingRecord_shouldThrowException() {
        Long borrowingRecordId = 1L;
        when(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(Optional.empty());

        assertThrows(BorrowingRecordNotFoundException.class, () -> borrowingService.returnBook(borrowingRecordId));
        verifyNoMoreInteractions(borrowingRecordRepository);
    }

    @Test
    public void convertToDTO_validBorrowingRecord_shouldReturnBorrowingRecordDTO() {
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        BorrowingRecordDTO result = borrowingService.convertToDTO(borrowingRecord);

    }

    @Test
    public void convertToEntity_validBorrowingRecordDTO_shouldReturnBorrowingRecord() {
        BorrowingRecordDTO dto = new BorrowingRecordDTO();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(new Patron()));

        BorrowingRecord result = borrowingService.convertToEntity(dto);

    }

    @Test
    public void convertToEntity_invalidBookId_shouldThrowException() {
        BorrowingRecordDTO dto = new BorrowingRecordDTO();
        dto.setBookId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> borrowingService.convertToEntity(dto));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void convertToEntity_invalidPatronId_shouldThrowException() {
        BorrowingRecordDTO dto = new BorrowingRecordDTO();
        dto.setPatronId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PatronNotFoundException.class, () -> borrowingService.convertToEntity(dto));
        verifyNoMoreInteractions(bookRepository, patronRepository);
    }
}
