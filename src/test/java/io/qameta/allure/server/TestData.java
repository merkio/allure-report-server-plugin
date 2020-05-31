package io.qameta.allure.server;

import io.space.geek.tms.commons.client.report.TestAttachmentApi;
import io.space.geek.tms.commons.client.report.TestRunApi;
import io.space.geek.tms.commons.dto.report.TestRunDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

    static TestAttachmentApi testAttachmentApi() {
        final TestAttachmentApi testAttachmentApi = mock(TestAttachmentApi.class);

        lenient().when(testAttachmentApi.uploadAttachment(any(MultipartFile.class), anyString(), anyString()))
            .thenReturn(StringUtils.EMPTY);

        return testAttachmentApi;
    }

    static TestRunApi testRunApi() {
        final TestRunApi testRunApi = mock(TestRunApi.class);

        lenient().when(testRunApi.createTestRun(any(TestRunDTO.class))).thenReturn(new TestRunDTO());

        return testRunApi;
    }
}
