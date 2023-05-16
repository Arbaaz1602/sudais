package com.alsudais.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleModuleSetResponseBean {

    @JsonProperty(value = "module_id")
    private String moduleId;

    @JsonProperty(value = "module_name")
    private String moduleName;
    @JsonProperty(value = "parent_module_id")
    private String parentModuleId;

    @JsonProperty(value = "parent_module_name")
    private String parentModuleName;
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
    private String remark;
    private String status;
    @JsonProperty(value = "added_on")
    private LocalDateTime addedOn;
    @JsonProperty(value = "last_updated_on")
    private LocalDateTime lastUpdatedOn;
    @JsonProperty(value = "last_updated_by")
    private String lastUpdatedBy;
}
