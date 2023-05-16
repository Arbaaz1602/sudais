package com.alsudais.beans.request;

import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleRequestBean {
    @JsonProperty(value = "module_id")
    private String moduleId;
    @NotBlank(message = LocaleMessageCodeConstants.MODULE_NAME_CANT_EMPTY)
    @JsonProperty(value = "module_name")
    private String moduleName;
    @JsonProperty(value = "parent_module_id")
    private String parentModuleId;
    @JsonProperty(value = "sort_order")
    private Integer sortOrder;
    private String status;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;
}