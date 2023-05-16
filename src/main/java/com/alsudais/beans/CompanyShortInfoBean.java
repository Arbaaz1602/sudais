package com.alsudais.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyShortInfoBean {

    private Long id;

    private String name;

    @JsonAlias({"logo_url", "logoUrl"})
    @JsonProperty(value = "logo_url")
    private String logoUrl;
}
