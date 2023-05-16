package com.alsudais.feign;

import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.exceptions.AlSudaisCustomException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

	private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Lazy
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public Exception decode(String methodKey, Response response) {
		try {
			HttpStatus responseStatus = HttpStatus.valueOf(response.status());
			JsonNode jsonNode = objectMapper.readTree(IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8.name()));
			LOGGER.info("JsonNode :: {}", jsonNode);
			
			String errors = null;
			if(jsonNode.get("error") instanceof ArrayNode) {
				StringBuffer stringBuffer = new StringBuffer();
				((ArrayNode) jsonNode.get("error")).iterator().forEachRemaining(data -> stringBuffer.append(data.asText()).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING));
				errors = stringBuffer.toString();
			}else {
				errors = jsonNode.get("error").asText();
			}
			LOGGER.info("Errors :: {}", errors);
			
			if (responseStatus.is5xxServerError()) {
				return new AlSudaisCustomException(errors);
			} else if (responseStatus.is4xxClientError()) {
				return new AlSudaisCustomException(errors);
			} else {
				return new Exception("Generic exception");
			}
		} catch (IOException e) {
			return new AlSudaisCustomException("Unable to parse response body");
		}
	}
}