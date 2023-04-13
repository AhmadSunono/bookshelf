package com.sonouno.bookshelf.service;

import com.sonouno.bookshelf.entity.Book;
import com.sonouno.bookshelf.entity.BookStatus;
import com.sonouno.bookshelf.entity.User;
import com.sonouno.bookshelf.enums.EBookStatus;
import com.sonouno.bookshelf.exception.ResourceForbiddenException;
import com.sonouno.bookshelf.exception.ResourceNotFoundException;
import com.sonouno.bookshelf.payload.BookStatusUpdateRequest;
import com.sonouno.bookshelf.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class BookStatusService {

    @Autowired
    private BookStatusRepository bookStatusRepository;

    public BookStatus getBookStatusById(Long id) {
        return bookStatusRepository.findByBookId(id).orElse(null);
    }

    public List<BookStatus> getBookStatusesByUserId(Long id) {
        return bookStatusRepository.findBookStatusesByUserId(id).orElse(Collections.emptyList());
    }

    public BookStatus updateBookStatus(Long bookId, BookStatusUpdateRequest bookStatusUpdateRequest, User user) {
        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Status", "id", bookId));

        Book book = bookStatus.getBook();

        if (!bookStatus.getBook().getUser().getId().equals(user.getId())) {
            throw new ResourceForbiddenException("Book Status", "user", user.getId());
        }

        // Determine the new status based on the current status and request parameters
        EBookStatus newStatus;
        Integer readPages = bookStatusUpdateRequest.getReadPages().orElse(bookStatus.getReadPages());
        Integer totalPages = book.getPages();

        if (bookStatusUpdateRequest.getStatus().isPresent()) {
            newStatus = bookStatusUpdateRequest.getStatus().orElse(bookStatus.getStatus());
        } else if (bookStatusUpdateRequest.getReadPages().isPresent()) {
            if (readPages == 0) {
                newStatus = EBookStatus.BACKLOG;
            } else if (readPages >= totalPages) {
                newStatus = EBookStatus.COMPLETED;
            } else {
                newStatus = EBookStatus.READING;
            }
        } else {
            newStatus = bookStatus.getStatus();
        }

        // Build the updated book status
        BookStatus updatedBookStatus = BookStatus.builder()
                .id(bookId)
                .book(book)
                .readPages(bookStatusUpdateRequest.getReadPages().orElse(bookStatus.getReadPages()))
                .status(newStatus)
                .notes(bookStatusUpdateRequest.getNotes().orElse(bookStatus.getNotes()))
                .startedAt(bookStatus.getStartedAt())
                .completedAt(bookStatus.getCompletedAt())
                .build();

        // Set the started time if necessary
        if (bookStatusUpdateRequest.getStarted().orElse(false) || (bookStatus.getStartedAt() == null && bookStatusUpdateRequest.getReadPages().orElse(0) > 0)) {
            updatedBookStatus.setStartedAt(LocalDateTime.now());
        }

        // Set the completed time and read pages if necessary
        if (bookStatusUpdateRequest.getCompleted().orElse(false) || (bookStatus.getCompletedAt() == null && bookStatusUpdateRequest.getReadPages().orElse(0) >= bookStatus.getBook().getPages())) {
            updatedBookStatus.setCompletedAt(LocalDateTime.now());
            updatedBookStatus.setReadPages(bookStatus.getBook().getPages());
        }

        return bookStatusRepository.save(updatedBookStatus);

    }
}
