package io.qameta.allure.server.dto;

import io.qameta.allure.entity.TestResult;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestRunDTO {

    private String id;

    private Long projectId;

    private Long featureId;

    private String name;

    private String description;

    private String tags;

    private Integer status;

    private LocalDateTime started;

    private LocalDateTime finished;

    private Integer duration;

    private String meta;

    private boolean manual;

    private String sendTo;

    private TestRunConfigurationDTO configuration;

    private TestRunStatisticsDTO statistics;

    private Set<TestResult> results;

}
