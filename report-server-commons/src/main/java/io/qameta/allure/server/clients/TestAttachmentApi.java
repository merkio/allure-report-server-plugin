package io.qameta.allure.server.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public interface TestAttachmentApi { ;

    @ResponseStatus(CREATED)
    @RequestLine("POST /report/attachment/{folderName}/{fileName}")
    @Headers({"Content-Type: multipart/form-data",
        "Accept: application/json"})
    String uploadAttachment(@Param(value = "file") File file,
                            @Param("folderName") String folderName,
                            @Param("fileName") String fileName);

    @ResponseStatus(CREATED)
    @RequestLine("POST /report/attachment/{folderName}")
    @Headers({"Content-Type: multipart/form-data",
        "Accept: application/json"})
    String uploadAttachment(@Param("file") File file,
                            @Param("folderName") String folderName);

    @ResponseStatus(NO_CONTENT)
    @RequestLine("DELETE /report/attachment/{runId}/{fileName}")
    void deleteAttachment(@Param("runId") String runId, @Param("fileName") String fileName);
}
