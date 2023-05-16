package com.alsudais.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleModuleRequestBean {
    @JsonProperty(value = "user_id")
    private String userId;
    @JsonProperty(value = "role_id")
    private String roleId;
    @JsonProperty(value = "user_role_module")
    private Set<UserRoleModuleSetRequestBean> userRoleModuleSetRequestBeans;
    private String remark;
    private String status;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;
    @JsonIgnore
    private LocalDateTime createdDate;
    @JsonIgnore
    private LocalDateTime modifiedDate;

}