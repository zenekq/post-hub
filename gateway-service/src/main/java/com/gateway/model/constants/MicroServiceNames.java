package com.gateway.model.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public final class MicroServiceNames {
    @Value("${internal.iam.service.name:iam-service}")
    private String iamServiceName;

    @Value("${internal.utils.service.name}")
    private String utilsServiceName;
}
