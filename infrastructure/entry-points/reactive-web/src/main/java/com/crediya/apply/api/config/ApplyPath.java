package com.crediya.apply.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class ApplyPath {
    private String saveApply;
    private String listApplys;
}
