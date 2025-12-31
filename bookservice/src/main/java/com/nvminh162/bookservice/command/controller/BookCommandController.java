package com.nvminh162.bookservice.command.controller;

import com.nvminh162.bookservice.command.command.CreateBookCommand;
import com.nvminh162.bookservice.command.command.DeleteBookCommand;
import com.nvminh162.bookservice.command.command.UpdateBookCommand;
import com.nvminh162.bookservice.command.model.BookRequestModel;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookCommandController {
    CommandGateway commandGateway; // Phát đi những Event

    @PostMapping
    public String createBook(@Valid @RequestBody BookRequestModel model) {
        CreateBookCommand command = new CreateBookCommand(UUID.randomUUID().toString(), model.getName(), model.getAuthor(), true);
        return commandGateway.sendAndWait(command); // aggregate identifier
    }

    @PutMapping("/{bookId}")
    public String updateBook(@RequestBody BookRequestModel model, @PathVariable String bookId) {
        UpdateBookCommand command = new UpdateBookCommand(bookId, model.getName(), model.getAuthor(), model.getIsReady());
        return commandGateway.sendAndWait(command); // aggregate identifier
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable String bookId) {
        DeleteBookCommand command = new DeleteBookCommand(bookId);
        return commandGateway.sendAndWait(command); // aggregate identifier
    }
}
