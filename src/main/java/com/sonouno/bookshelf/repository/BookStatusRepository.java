package com.sonouno.bookshelf.repository;

import com.sonouno.bookshelf.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    Optional<BookStatus> findByBookId(Long bookId);

    @Query("SELECT status FROM BookStatus status INNER JOIN status.book book WHERE book.user.id = ?1")
    Optional<List<BookStatus>> findBookStatusesByUserId(Long userId);
}
