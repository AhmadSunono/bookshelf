package com.sonouno.bookshelf.service;

import com.sonouno.bookshelf.entity.BookStatus;
import com.sonouno.bookshelf.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookStatusService {

    @Autowired
    private BookStatusRepository bookStatusRepository;

    public BookStatus getBookStatusById(Long id) {
        return bookStatusRepository.findByBookId(id).orElse(null);
    }


}
