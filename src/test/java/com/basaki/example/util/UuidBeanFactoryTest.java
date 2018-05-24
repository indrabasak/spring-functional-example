package com.basaki.example.util;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * {@code UuidBeanFactoryTest} unit test class for {@link UuidBeanFactory}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@ActiveProfiles("test")
public class UuidBeanFactoryTest {

    private UuidBeanFactory factory;

    @Before
    public void setUp() {
        factory = new UuidBeanFactory();
    }

    @Test
    public void testCreateBean() {
        UUID expected = UUID.randomUUID();
        Object actual = factory.createBean(expected, UUID.class, null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateBeanForNull() {
        Object actual = factory.createBean(null, UUID.class, null);
        assertNull(actual);
    }
}
