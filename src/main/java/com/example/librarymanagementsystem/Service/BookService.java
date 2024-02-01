package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.DTO.BookDTO;
import com.example.librarymanagementsystem.Entity.Book;
import com.example.librarymanagementsystem.Exception.BookNotFoundException;
import com.example.librarymanagementsystem.Repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Cacheable("books")
    public List<BookDTO> findAll() {
        log.info("Fetching all books");
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#id")
    public Optional<BookDTO> findById(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    @CachePut(value = "books", key = "#result.id")
    public BookDTO save(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        log.info("Saving new book: {}", bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public Optional<BookDTO> update(Long id, BookDTO bookDTO) {
        log.info("Updating book with id: {}", id);
        return Optional.ofNullable(bookRepository.findById(id).map(book -> {
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            book.setPublicationYear(bookDTO.getPublicationYear());
            book.setIsbn(bookDTO.getIsbn());
            return convertToDTO(bookRepository.save(book));
        }).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id)));
    }

    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public boolean delete(Long id) {
        log.info("Deleting book with id: {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getIsbn());
    }

    private Book convertToEntity(BookDTO bookDTO) {
        return new Book(bookDTO.getId(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublicationYear(), bookDTO.getIsbn());
    }
}
