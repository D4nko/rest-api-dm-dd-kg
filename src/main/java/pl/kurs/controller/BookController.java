package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.BookIdGenerator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
@RequiredArgsConstructor
//@Scope("singleton")// jedna instancja dla calej aplikacji
//@Scope("request")// nowa instancja do kjazdego zadania
@Scope("session")// kazdy nowy klient bedzie mial nowa instancje kontrollera

public class BookController {


    private final List<Book> books;

    private final BookIdGenerator bookIdGenerator;

    private int counter = 0;
    @PostConstruct
    public void init() {
//        books.add(new Book(generator.incrementAndGet(), "W pustyni i w puszczy", "LEKTURA", true));
//        books.add(new Book(generator.incrementAndGet(), "Krzyżacy", "LEKTURA", true));
    }

    @GetMapping("/test")
    public void test() {
        log.info("counter:{}", counter++);
    }


    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody CreateBookCommand command) {
        Book book = new Book(bookIdGenerator.getId(), command.getTitle(), command.getCategory(), true);
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBook(@PathVariable int id) {
        return ResponseEntity.ok(books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id) {
        if(books.removeIf(b -> b.getId() == id)){
            throw new BookNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> editBook(@PathVariable int id, @RequestBody EditBookCommand command) {
       Book book = books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new);
       book.setCategory(command.getCategory());
       book.setTitle(command.getTitle());
       book.setAvailable(command.getAvailable());
       return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> editBookPartially(@PathVariable int id, @RequestBody EditBookCommand command) {
        Book book = books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }


}
