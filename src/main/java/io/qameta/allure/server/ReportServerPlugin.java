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

import io.qameta.allure.Aggregator;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.LaunchResults;
import io.qameta.allure.entity.Statistic;
import io.qameta.allure.server.feign.FeignClientBuilder;
import io.space.geek.tms.commons.client.report.TestAttachmentApi;
import io.space.geek.tms.commons.client.report.TestRunApi;
import io.space.geek.tms.commons.dto.report.TestAttachmentDTO;
import io.space.geek.tms.commons.dto.report.TestRunConfigurationDTO;
import io.space.geek.tms.commons.dto.report.TestRunDTO;
import io.space.geek.tms.commons.dto.report.TestRunStatisticsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.qameta.allure.util.PropertyUtils.getProperty;

/**
 * The plugin adds ability to upload report to the server.
 *
 * @since 2.0
 */

@Slf4j
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.UseUtilityClass"})
public class ReportServerPlugin implements Aggregator {

    private static final String ALLURE_REPORT_SERVER_ENABLED = "ALLURE_REPORT_SERVER_ENABLED";

    private final ReportEnvironmentParameters envParameters;
    private final TestRunApi testRunApi;
    private final TestAttachmentApi testAttachmentApi;

    private final boolean enabled;

    public ReportServerPlugin() {
        this(
            getProperty(ALLURE_REPORT_SERVER_ENABLED).map(Boolean::parseBoolean).orElse(false),
            new FeignClientBuilder().defaults()
        );
    }

    public ReportServerPlugin(boolean enabled, FeignClientBuilder clientBuilder) {
        this(
            enabled,
            clientBuilder.createClient(TestRunApi.class),
            clientBuilder.createClient(TestAttachmentApi.class)
        );
    }

    public ReportServerPlugin(boolean enabled,
                              TestRunApi testRunApi,
                              TestAttachmentApi testAttachmentApi) {
        this.enabled = enabled;
        this.envParameters = new ReportEnvironmentParameters();

        this.testRunApi = testRunApi;
        this.testAttachmentApi = testAttachmentApi;
    }

    @Override
    public void aggregate(Configuration configuration,
                          List<LaunchResults> launchesResults,
                          Path outputDirectory) {
        if (enabled) {
            log.info("Upload test results to report server");

            TestRunDTO runDTO = getTestRunDTO(launchesResults);
            testRunApi.createTestRun(runDTO);

            List<TestAttachmentDTO> uploadedAttachments = new LinkedList<>();
            launchesResults.forEach(launchResults -> uploadedAttachments.addAll(uploadAttachments(runDTO.getName(), launchResults)));
            log.info("Successfully saved [{}] attachments on report server", uploadedAttachments.size());
        }
    }

    private TestRunDTO getTestRunDTO(List<LaunchResults> results) {

        final Statistic statistic = getStatistic(results);

        TestRunDTO runDTO = TestRunDTO.builder()
            .id(envParameters.getRunId())
            .name(envParameters.getRunName())
            .description(envParameters.getRunDescription())
            .featureId(envParameters.getFeatureId())
            .projectId(envParameters.getProjectId())
            .meta(envParameters.getMeta())
            .sendTo(envParameters.getSendTo())
            .tags(envParameters.getTags())
            .statistics(TestRunStatisticsDTO.builder()
                            .aborted(statistic.getBroken())
                            .failed(statistic.getFailed())
                            .passed(statistic.getPassed())
                            .skipped(statistic.getSkipped())
                            .total(statistic.getTotal())
                            .build())
            .configuration(TestRunConfigurationDTO.builder()
                               .browser(envParameters.getBrowser())
                               .browserVersion(envParameters.getBrowserVersion())
                               .environment(envParameters.getEnvironment())
                               .platform(envParameters.getPlatform())
                               .url(envParameters.getUrl())
                               .build())
            .results(results.stream().map(LaunchResults::getAllResults).flatMap(Collection::stream).collect(Collectors.toSet()))
            .build();
        log.info("Create test run [{}]", runDTO.getName());
        return runDTO;
    }

    private List<TestAttachmentDTO> uploadAttachments(String runName, LaunchResults results) {
        return results.getAttachments().entrySet().stream()
            .map(entry -> {
                File filePath = entry.getKey().toFile();
                String name = entry.getValue().getName();
                try {
                    MockMultipartFile file = new MockMultipartFile(name, entry.getValue().getUid(), null, new FileInputStream(filePath));
                    String url = testAttachmentApi.uploadAttachment(file, runName, entry.getValue().getUid());
                    return TestAttachmentDTO.builder().url(url).uid(entry.getValue().getUid()).build();
                } catch (IOException e) {
                    log.error("Error during upload file [{}] in path [{}] to the server", filePath.toString(), name, e);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private Statistic getStatistic(final List<LaunchResults> launchesResults) {
        final Statistic statistic = new Statistic();
        launchesResults.stream()
            .map(LaunchResults::getAllResults)
            .flatMap(Collection::stream)
            .forEach(statistic::update);
        return statistic;
    }
}
