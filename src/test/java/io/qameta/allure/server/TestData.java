package io.qameta.allure.server;

import io.qameta.allure.server.clients.TestAttachmentApi;
import io.qameta.allure.server.clients.TestRunApi;
import io.qameta.allure.server.dto.TestRunDTO;
import io.qameta.allure.server.feign.FeignClientBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

    static Supplier<FeignClientBuilder> clientBuilder() {
        FeignClientBuilder builder = mock(FeignClientBuilder.class);

        final TestRunApi testRunApi = mock(TestRunApi.class);
        final TestAttachmentApi testAttachmentApi = mock(TestAttachmentApi.class);

        lenient().when(testRunApi.createTestRun(any())).thenReturn(TestRunDTO.builder().build());
        lenient().when(testAttachmentApi.uploadAttachment(any(), anyString(), anyString()))
            .thenReturn(StringUtils.EMPTY);

        when(builder.createClient(TestRunApi.class)).thenReturn(testRunApi);
        when(builder.createClient(TestAttachmentApi.class)).thenReturn(testAttachmentApi);
        return () -> builder;
    }
}
