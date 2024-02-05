package pl.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.implementation.bytecode.ShiftRight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.Main;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.service.BookIdGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
// w mainie na razie mamy jedna konfiguracjhe, skanowanie i auokonfiguracja, testy beda uruchamialy caly kontekst springowuy, nie sa to testy jednostkowe
@AutoConfigureMockMvc // zebysmy z kontenera springowego mogli pozyskac obiekty, taki programistyczny postman
class BookControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private List<Book> books;

    @SpyBean
    private BookIdGenerator bookIdGenerator;

    @Test
    void shouldReturnSingleBook() throws Exception {
        postman.perform(MockMvcRequestBuilders.get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("W pustyni i w puszczy"))
                .andExpect(jsonPath("$.category").value("LEKTURA"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void shouldAddBook() throws Exception {

        CreateBookCommand command = new CreateBookCommand("podstawy java", "NAUKOWA");
        String json = objectMapper.writeValueAsString(command);


        postman.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("podstawy java"))
                .andExpect(jsonPath("$.category").value("NAUKOWA"))
                .andExpect(jsonPath("$.available").value(true));

        Book recentyAdded = books.get(books.size() - 1);
        Assertions.assertEquals("podstawy java", recentyAdded.getTitle());
        Assertions.assertEquals("NAUKOWA", recentyAdded.getCategory());
        Assertions.assertTrue(recentyAdded.isAvailable());

        Mockito.verify(bookIdGenerator, Mockito.times(1)).getId();


    }
}