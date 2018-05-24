package com.basaki.example.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.Assert.assertEquals;

/**
 * {@code SwaggerConfigurationTest} unit test class for {@link
 * SwaggerConfiguration}.
 * <p/>
 *
 * @author Indra Basak
 * @since 6/3/17
 */
@ActiveProfiles("test")
public class SwaggerConfigurationTest {

    private SwaggerConfiguration config;

    @Before
    public void startUp() {
        config = new SwaggerConfiguration();
    }

    @Test
    public void testApi() {
        Docket docket = config.api();
        assertEquals("book", docket.getGroupName());
        assertEquals(DocumentationType.SWAGGER_2,
                docket.getDocumentationType());
    }
}
