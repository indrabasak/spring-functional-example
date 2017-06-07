package com.basaki.example.book.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@code BookApplicationIntegration} integration test class for {@link
 * BookApplication}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookApplication.class)
@ActiveProfiles("test")
public class BookApplicationIntegrationTest {

    @Test
    public void testLoadingContext() {
    }
}
