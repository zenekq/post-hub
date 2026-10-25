package com.posthub.iam.model.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;
    private Pagination pagination;

    public record Pagination(
            long total,
            int limit,
            int page,
            int pages
    ) {}

}
