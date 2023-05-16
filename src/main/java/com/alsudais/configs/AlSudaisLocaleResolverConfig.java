package com.alsudais.configs;

import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.enums.LanguageCodeEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class AlSudaisLocaleResolverConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ResourceBundleMessageSource resourceBundleMessageSource = null;

    private ServerWebExchange serverWebExchange;

    private final List<Locale> LOCALES = LanguageCodeEnum.getAllValues().parallelStream().map(Locale::new).collect(Collectors.toList());

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String applicationName;

    @Value("${spring.webflux.base-path}")
    private String springWebfluxBasePath;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public HttpHandler httpHandler(ApplicationContext applicationContext) {
        HttpHandler delegate = WebHttpHandlerBuilder.applicationContext(applicationContext).build();

        HttpWebHandlerAdapter httpWebHandlerAdapter = new HttpWebHandlerAdapter(((HttpWebHandlerAdapter) delegate)) {
            @Override
            protected ServerWebExchange createExchange(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
                serverWebExchange = super.createExchange(serverHttpRequest, serverHttpResponse);
                String headerLanguage = serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE);
                LOGGER.debug("HeaderLanguage :: {}", headerLanguage);

                if (headerLanguage != null && !headerLanguage.isEmpty()) {
                    Optional<String> headerLanguageOptional =
                            Arrays.stream(headerLanguage.split(AlSudaisCommonConstants.INSTANCE.SEMI_COLON_STRING))
                                    .flatMap(data -> Arrays.stream(data.split(AlSudaisCommonConstants.INSTANCE.COMMA_STRING)))
                                    .filter(headerLanguageInner -> LanguageCodeEnum.getAllValues().stream().anyMatch(languageCode -> languageCode.equalsIgnoreCase(headerLanguageInner))).findFirst();

                    LOGGER.debug("HeaderLanguageOptional :: {}", headerLanguageOptional);
                    ServerHttpRequest.Builder serverHttpRequestBuilder = serverWebExchange.getRequest().mutate();

                    ServerWebExchange.Builder serverWebExchangeBuilder = serverWebExchange.mutate();
                    headerLanguageOptional.ifPresent(languageCode -> serverHttpRequestBuilder.header(HttpHeaders.ACCEPT_LANGUAGE, languageCode));
                    serverWebExchangeBuilder.build();

                    LocaleContext localeContext = new SimpleLocaleContext(!headerLanguageOptional.isPresent() || StringUtils.isEmpty(headerLanguageOptional.get()) ? new Locale(LanguageCodeEnum.ENGLISH.getValue()) : Locale.lookup(Locale.LanguageRange.parse(headerLanguageOptional.get()), LOCALES));
                    LocaleContextHolder.setLocaleContext(localeContext);
                }
                return serverWebExchange;
            }
        };

        return new ContextPathCompositeHandler(Map.of(this.springWebfluxBasePath, httpWebHandlerAdapter));
    }

    @Bean
    public ResourceBundleMessageSource getResourceBundleMessageSource() {
        this.resourceBundleMessageSource = new ResourceBundleMessageSource();
        this.resourceBundleMessageSource.setBasename(AlSudaisCommonConstants.INSTANCE.MESSAGES_STRING);
        this.resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        this.resourceBundleMessageSource.setUseCodeAsDefaultMessage(Boolean.TRUE);
        return this.resourceBundleMessageSource;
    }

    public String toLocale(String messageCode) {
        Locale locale = this.serverWebExchange.getLocaleContext().getLocale();
        return this.resourceBundleMessageSource.getMessage(messageCode, null, locale);
    }

    public String getIdentity() {
        try {
            String accessToken = this.serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            accessToken = accessToken.replace(AlSudaisCommonConstants.INSTANCE.BEARER_STRING, AlSudaisCommonConstants.INSTANCE.BLANK_STRING);
            LOGGER.info("Access Token :: {}", accessToken);
            String accessTokenPayload = accessToken.split(AlSudaisCommonConstants.INSTANCE.PERIOD_SPLITTER_STRING)[BigInteger.ONE.intValue()];
            LOGGER.info("Access Token Payload :: {}", accessTokenPayload);
            JsonNode jsonNode = this.objectMapper.readTree(Base64.getDecoder().decode(accessTokenPayload));
            String userId = StringUtils.isNotEmpty(jsonNode.get("user_id").asText()) ? jsonNode.get("user_id").asText() : jsonNode.get("sub").asText();
            LOGGER.info("UserId:: {}", userId);
            return userId;
        } catch (Exception e) {
            return this.applicationName;
        }
    }
}
