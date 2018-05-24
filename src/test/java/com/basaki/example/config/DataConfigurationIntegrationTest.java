package com.basaki.example.config;

import com.basaki.example.boot.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * {@code DataConfigurationTest} is the unit test for {@link DataConfiguration}
 * <p/>
 *
 * @author Indra Basak
 * @since 4/11/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class DataConfigurationIntegrationTest {

    @Autowired
    private LocalContainerEntityManagerFactoryBean factory;

    @Test
    public void testJpaProperties() {
        assertEquals("none",
                factory.getJpaPropertyMap().get("hibernate.hbm2ddl.auto"));
        assertEquals(true,
                factory.getJpaPropertyMap().get("hibernate.show_sql"));
        assertEquals(true,
                factory.getJpaPropertyMap().get("hibernate.format_sql"));
        assertEquals("AUTO",
                factory.getJpaPropertyMap().get("org.hibernate.flushMode"));
    }
}
