package com.posthub.iam.util;

import com.posthub.iam.model.responce.ApiResult;
import tools.jackson.databind.ObjectMapper;

public class TestUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static  <T> ApiResult<T> parseDTOResponse(byte[] contentAsByteArray, Class<T> payloadClass) {
        ApiResult<T> rawResult = objectMapper.readValue(contentAsByteArray, ApiResult.class);
        T payload = objectMapper.convertValue(rawResult.getPayload(), payloadClass);
        rawResult.setPayload(payload);
        return rawResult;
    }

}
