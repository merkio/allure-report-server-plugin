package io.qameta.allure.server.feign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.qameta.allure.util.PropertyUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FeignClientBuilder {

    private static final String REPORT_SERVER_ENDPOINT = "ALLURE_REPORT_SERVER_ENDPOINT";

    private String endpoint;
    private RequestInterceptor requestInterceptor;

    public FeignClientBuilder endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public FeignClientBuilder requestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    public FeignClientBuilder defaults() {
        endpoint = PropertyUtils.requireProperty(REPORT_SERVER_ENDPOINT);
        return this;
    }

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new Jdk8Module())
        .registerModule(newJavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public <T> T createClient(Class<T> type) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new FormEncoder(new JacksonEncoder(OBJECT_MAPPER)))
            .decoder(new JacksonDecoder(OBJECT_MAPPER))
            .logger(new Slf4jLogger())
            .logLevel(Logger.Level.FULL)
            .target(type, endpoint);
    }

    public <T> T createClient(Class<T> type, RequestInterceptor requestInterceptor) {
        return Feign.builder()
            .client(new OkHttpClient())
            .encoder(new FormEncoder(new JacksonEncoder(OBJECT_MAPPER)))
            .decoder(new JacksonDecoder(OBJECT_MAPPER))
            .logger(new Slf4jLogger())
            .requestInterceptor(requestInterceptor)
            .logLevel(Logger.Level.FULL)
            .target(type, endpoint);
    }


    private JavaTimeModule newJavaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return module;
    }
}
