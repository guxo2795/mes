package com.wimir.bae.global.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ListWrapperDTO<T> {

    @NotEmpty
    @Valid
    private List<T> list;
}
