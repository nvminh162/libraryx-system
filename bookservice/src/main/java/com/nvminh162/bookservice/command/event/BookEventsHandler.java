package com.nvminh162.bookservice.command.event;

import com.nvminh162.bookservice.command.data.Book;
import com.nvminh162.bookservice.command.data.BookRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // ứng dụng quét tới class này để lắng nghe (Scan class được đánh dấu)
@RequiredArgsConstructor
public class BookEventsHandler {
    private final BookRepository bookRepository;

    @EventHandler // tại đây mới thực sự xử lý event đó
    public void on(BookCreatedEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event, book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdatedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        if (oldBook.isPresent()) {
            Book book = oldBook.get();
            book.setAuthor(event.getAuthor());
            book.setName(event.getName());
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        }
    }

    @EventHandler
    public void on(BookDeletedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());
        oldBook.ifPresent(book -> bookRepository.deleteById(book.getId()));
    }
}
