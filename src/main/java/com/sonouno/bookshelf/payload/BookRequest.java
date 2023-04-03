package com.sonouno.bookshelf.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
public class BookRequest {
	final String title;
	final Integer pages;
	final Optional<String> description;
	final Optional<String> author;
	final Optional<String> type;
}
