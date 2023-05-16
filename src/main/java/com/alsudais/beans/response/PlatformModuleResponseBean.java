package com.alsudais.beans.response;

import com.alsudais.commons.AlSudaisCommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PlatformModuleResponseBean {

    @JsonProperty(value = "platform_id")
    private String platformId;
    @JsonProperty(value = "module_ids")
    private Set<String> moduleIds;

    @JsonProperty(value = "added_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DATE_FORMAT)
    private LocalDateTime addedOn;
    @JsonProperty(value = "modified_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DATE_FORMAT)
    private LocalDateTime modifiedOn;

}
