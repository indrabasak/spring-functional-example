package com.basaki.example.config;

import com.basaki.example.handler.BookHandler;
import com.basaki.example.handler.MovieHandler;
import com.basaki.example.util.UuidBeanFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.util.UUID;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

/**
 * Created by indra.basak on 3/8/17.
 */
@Configuration
public class SpringConfiguration {

    @Bean
    public static Mapper getMapper() {
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(UUID.class, UUID.class, TypeMappingOptions.oneWay(),
                        TypeMappingOptions.beanFactory(
                                UuidBeanFactory.class.getName()));
            }
        };

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(builder);

        return mapper;
    }

    @Primary
    @Bean
    public ObjectMapper createCustomObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JodaModule());

        return mapper;
    }

    //@Bean
    public ServletRegistrationBean registerBookServlet(BookHandler handler) {
        HttpHandler httpHandler = WebHttpHandlerBuilder
                .webHandler(toHttpHandler(handler.getRoutes()))
                .build();

        ServletHttpHandlerAdapter servlet =
                new ServletHttpHandlerAdapter(httpHandler);

        ServletRegistrationBean registrationBean =
                new ServletRegistrationBean<>(servlet, "/booxs");
        registrationBean.setName("booxs");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);

        return registrationBean;
    }

    @Bean
    public ServletRegistrationBean registerMoviewServlet(MovieHandler handler)  {
        HttpHandler httpHandler = WebHttpHandlerBuilder
                .webHandler(toHttpHandler(handler.getRoutes()))
                .build();

        ServletHttpHandlerAdapter servlet =
                new ServletHttpHandlerAdapter(httpHandler);

        ServletRegistrationBean registrationBean =
                new ServletRegistrationBean<>(servlet, "/");
        registrationBean.setName("movies");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);

        return registrationBean;
    }

    private RouterFunction<ServerResponse> routingFunctionOld() {
        HandlerFunction<ServerResponse> ping =
                request -> ServerResponse.ok().body(fromObject("pong"));

        return route(GET("/ping"), ping).filter((request, next) -> {
            System.out.println(
                    "Before handler invocation: " + request.path());
            return next.handle(request);
        });
    }
}
