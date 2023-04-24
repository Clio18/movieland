package com.tteam.movieland.entity.kafka;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class ReportRequest {
    private ReportType type;
    private StrategyName strategy;
}