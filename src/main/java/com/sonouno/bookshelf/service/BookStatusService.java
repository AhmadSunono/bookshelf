package com.sonouno.bookshelf.service;

import com.sonouno.bookshelf.entity.Book;
import com.sonouno.bookshelf.entity.BookStatus;
import com.sonouno.bookshelf.entity.User;
import com.sonouno.bookshelf.enums.EBookStatus;
import com.sonouno.bookshelf.exceptions.ResourceForbiddenException;
import com.sonouno.bookshelf.exceptions.ResourceNotFoundException;
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

        BookStatus updatedBookStatus = BookStatus.builder()
                .id(bookId)
                .book(book)
                .readPages(bookStatusUpdateRequest.getReadPages().orElse(bookStatus.getReadPages()))
                .status(bookStatusUpdateRequest.getStatus().orElse(bookStatus.getStatus()))
                .notes(bookStatusUpdateRequest.getNotes().orElse(bookStatus.getNotes()))
                .build();

        if (bookStatusUpdateRequest.getStarted().orElse(false)
                || (bookStatus.getStartedAt() == null) && bookStatusUpdateRequest.getReadPages().isPresent()) {
            updatedBookStatus.setStartedAt(LocalDateTime.now());
        }

        if (bookStatusUpdateRequest.getCompleted().orElse(false)
                || (bookStatus.getCompletedAt() == null) && bookStatusUpdateRequest.getReadPages().orElse(0) >= bookStatus.getBook().getPages()) {
            updatedBookStatus.setCompletedAt(LocalDateTime.now());
        }

        if (bookStatusUpdateRequest.getReadPages().orElse(0) >= bookStatus.getBook().getPages()) {
            updatedBookStatus.setStatus(EBookStatus.COMPLETED);
        }

        if (bookStatus.getStatus() == EBookStatus.COMPLETED && bookStatusUpdateRequest.getReadPages().orElse(0) < bookStatus.getBook().getPages()) {
            updatedBookStatus.setStatus(EBookStatus.READING);
        }

        if (bookStatus.getStatus() == EBookStatus.READING && bookStatusUpdateRequest.getReadPages().orElse(0) == 0) {
            updatedBookStatus.setStatus(EBookStatus.BACKLOG);
        }

        return bookStatusRepository.save(updatedBookStatus);

    }
}
