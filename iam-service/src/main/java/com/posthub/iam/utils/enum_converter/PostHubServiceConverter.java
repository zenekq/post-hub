package com.posthub.iam.utils.enum_converter;

import com.posthub.iam.kafka.model.utils.PostHubService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PostHubServiceConverter implements AttributeConverter<PostHubService,String> {

    @Override
    public String convertToDatabaseColumn(PostHubService attribute) {
        return attribute.getValue();
    }

    @Override
    public PostHubService convertToEntityAttribute(String dbData) {
        return PostHubService.fromValue(dbData);
    }

}
