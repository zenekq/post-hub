package com.posthub.util.model.request;

import jakarta.validation.constraints.Min;

public record PaginationRequest(

        @Min(0)
        Integer page,

        @Min(0)
        Integer limit
) {
    public PaginationRequest {
        if (page == null) page = 0;
        if (limit == null) limit = 10;
        if (page < 0) page = 0;
        if (limit <= 0) limit = 10;
        if (limit >= 100) limit = 100;
    }
}