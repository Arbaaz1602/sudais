package com.alsudais.feign.clients;

import com.alsudais.beans.NotificationDetailPayloadBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value = "IAlSudaisNotificationServiceReactiveFeignClient", url = "${alsudais.notification.service.url:http://127.0.0.1:8082}")
public interface IAlSudaisNotificationServiceReactiveFeignClient {

    @PostMapping(value = "/${notification-service-version}/notification/produce-email")
    public Mono<?> sendEmailEvent(@RequestBody NotificationDetailPayloadBean notificationDetailPayloadBean);

}
