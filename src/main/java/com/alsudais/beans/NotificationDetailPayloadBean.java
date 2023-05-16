package com.alsudais.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDetailPayloadBean {

	@NotEmpty(message = "Notification type cannot be empty or blank")
	@JsonProperty(value = "notification_type")
	private String notificationType;

	@JsonProperty(value = "user_id")
	private String userId;

	@JsonProperty(value = "subject_line")
	private String subjectLine;

	@JsonProperty(value = "message_body")
	private String messageBody;

	private String from;

	private List<String> to;

	private List<String> cc;

	private List<String> bcc;

	@JsonProperty(value = "template_name")
	private String templateName;

	@JsonProperty(value = "attachment_paths")
	private List<String> attachmentPaths;

	@JsonProperty(value = "data_map")
	private Map<String, Object> dataMap;
}