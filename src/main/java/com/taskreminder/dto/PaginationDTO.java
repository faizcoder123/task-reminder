package com.taskreminder.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PaginationDTO implements Serializable {

    private static final long serialVersionUID = 2432452352453L;

    @JsonProperty("page")
    @Min(value = 0, message = " must be >= 0")
    private int page;

    @JsonProperty("sortBy")
    @Pattern(regexp = "deadline|createdTime", message = " must be (createdTime|deadline)")
    protected String sortBy;

    @JsonProperty("sortOrder")
    @Pattern(regexp = "asc|desc", message = " must be (asc|desc)")
    protected String sortOrder;

    @JsonProperty("perPage")
    @Min(value = 1, message = " must be >= 1")
    @Max(value = 100, message = " must be <= 100")
    private int perPage;

    @JsonProperty("totalCount")
    private long totalCount;

    @JsonProperty("totalPages")
    private int totalPages;

    public Pageable pageable(PaginationDTO page) {
        return PageRequest.of(getPage(), getPerPage(), getSort(page));
    }

    private Sort getSort(PaginationDTO page) {
        if(sortOrder != null && sortOrder.toLowerCase().contentEquals("asc")) {
            return Sort.by(page.getSortBy()).ascending();
        } else {
            return Sort.by(page.getSortBy()).descending();
        }
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public PaginationDTO buildPagination(SearchParams indexParams, Integer page, Integer perPage, String sortBy, String sortOrder) {
        if (indexParams != null && indexParams.getPagination() != null) {
            return indexParams.getPagination();
        }
        PaginationDTOBuilder paginationDTOBuilder = PaginationDTO.builder();
        paginationDTOBuilder.page(0).perPage(10).sortBy("created_at").sortOrder("desc");
        if (page != null) {
            paginationDTOBuilder.page(page);
        }
        if (perPage != null) {
            paginationDTOBuilder.perPage(perPage);
        }
        if (sortBy != null && sortOrder != null) {
            paginationDTOBuilder.sortBy(sortBy);
            paginationDTOBuilder.sortOrder(sortOrder);
        }
        return paginationDTOBuilder.build();
    }
}
