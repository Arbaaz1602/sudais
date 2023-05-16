package com.alsudais.beans.request;

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
public class UserRoleModuleSetRequestBean {

    @JsonProperty(value = "module_id")
    private String moduleId;
    @JsonProperty(value = "parent_module_id")
    private String parentModuleId;
    @JsonProperty(value = "full_control")
    private Boolean fullControl;
    private Boolean modify;
    @JsonProperty(value = "read_and_execute")
    private Boolean readAndExecute;
    private Boolean approver;
    private Boolean read;
    private Boolean write;
    @JsonProperty(value = "no_access")
    private Boolean noAccess;

}
