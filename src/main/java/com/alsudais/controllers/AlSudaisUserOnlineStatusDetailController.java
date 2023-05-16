package com.alsudais.controllers;

import com.alsudais.beans.request.UserOnlineStatusDetailRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisUserOnlineStatusDetailService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user-online-status-detail")
public class AlSudaisUserOnlineStatusDetailController {

    @Autowired
    private IAlSudaisUserOnlineStatusDetailService alSudaisUserOnlineStatusDetailService;

    @GetMapping(value = "/{user_id}")
    public Mono<?> fetchUserOnlineStatusByUserId(@PathVariable(value = "user_id") @NotBlank(message = LocaleMessageCodeConstants.USER_ID_CANT_EMPTY) String userId){
        return this.alSudaisUserOnlineStatusDetailService.fetchUserOnlineStatusByUserId(userId);
    }

    @PatchMapping(value = "/")
    public Mono<?> updateUserOnlineStatus(@Valid @RequestBody UserOnlineStatusDetailRequestBean userOnlineStatusDetailRequestBean){
        return this.alSudaisUserOnlineStatusDetailService.updateUserOnlineStatus(userOnlineStatusDetailRequestBean);
    }

    @GetMapping(value = "/historical/{user_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "user_id") String userId) {
        return this.alSudaisUserOnlineStatusDetailService.fetchHistoricalData(userId);
    }


}
