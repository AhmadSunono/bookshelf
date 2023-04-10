package com.sonouno.bookshelf.payload;

import com.sonouno.bookshelf.enums.EBookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class BookStatusUpdateRequest {
    final Optional<Integer> readPages;
    final Optional<EBookStatus> status;
    final Optional<String> notes;
    final Optional<Boolean> started;
    final Optional<Boolean> completed;
}
