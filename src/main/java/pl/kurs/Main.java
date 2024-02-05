package pl.kurs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


        //Car: id|brand|model|fuelType
        //Garage: id|places|address|lpgAllowed
        // do tego kontrollery i metody pelny CRUD

//        List<Integer> numbers = Collections.synchronizedList(new ArrayList<>());
//
//        MyThread mt1 = new MyThread(numbers, 1);
//        MyThread mt2 = new MyThread(numbers, 2);
//        MyThread mt3 = new MyThread(numbers, 3);
//        MyThread mt4 = new MyThread(numbers, 4);
//
//        mt1.start();
//        mt2.start();
//        mt3.start();
//        mt4.start();
//
//        mt1.join();
//        mt2.join();
//        mt3.join();
//        mt4.join();
//
//        System.out.println("Ilosc elementow: " + numbers.size());
//        System.out.println("Ilosc 1: " + numbers.stream().filter(i -> i == 1).count());
//        System.out.println("Ilosc 2: " + numbers.stream().filter(i -> i == 2).count());
//        System.out.println("Ilosc 3: " + numbers.stream().filter(i -> i == 3).count());
//        System.out.println("Ilosc 4: " + numbers.stream().filter(i -> i == 4).count());
//    }
//
//    @RequiredArgsConstructor
//    static class MyThread extends Thread {
//        private final List<Integer> numbers;
//        private final int number;
//
//        @Override
//        public void run() {
//            for (int i = 0; i < 1000; i++) {
//                numbers.add(number);
//            }
//        }

        // wykonaj test wykorzystaj 10 watkow i kazdy watek dodaje 1000 roznych ksiazek
        // wypisz ile jest wszystkich ksiazek: ArrayListy/SynchronizedList

//        List<Thread> threads = new ArrayList<>();
//        List<Book> books = Collections.synchronizedList(new ArrayList<>());
//        AtomicInteger idGenerator = new AtomicInteger();


//        for (int i = 0; i < 10; i++) {
//            Thread thread = new Thread(() -> {
//                IntStream.rangeClosed(1, 1000).forEach(j -> {
//                    int id = idGenerator.incrementAndGet();
//                    Book book = new Book(id, "Book " + id, "Author " + id, true);
//                    books.add(book);
//                });
//            });
//            threads.add(thread);
//            thread.start();
//        }
//
//        for (Thread thread : threads) {
//            thread.join();
//        }
//        System.out.println("Total number of books " + books.size());

    }
}
