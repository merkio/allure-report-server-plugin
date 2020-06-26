package io.qameta.allure.server.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestRunStatisticsDTO {

    private long total;

    private long passed;

    private long failed;

    private long skipped;

    private long started;

    private long aborted;
}
