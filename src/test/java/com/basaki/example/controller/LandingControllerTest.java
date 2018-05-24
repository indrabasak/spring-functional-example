package com.basaki.example.controller;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;

/**
 * {@code LandingControllerTest} unit test class for {@link LandingController}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
public class LandingControllerTest {

    @InjectMocks
    private LandingController controller;

    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        response = new MockHttpServletResponse();
    }

    @Test
    public void testHome() throws IOException {
        controller.home(response);
        assertEquals("/swagger-ui.html", response.getRedirectedUrl());
    }
}
