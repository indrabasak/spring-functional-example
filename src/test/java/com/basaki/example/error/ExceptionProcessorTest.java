package com.basaki.example.error;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

/**
 * {@code ExceptionProcessorTest} unit test class for {@link
 * ExceptionProcessor}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@ActiveProfiles("test")
public class ExceptionProcessorTest {

    private ExceptionProcessor processor;
    private MockHttpServletRequest request;
    private String message;
    private String path;

    @Before
    public void setUp() throws Exception {
        processor = new ExceptionProcessor();
        request = new MockHttpServletRequest();
        message = "error message";
        path = "/some/path";
        request.setRequestURI(path);
        request.setPathInfo(path);
    }

    @Test
    public void testHandleDataNotFoundException() {
        DataNotFoundException exception =
                new DataNotFoundException(message);
        ErrorInfo info =
                processor.handleDataNotFoundException(request, exception);
        validate(info, HttpStatus.NOT_FOUND, message);
    }

    @Test
    public void testHandleIllegalArgException() {
        IllegalArgumentException exception =
                new IllegalArgumentException(message);
        ErrorInfo info =
                processor.handleIllegalArgException(request, exception);
        validate(info, HttpStatus.BAD_REQUEST, message);
    }

    @Test
    public void testHandleIllegalStateException() {
        IllegalStateException exception =
                new IllegalStateException(message);
        ErrorInfo info =
                processor.handleIllegalStateException(request, exception);
        validate(info, HttpStatus.BAD_REQUEST, message);
    }

    private void validate(ErrorInfo info, HttpStatus status, String msg) {
        assertEquals(msg, info.getMessage());
        assertEquals(status.value(), info.getCode());
        assertEquals(status.getReasonPhrase(), info.getType());
        assertEquals(path, info.getPath());
    }
}
