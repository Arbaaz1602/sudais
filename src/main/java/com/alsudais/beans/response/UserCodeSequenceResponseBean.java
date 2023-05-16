package com.alsudais.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCodeSequenceResponseBean {

    @JsonProperty(value = "identifier")
    private String identifier;

    @JsonProperty(value = "counter")
    private Integer counter;

}
