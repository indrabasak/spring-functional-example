package com.basaki.example.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {@code BookApplicationIntegration} integration test class for {@link
 * Application}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ApplicationIntegrationTest {

    @Test
    public void testLoadingContext() {
    }
}
