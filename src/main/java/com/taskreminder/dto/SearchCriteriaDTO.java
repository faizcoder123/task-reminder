package com.taskreminder.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchCriteriaDTO implements Serializable {

    private static final long serialVersionUID = 2432452352453L;

    private String field;
    private String operator;
    private List<String> values;

    @JsonCreator
    public SearchCriteriaDTO(
            @NotNull(message = "'field' can't be null")
            @JsonProperty("field")
                    String field,
            @NotNull(message = "'operator' can't be null")
            @Pattern(regexp = "<|!|=|>|exist|not_exist|prefix")
            @JsonProperty("operator")
                    String operator,
            @NotNull(message = "'value' can't be null")
            @JsonProperty("values")
                    List<String> values

    ) {
        this.field = field;
        this.operator = operator;
        this.values = values;
    }

    public SearchCriteriaDTO(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.values = Collections.singletonList(value);
    }

}