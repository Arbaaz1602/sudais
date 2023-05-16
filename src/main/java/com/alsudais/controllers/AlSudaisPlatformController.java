package com.alsudais.controllers;

import com.alsudais.beans.request.PlatformRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisPlatformService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${iam-service-version}/platform")
public class AlSudaisPlatformController {

    @Autowired
    private IAlSudaisPlatformService alSudaisPlatformService;

    @PostMapping(value = "/")
    public Mono<?> addPlatform(@Valid @RequestBody PlatformRequestBean platformRequestBean) {
        return this.alSudaisPlatformService.addPlatform(platformRequestBean);
    }

    @GetMapping(value = "/{platform_id}")
    public Mono<?> fetchPlatformById(@PathVariable(value = "platform_id") @NotBlank(message = LocaleMessageCodeConstants.PLATFORM_ID_CANT_EMPTY) String platformId) {
        return this.alSudaisPlatformService.fetchPlatformById(platformId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllPlatform(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                    @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.alSudaisPlatformService.fetchAllPlatform(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updatePlatform(@Valid @RequestBody PlatformRequestBean platformRequestBean) {
        return this.alSudaisPlatformService.updatePlatform(platformRequestBean);
    }

    @DeleteMapping(value = "/{platform_id}")
    public Mono<?> deletePlatform(@PathVariable(value = "platform_id") @NotBlank(message = LocaleMessageCodeConstants.PLATFORM_ID_CANT_EMPTY) String platfromId) {
        return this.alSudaisPlatformService.deletePlatform(platfromId);
    }

    @GetMapping(value = "/historical/{platform_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "platform_id") String platformId) {
        return this.alSudaisPlatformService.fetchHistoricalData(platformId);
    }

}

