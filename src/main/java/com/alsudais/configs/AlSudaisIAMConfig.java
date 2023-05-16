package com.alsudais.configs;

import com.bugsnag.Bugsnag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.ReactiveOptions;
import reactivefeign.spring.config.ReactiveFeignClientsProperties;
import reactivefeign.spring.config.ReactiveFeignNamedContextFactory;
import reactivefeign.webclient.WebReactiveOptions;


@Configuration
public class AlSudaisIAMConfig {

    @Value(value = "${bugsnag.api.key:f3c024cfef8e61283a7ce3d9c2ad102c}")
    private String bugsnagApiKey;

    @Value(value = "${iam.service.app.version:Alsudais_IAM_1.0.0}")
    private String iamServiceAppVersion;

    @Bean
    public Bugsnag initBugSnag() {
        Bugsnag bugsnag = new Bugsnag(this.bugsnagApiKey);
        bugsnag.setAppVersion(this.iamServiceAppVersion);
       // bugsnag.notify(new RuntimeException("Test error"));
        return bugsnag;
    }

    @Bean
    public ReactiveOptions reactiveOptions() {
        return new WebReactiveOptions.Builder()
                .setReadTimeoutMillis(2000)
                .setWriteTimeoutMillis(2000)
                .setResponseTimeoutMillis(2000)
                .build();
    }

    @Bean
    public ReactiveFeignNamedContextFactory reactiveFeignNamedContextFactory(){
        return new ReactiveFeignNamedContextFactory();
    }

    @Bean
    public ReactiveFeignClientsProperties reactiveFeignClientsProperties(){
        return new ReactiveFeignClientsProperties();
    }
//
//    @Bean
//    public Decoder feignDecoder() {
//        ObjectFactory<HttpMessageConverters> messageConverters = () -> initHttpMessageConverters();
//        return new SpringDecoder(messageConverters);
//    }
//
//    @Bean
//    public HttpMessageConverters initHttpMessageConverters() {
//        HttpMessageConverters converters = new HttpMessageConverters();
//        return converters;
//    }

}