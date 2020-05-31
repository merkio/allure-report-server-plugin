package io.qameta.allure.server;

import io.qameta.allure.util.PropertyUtils;
import lombok.Getter;

import java.util.Optional;

/**
 * The pojo with all environment parameters.
 *
 * @since 2.0
 */
@Getter
public class ReportEnvironmentParameters {

    private Long releaseId;
    private Long stageId;
    private Boolean enabled;
    private final Long projectId;
    private final String browserName;
    private final String browserVersion;
    private final String meta;
    private final String tags;
    private final String runId;
    private final String environment;
    private final String runName;
    private final String runDescription;
    private final String sendTo;
    private Long featureId;
    private final String browser;
    private final String platform;
    private final String url;

    public ReportEnvironmentParameters() {
        Optional<String> enabledO = PropertyUtils.getProperty(EnvVariable.ENABLE.value);
        enabledO.ifPresent(enabled -> this.enabled = Boolean.parseBoolean(enabled.trim()));
        
        this.runName = PropertyUtils.getProperty(EnvVariable.RUN_NAME.value).orElse("DEFAULT");
        this.runDescription = PropertyUtils.getProperty(EnvVariable.RUN_DESCRIPTION.value).orElse(null);
        this.projectId = Long.parseLong(PropertyUtils.getProperty(EnvVariable.PROJECT_ID.value).orElse("0"));
        this.browserName = PropertyUtils.getProperty(EnvVariable.BROWSER.value).orElse(null);
        this.browserVersion = PropertyUtils.getProperty(EnvVariable.BROWSER_VERSION.value).orElse(null);

//        release parameters
        Optional<String> releaseIdO = PropertyUtils.getProperty(EnvVariable.RELEASE_ID.value);
        Optional<String> stageIdO = PropertyUtils.getProperty(EnvVariable.STAGE_ID.value);
        
        releaseIdO.ifPresent(releaseId -> this.releaseId = Long.parseLong(releaseId.trim()));
        stageIdO.ifPresent(stageId -> this.stageId = Long.parseLong(stageId.trim()));
        
//        notification parameter
        this.sendTo = PropertyUtils.getProperty(EnvVariable.SEND_TO.value).orElse(null);
        
//        additional parameters from environment variables
        this.meta = PropertyUtils.getProperty(EnvVariable.META.value).orElse(null);

        this.tags = PropertyUtils.getProperty(EnvVariable.TAGS.value).orElse(null);

//        set a test run id if we've already created the test run
        this.runId = PropertyUtils.getProperty(EnvVariable.RUN_ID.value).orElse(null);
        
        Optional<String> featureIdO = PropertyUtils.getProperty(EnvVariable.FEATURE_ID.value);
        featureIdO.ifPresent(featureId -> this.featureId = Long.parseLong(featureId.trim()));

//        test run configuration properties
        this.environment = PropertyUtils.getProperty(EnvVariable.ENVIRONMENT.value).orElse(null);
        this.browser = PropertyUtils.getProperty(EnvVariable.BROWSER.value).orElse(null);
        this.platform = PropertyUtils.getProperty(EnvVariable.PLATFORM.value).orElse(null);
        this.url = PropertyUtils.getProperty(EnvVariable.URL.value).orElse(null);
    }
}
