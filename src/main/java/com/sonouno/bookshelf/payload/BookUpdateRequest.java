package com.sonouno.bookshelf.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class BookUpdateRequest {
	final Optional<String> title;
	final Optional<Integer> pages;
	final Optional<String> description;
	final Optional<String> author;
	final Optional<String> type;
}

