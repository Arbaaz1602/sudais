package com.alsudais.controllers;

import com.alsudais.beans.request.PlatformModuleRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisPlatformModuleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/platform-module")
public class AlSudaisPlatformModuleController {

    @Autowired
    private IAlSudaisPlatformModuleService alSudaisPlatformModuleService;

    @PostMapping(value = "/")
    public Mono<?> addPlatformModule(@Valid @RequestBody PlatformModuleRequestBean platformModuleRequestBean) {
        return this.alSudaisPlatformModuleService.addPlatformModule(platformModuleRequestBean);
    }

    @GetMapping(value = "/{platform_id}")
    public Mono<?> fetchPlatformModuleById(@PathVariable(value = "platform_id") @NotBlank(message = LocaleMessageCodeConstants.PLATFORM_ID_CANT_EMPTY) String platformId) {
        return this.alSudaisPlatformModuleService.fetchPlatformModuleById(platformId);
    }

    @PutMapping(value = "/")
    public Mono<?> updatePlatformModule(@Valid @RequestBody PlatformModuleRequestBean platformModuleRequestBean) {
        return this.alSudaisPlatformModuleService.updatePlatformModule(platformModuleRequestBean);
    }

    @DeleteMapping(value = "/{platform_id}")
    public Mono<?> deleteUserRoleById(@PathVariable(value = "platform_id") @NotBlank(message = LocaleMessageCodeConstants.PLATFORM_ID_CANT_EMPTY) String platformId) {
        return this.alSudaisPlatformModuleService.deletePlatformModuleById(platformId);
    }

    @GetMapping(value = "/historical/{platform_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "platform_id") String platformId) {
        return this.alSudaisPlatformModuleService.fetchHistoricalData(platformId);
    }

}
