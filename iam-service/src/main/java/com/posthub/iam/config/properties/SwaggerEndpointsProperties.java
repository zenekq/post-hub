package com.posthub.iam.config.properties;

public record SwaggerEndpointsProperties() {

    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String API_DOCS_YAML = "/v3/api-docs.yaml";
    public static final String UI = "/swagger-ui/**";
    public static final String UI_HTML = "/swagger-ui.html";
    public static final String WEBJARS = "/webjars/**";

    private static final String[] PERMIT = {API_DOCS, API_DOCS_YAML, UI, UI_HTML, WEBJARS};

    public static String[] permit() {
        return PERMIT;
    }
}
