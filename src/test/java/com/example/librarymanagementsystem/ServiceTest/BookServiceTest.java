package com.example.librarymanagementsystem.ServiceTest;

import com.example.librarymanagementsystem.DTO.BookDTO;
import com.example.librarymanagementsystem.Entity.Book;
import com.example.librarymanagementsystem.Exception.BookNotFoundException;
import com.example.librarymanagementsystem.Repository.BookRepository;
import com.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void findAll_noBooksInDatabase_shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        List<BookDTO> result = bookService.findAll();

        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void findAll_booksInDatabase_shouldReturnList() {
        List<Book> booksInDatabase = new ArrayList<>();
        booksInDatabase.add(new Book(1L, "Book1", "Author1", 2022, "ISBN1"));
        booksInDatabase.add(new Book(2L, "Book2", "Author2", 2021, "ISBN2"));
        when(bookRepository.findAll()).thenReturn(booksInDatabase);

        List<BookDTO> result = bookService.findAll();

        assertEquals(booksInDatabase.size(), result.size());
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void findById_existingBook_shouldReturnBookDTO() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Book1", "Author1", 2022, "ISBN1");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<BookDTO> result = bookService.findById(bookId);

        assertTrue(result.isPresent());
        assertEquals(book.getId(), result.get().getId());
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void findById_nonExistingBook_shouldThrowException() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void save_validBookDTO_shouldReturnBookDTO() {
        BookDTO bookDTO = new BookDTO(null, "New Book", "New Author", 2023, "New ISBN");
        Book savedBook = new Book(1L, "New Book", "New Author", 2023, "New ISBN");
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDTO result = bookService.save(bookDTO);

        assertNotNull(result.getId());
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getPublicationYear(), result.getPublicationYear());
        assertEquals(bookDTO.getIsbn(), result.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void update_existingBook_shouldReturnUpdatedBookDTO() {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(bookId, "Updated Book", "Updated Author", 2023, "Updated ISBN");
        Book existingBook = new Book(bookId, "Original Book", "Original Author", 2022, "Original ISBN");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Optional<BookDTO> result = bookService.update(bookId, updatedBookDTO);

        assertTrue(result.isPresent());
        assertEquals(updatedBookDTO.getId(), result.get().getId());
        assertEquals(updatedBookDTO.getTitle(), result.get().getTitle());
        assertEquals(updatedBookDTO.getAuthor(), result.get().getAuthor());
        assertEquals(updatedBookDTO.getPublicationYear(), result.get().getPublicationYear());
        assertEquals(updatedBookDTO.getIsbn(), result.get().getIsbn());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void update_nonExistingBook_shouldThrowException() {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(bookId, "Updated Book", "Updated Author", 2023, "Updated ISBN");
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.update(bookId, updatedBookDTO));
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void delete_existingBook_shouldReturnTrue() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        boolean result = bookService.delete(bookId);

        assertTrue(result);
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void delete_nonExistingBook_shouldThrowException() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete(bookId));
        verify(bookRepository, times(1)).existsById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

}
