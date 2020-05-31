/*
 *  Copyright 2019 Qameta Software OÜ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.qameta.allure.server;

import io.qameta.allure.DefaultLaunchResults;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Status;
import io.qameta.allure.entity.TestResult;
import io.space.geek.tms.commons.client.report.TestAttachmentApi;
import io.space.geek.tms.commons.client.report.TestRunApi;
import io.space.geek.tms.commons.dto.report.TestRunDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.qameta.allure.entity.LabelName.FEATURE;
import static io.qameta.allure.entity.LabelName.STORY;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Igor Merkushev merkushevio@gmail.com
 */
@ExtendWith(MockitoExtension.class)
class ReportServerPluginTest {

    @Test
    void storiesPerFeatureResultsAggregation() {
        final Set<TestResult> testResults = new HashSet<>();
        testResults.add(new TestResult()
                            .setStatus(Status.PASSED)
                            .setLabels(asList(FEATURE.label("feature1"), FEATURE.label("feature2"), STORY.label("story1"), STORY.label("story2"))));
        testResults.add(new TestResult()
                            .setStatus(Status.FAILED)
                            .setLabels(asList(FEATURE.label("feature2"), FEATURE.label("feature3"), STORY.label("story2"), STORY.label("story3"))));

        LaunchResults results = new DefaultLaunchResults(testResults, Collections.emptyMap(), Collections.emptyMap());

        TestRunApi testRunApi = TestData.testRunApi();
        TestAttachmentApi testAttachmentApi = TestData.testAttachmentApi();

        ReportServerPlugin reportServerPlugin = new ReportServerPlugin(true, testRunApi, testAttachmentApi);

        reportServerPlugin.aggregate(mock(Configuration.class),
                                     Collections.singletonList(results),
                                     Paths.get("/"));
        verify(testRunApi, times(1)).createTestRun(any(TestRunDTO.class));
        verify(testAttachmentApi, times(0)).uploadAttachment(any(MockMultipartFile.class), anyString(), anyString());
    }
}
