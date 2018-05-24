package com.basaki.example.service;

import com.basaki.example.boot.Application;
import com.basaki.example.error.DataNotFoundException;
import com.basaki.example.model.book.Author;
import com.basaki.example.model.book.Book;
import com.basaki.example.model.book.BookRequest;
import com.basaki.example.model.Genre;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * {@code BookServiceIntegrationTest} integration test class for {@link
 * BookService}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class BookServiceIntegrationTest {

    @Autowired
    private BookService service;

    @Test
    public void testCreate() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        validate(request, book);
    }

    @Test
    @Transactional
    public void testRead() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        assertNotNull(book);
        assertNotNull(book.getId());

        Book book2 = service.read(book.getId());
        assertNotNull(book2);
        assertNotNull(book2.getId());
        assertTrue(book.equals(book2));
    }

    @Test
    @Transactional
    public void testReadAll() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        assertNotNull(book);
        assertNotNull(book.getId());

        List<Book>
                books = service.readAll(book.getTitle(), book.getGenre(),
                book.getPublisher(),
                book.getAuthor().getFirstName(),
                book.getAuthor().getLastName());
        assertNotNull(books);
        assertEquals(1, books.size());
        assertNotNull(books.get(0));
        assertNotNull(books.get(0).getId());
        validate(book, books.get(0));
    }

    @Test
    @Transactional
    public void testReadAllWithNullParams() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        assertNotNull(book);
        assertNotNull(book.getId());

        List<Book>
                books = service.readAll(null, null, null, null, null);
        assertNotNull(books);
        assertEquals(1, books.size());
        assertNotNull(books.get(0));
        assertNotNull(books.get(0).getId());
        validate(book, books.get(0));
    }

    @Test(expected = DataNotFoundException.class)
    @Transactional
    public void testUnsuccessfulReadAll() {
        service.readAll("hello", null, null, null, "zero");
    }

    @Test
    @Transactional
    public void testUpdate() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        assertNotNull(book);
        assertNotNull(book.getId());

        request.setStar(5);
        service.update(book.getId(), request);
        Book book2 = service.read(book.getId());
        assertNotNull(book2);
        assertNotNull(book2.getId());
        assertEquals(book.getId(), book2.getId());
        validate(book, book2);
        assertEquals(5, book2.getStar());
    }

    //@Test(expected = DataNotFoundException.class)
    @Transactional
    public void testUnsuccessfulUpdate() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        service.update(UUID.randomUUID(), request);
    }

    @Test(expected = DataNotFoundException.class)
    @Transactional
    public void testDelete() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);
        assertNotNull(book);
        assertNotNull(book.getId());

        service.delete(book.getId());

        service.read(book.getId());
    }

    @Test(expected = DataNotFoundException.class)
    @Transactional
    public void testUnsuccessfulDelete() {
        service.delete(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testGetPublisher() {
        service.deleteAll();

        BookRequest request = getBookRequest();
        Book book = service.create(request);

        List<String> publishers = service.getPublisher(book.getPublisher());
        assertNotNull(publishers);
        assertEquals(1, publishers.size());
        assertEquals(book.getPublisher(), publishers.get(0));
    }

    private void validate(BookRequest expected, Book actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getGenre(), actual.getGenre());
        assertEquals(expected.getPublisher(), actual.getPublisher());
        assertEquals(expected.getStar(), actual.getStar());

        assertNotNull(expected.getAuthor());
        assertNotNull(actual.getAuthor());
        assertEquals(expected.getAuthor().getFirstName(),
                actual.getAuthor().getFirstName());
        assertEquals(expected.getAuthor().getLastName(),
                actual.getAuthor().getLastName());
    }

    private BookRequest getBookRequest() {
        BookRequest book = new BookRequest();
        book.setTitle("Ethan Frome");
        book.setGenre(Genre.DRAMA);
        book.setPublisher("Scribner's");
        book.setStar(5);

        Author author = new Author();
        author.setFirstName("Edith");
        author.setLastName("Wharton");
        book.setAuthor(author);

        return book;
    }
}
