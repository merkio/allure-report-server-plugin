package io.qameta.allure.server.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public interface TestAttachmentApi {

    @ResponseStatus(CREATED)
    @RequestLine("POST /api/report/attachment/{runId}/upload")
    @Headers({"Content-Type: multipart/form-data"})
    void uploadAttachment(@Param("file") File file,
                            @Param("runId") String runId);

    @ResponseStatus(NO_CONTENT)
    @RequestLine("DELETE /api/report/attachment/{runId}/{fileName}")
    void deleteAttachment(@Param("runId") String runId, @Param("fileName") String fileName);
}
