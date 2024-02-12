package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kurs.Main;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
// w mainie na razie mamy jedna konfiguracjhe, skanowanie i auokonfiguracja, testy beda uruchamialy caly kontekst springowuy, nie sa to testy jednostkowe
@AutoConfigureMockMvc // zebysmy z kontenera springowego mogli pozyskac obiekty, taki programistyczny postman
@ActiveProfiles("tests")
class BookControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @Test
    void shouldReturnSingleBook() throws Exception {
        Author author = authorRepository.findAllWithBooks().get(0);
        int id = bookRepository.saveAndFlush(new Book("Testowa", "TESTOWA", true, author)).getId();
        postman.perform(MockMvcRequestBuilders.get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Testowa"))
                .andExpect(jsonPath("$.category").value("TESTOWA"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void shouldAddBook() throws Exception {
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("podstawy java", "NAUKOWA", author.getId());
        String json = objectMapper.writeValueAsString(command);


        String responseJson = postman.perform(MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("podstawy java"))
                .andExpect(jsonPath("$.category").value("NAUKOWA"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseJson, Book.class);

        Book recentlyAdded = bookRepository.findById(saved.getId()).get();

        Assertions.assertEquals("podstawy java", recentlyAdded.getTitle());
        Assertions.assertEquals("NAUKOWA", recentlyAdded.getCategory());
        Assertions.assertTrue(recentlyAdded.isAvailable());
    }
}