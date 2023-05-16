package com.alsudais.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponseBean <T>{
    @JsonProperty(value = "total_pages")
    private Integer totalPages;
    @JsonProperty(value = "current_page")
    private Integer currentPage;
    @JsonProperty(value = "page_size")
    private Integer pageSize;
    @JsonProperty(value = "total_elements")
    private Long totalElements;
    @JsonProperty(value = "number_of_elements")
    private Integer numberOfElements;
    @JsonProperty(value = "payload")
    private T payload;

}