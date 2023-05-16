package com.alsudais.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AlSudaisOpenApi3Config {

	@Value("${openapi3.group}")
	private String openApi3Group;

	@Value("${openapi3.package.to.scan}")
	private String openApi3PackageToScan;

	@Value("${openapi3.title}")
	private String openApi3Title;

	@Value("${openapi3.description}")
	private String openApi3Description;

	@Value("${openapi3.version}")
	private String openApi3Version;

	@Value("${openapi3.contact.name}")
	private String openApi3ContactName;

	@Value("${openapi3.contact.url}")
	private String openApi3ContactUrl;

	@Value("${openapi3.contact.email}")
	private String openApi3ContactEmail;

	@Value("${spring.webflux.base-path}")
	private String springWebfluxBasePath;

	@Bean
	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder()
				.group(this.openApi3Group)
				.packagesToScan(this.openApi3PackageToScan)
				.build();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url(this.springWebfluxBasePath))
				.info(new Info().title(this.openApi3Title)
						.description(this.openApi3Description)
						.version(this.openApi3Version)
						.contact(new Contact().name(this.openApi3ContactName)
											  .url(this.openApi3ContactUrl)
											  .email(this.openApi3ContactEmail)
								)
						);
	}
}