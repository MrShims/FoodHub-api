package org.mrshim.menuservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableResponse<T> {
    private List<T> data;
    private Integer totalPage = 0;
    private Long totalElements = 0L;
}
