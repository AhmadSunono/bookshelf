package com.sonouno.bookshelf.controller;

import com.sonouno.bookshelf.entity.Book;
import com.sonouno.bookshelf.entity.User;
import com.sonouno.bookshelf.payload.BookRequest;
import com.sonouno.bookshelf.payload.BookUpdateRequest;
import com.sonouno.bookshelf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping()
	public ResponseEntity<List<Book>> getBooksByUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();

		List<Book> books = bookService.getBooksByUser(userDetails.getId());
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable Long id) {
		Book book = bookService.getBookById(id);
		if (book == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<Book> createBook(@RequestBody BookRequest payload) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();

		Book book = bookService.createBook(payload, userDetails);
		return new ResponseEntity<>(book, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(
					@PathVariable Long id,
					@RequestBody BookUpdateRequest payload) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();

		Book book = bookService.updateBook(id, payload, userDetails);

		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) authentication.getPrincipal();

		bookService.deleteBook(id, userDetails.getId());

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
