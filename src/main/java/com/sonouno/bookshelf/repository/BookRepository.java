package com.sonouno.bookshelf.repository;

import com.sonouno.bookshelf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	Optional<List<Book>> findByUserId(Long userId);

	@Modifying
	@Query("UPDATE Book e SET e.visible = false WHERE e.id = ?1")
	void softDelete(Long bookId);
}
