package io.qameta.allure.server;

public enum EnvVariable {
    ENABLE("report.enabled"),
    RUN_NAME("report.launch.name"),
    RUN_DESCRIPTION("report.description"),
    PROJECT_ID("report.project"),
    BROWSER("report.browser"),
    BROWSER_VERSION("report.browser.version"),
    RELEASE_ID("report.release"),
    STAGE_ID("report.stage"),
    SEND_TO("report.send.to"),
    META("report.meta"),
    TAGS("report.tags"),
    RUN_ID("report.run.id"),
    FEATURE_ID("report.feature"),
    ENVIRONMENT("report.environment"),
    PLATFORM("report.platform"),
    URL("report.url");

    public final String value;

    EnvVariable(String value) {
        this.value = value;
    }
}
