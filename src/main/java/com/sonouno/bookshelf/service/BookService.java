package com.sonouno.bookshelf.service;

import com.sonouno.bookshelf.entity.Book;
import com.sonouno.bookshelf.entity.BookStatus;
import com.sonouno.bookshelf.entity.User;
import com.sonouno.bookshelf.exceptions.ResourceForbiddenException;
import com.sonouno.bookshelf.exceptions.ResourceNotFoundException;
import com.sonouno.bookshelf.payload.BookRequest;
import com.sonouno.bookshelf.payload.BookUpdateRequest;
import com.sonouno.bookshelf.repository.BookRepository;
import com.sonouno.bookshelf.repository.BookStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
	@Autowired
	private BookStatusRepository bookStatusRepository;

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByUser(Long userId) {
        return bookRepository.findByUserId(userId).orElse(Collections.emptyList());
    }

    public Book createBook(BookRequest bookRequest, User user) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .pages(bookRequest.getPages())
                .author(bookRequest.getAuthor().orElse(null))
                .description(bookRequest.getDescription().orElse(null))
                .type(bookRequest.getType().orElse(null))
                .user(user)
                .build();

        BookStatus bookStatus = BookStatus.builder()
                .book(book)
                .build();

        bookRepository.save(book);
		bookStatusRepository.save(bookStatus);
        return book;
    }

    public Book updateBook(Long bookId, BookUpdateRequest bookUpdateRequest, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        if (!book.getUser().getId().equals(user.getId())) {
            throw new ResourceForbiddenException("Book", "user", user.getId());
        }

        Book updatedBook = Book.builder()
                .id(bookId)
                .title(bookUpdateRequest.getTitle().orElse(book.getTitle()))
                .pages(bookUpdateRequest.getPages().orElse(book.getPages()))
                .description(bookUpdateRequest.getDescription().orElse(book.getDescription()))
                .author(bookUpdateRequest.getAuthor().orElse(book.getAuthor()))
                .type(bookUpdateRequest.getType().orElse(book.getType()))
                .user(book.getUser())
                .build();

        return bookRepository.save(updatedBook);
    }

    @Transactional
    public void deleteBook(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        if (!book.getUser().getId().equals(userId)) {
            throw new ResourceForbiddenException("Book", "user", userId);
        }

        bookRepository.softDelete(bookId);
    }

}
