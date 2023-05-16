package com.alsudais.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformModuleRequestBean {

    @JsonProperty(value = "platform_id")
    private String platformId;
    @JsonProperty(value = "module_ids")
    private Set<String> moduleIds;

    private String status;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;

}
