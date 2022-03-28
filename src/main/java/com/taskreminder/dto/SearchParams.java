package com.taskreminder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchParams implements Serializable {

    private static final long serialVersionUID = 2432452352453L;


    @Valid
    @JsonProperty("searchCriteria")
    private List<SearchCriteriaDTO> searchCriteria;

    @Valid
    @JsonProperty("pagination")
    private PaginationDTO pagination;

}