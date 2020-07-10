package io.qameta.allure.server.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.qameta.allure.server.dto.TestRunDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface TestRunApi {

    @ResponseStatus(HttpStatus.CREATED)
    @RequestLine("POST /api/report/test-run")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    TestRunDTO createTestRun(TestRunDTO testRunDTO);

    @ResponseStatus(HttpStatus.OK)
    @RequestLine("GET /api/report/test-run/{id}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    TestRunDTO getTestRun(@Param("id") String id);

    @ResponseStatus(HttpStatus.OK)
    @RequestLine("PUT /api/report/test-run/{id}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    TestRunDTO updateTestRun(@Param("id") String id, TestRunDTO testRunDTO);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestLine("DELETE /api/report/test-run/{id}")
    void deleteTestRun(@Param("id") String id);

    @ResponseStatus(HttpStatus.OK)
    @RequestLine("GET /api/report/test-run/all")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    List<TestRunDTO> getAll();
}
