package com.alsudais.configs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class AlSudaisCacheConfig {

    @Value("#{'${redis.cluster.nodes:}'.split(',')}")
    private List<String> redisClusterNodes;

    @Value("#{'${redis.standalone.node:ec2-157-175-205-7.me-south-1.compute.amazonaws.com:6379}'.split(':')}")
    private List<String> redisStandaloneNode;

    @Value("${redis.password:eYVX7EwVmmxKPCDmwMtyKVge8oLd2t81}")
    private String redisPassword;

    @Value("${redis.command.timeout:200}")
    private Long redisCommandTimeout;

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = null;
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(this.redisCommandTimeout)).build();
        if (this.redisClusterNodes.stream().filter(Predicate.not(String::isEmpty)).count() > BigInteger.ZERO.intValue()) {
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(this.redisClusterNodes);
            if (StringUtils.isNotEmpty(this.redisPassword))
                redisClusterConfiguration.setPassword(RedisPassword.of(this.redisPassword));
            lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
        } else if (this.redisStandaloneNode.stream().filter(Predicate.not(String::isEmpty)).count() > BigInteger.ZERO.intValue()) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(this.redisStandaloneNode.get(BigInteger.ZERO.intValue()), Integer.parseInt(this.redisStandaloneNode.get(BigInteger.ONE.intValue())));

            if (StringUtils.isNotEmpty(this.redisPassword))
                redisStandaloneConfiguration.setPassword(RedisPassword.of(this.redisPassword));
            lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);

        }

        if (lettuceConnectionFactory != null)
            lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate() {
        RedisSerializationContext<String, Object> redisSerializationContext = RedisSerializationContext.<String, Object>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class))
                .hashKey(new StringRedisSerializer())
                .hashValue(new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class))
                .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory(), redisSerializationContext);
    }

    @Primary
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
