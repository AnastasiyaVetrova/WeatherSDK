package com.project.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApiKey {
    @EqualsAndHashCode.Include
    private String apiKey;
    private TypeApiEnum type;
    private String lang;
}
