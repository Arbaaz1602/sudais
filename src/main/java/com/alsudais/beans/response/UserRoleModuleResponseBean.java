package com.alsudais.beans.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleModuleResponseBean {
    @JsonProperty(value = "user_id")
    private String userId;
    @JsonProperty(value = "role_id")
    private String roleId;
    @JsonProperty(value = "user_role_module")
    private Set<UserRoleModuleSetResponseBean> userRoleModuleSetResponseBeans;

}