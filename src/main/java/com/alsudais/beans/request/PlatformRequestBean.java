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
public class PlatformRequestBean {

    @JsonProperty(value = "platform_id")
    private String platformId;
    @JsonProperty(value = "platform_name")
    @NotBlank(message = LocaleMessageCodeConstants.PLATFORM_NAME_CANT_EMPTY)
    private String platformName;
    private String status;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;
    
}
