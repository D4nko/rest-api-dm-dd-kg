package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.Book;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init() {
        books.add(new Book(1, "W pustyni i w puszczy", "Henryk Sienkiewicz", true));
        books.add(new Book(2, "Krzyżacy", "Henryk Sienkiewicz", true));
        books.add(new Book(3, "Pan Wołodyjowski", "Henryk Sienkiewicz", true));
        books.add(new Book(4, "Potop", "Henryk Sienkiewicz", true));
        books.add(new Book(5, "Quo vadis", "Henryk Sienkiewicz", true));
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

}
