package com.example.librarymanagementsystem.ControllerTest;

import com.example.librarymanagementsystem.Controller.BookController;
import com.example.librarymanagementsystem.DTO.BookDTO;
import com.example.librarymanagementsystem.Service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBooks_shouldReturnListOfBooks() throws Exception {
        when(bookService.findAll()).thenReturn(Collections.singletonList(new BookDTO()));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(bookService, times(1)).findAll();
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getBookById_existingId_shouldReturnBook() throws Exception {
        Long bookId = 1L;
        when(bookService.findById(bookId)).thenReturn(java.util.Optional.of(new BookDTO()));

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(bookService, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void getBookById_nonExistingId_shouldReturnNotFound() throws Exception {
        Long bookId = 1L;
        when(bookService.findById(bookId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void addBook_validBook_shouldReturnCreated() throws Exception {
        BookDTO bookDTO = new BookDTO();

        when(bookService.save(any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(bookService, times(1)).save(any(BookDTO.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void updateBook_existingId_shouldReturnUpdatedBook() throws Exception {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();

        when(bookService.update(eq(bookId), any(BookDTO.class))).thenReturn(java.util.Optional.of(bookDTO));

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty());

        verify(bookService, times(1)).update(eq(bookId), any(BookDTO.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void updateBook_nonExistingId_shouldReturnNotFound() throws Exception {
        Long bookId = 1L;

        when(bookService.update(eq(bookId), any(BookDTO.class))).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).update(eq(bookId), any(BookDTO.class));
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deleteBook_existingId_shouldReturnOk() throws Exception {
        Long bookId = 1L;

        when(bookService.delete(bookId)).thenReturn(true);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isOk());

        verify(bookService, times(1)).delete(bookId);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    public void deleteBook_nonExistingId_shouldReturnNotFound() throws Exception {
        Long bookId = 1L;

        when(bookService.delete(bookId)).thenReturn(false);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).delete(bookId);
        verifyNoMoreInteractions(bookService);
    }
}
