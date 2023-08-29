package com.logicea.cards.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBodyDto<T> {

    private int page;
    private int size;
    private int totalCount;
    private int totalPages;
    private T content;

}
