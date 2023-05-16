package com.alsudais.controllers;

import com.alsudais.beans.request.UserMasterRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisUserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user")
public class AlSudaisUserController {

    @Autowired
    private IAlSudaisUserService alSudaisUserService;

    @GetMapping(value = "/{user_id}")
    public Mono<?> fetchUserById(@PathVariable(value = "user_id") @NotBlank(message = LocaleMessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.alSudaisUserService.fetchUserById(userId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllUser(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.alSudaisUserService.fetchAll(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateUser(@Valid @RequestBody UserMasterRequestBean userMasterRequestBean) {
        return this.alSudaisUserService.updateUser(userMasterRequestBean);
    }

    @GetMapping(value = "/fetch-by-tenant-id/{tenant_id}")
    public Mono<?> fetchUserByTenantId(@PathVariable(value = "tenant_id") @NotBlank(message = LocaleMessageCodeConstants.TENANT_ID_CANT_EMPTY) String tenantId) {
        return this.alSudaisUserService.fetchUserByTenantId(tenantId);
    }

    @GetMapping(value = "/historical/{user_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "user_id") String userId) {
        return this.alSudaisUserService.fetchHistoricalData(userId);
    }

}
