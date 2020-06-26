package io.qameta.allure.server.dto;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestRunConfigurationDTO {

    private String url;

    private String branchName;

    private String environment;

    private String platform;

    private String browser;

    private String browserVersion;
}
