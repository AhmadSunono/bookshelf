package com.sonouno.bookshelf.controller;

import com.sonouno.bookshelf.entity.BookStatus;
import com.sonouno.bookshelf.entity.User;
import com.sonouno.bookshelf.payload.BookStatusUpdateRequest;
import com.sonouno.bookshelf.service.BookStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book/status")
public class BookStatusController {

    @Autowired
    private BookStatusService bookStatusService;

    @GetMapping("/{id}")
    public ResponseEntity<BookStatus> getBookStatus(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();

        BookStatus bookStatus = bookStatusService.getBookStatusById(id);

        if(bookStatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if(bookStatus.getBook().getUser().getId() != userDetails.getId()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(bookStatus, HttpStatus.OK);
        }
    }

    @GetMapping()
    public ResponseEntity<List<BookStatus>> getBookStatusesByUsed() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();

        List<BookStatus> statuses = bookStatusService.getBookStatusesByUserId(userDetails.getId());

        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookStatus> updateBookStatus(
            @PathVariable Long id,
            @RequestBody BookStatusUpdateRequest bookStatusUpdateRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();

        BookStatus bookStatus = bookStatusService.updateBookStatus(id, bookStatusUpdateRequest, userDetails);

        return new ResponseEntity<>(bookStatus, HttpStatus.OK);

    }
}
