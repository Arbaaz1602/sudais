package com.alsudais.controllers;

import com.alsudais.beans.request.ModuleRequestBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisModuleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping(path = "${iam-service-version}/module")
public class AlSudaisModuleController {
    @Autowired
    private IAlSudaisModuleService alSudaisModuleService;

    @PostMapping(value = "/")
    public Mono<?> addModule(@Valid @RequestBody ModuleRequestBean moduleRequestBean) {
        return this.alSudaisModuleService.addModule(moduleRequestBean);
    }

    @GetMapping(value = "/{module_id}")
    public Mono<?> fetchModuleById(@PathVariable(value = "module_id") @NotBlank(message = LocaleMessageCodeConstants.MODULE_ID_CANT_EMPTY) String moduleId){
        return this.alSudaisModuleService.fetchModuleByModuleId(moduleId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllModule(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                  @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "2147483647") Integer size){
        return this.alSudaisModuleService.fetchAllModule(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateModule(@Valid @RequestBody ModuleRequestBean moduleRequestBean){
        return this.alSudaisModuleService.updateModule(moduleRequestBean);
    }

    @DeleteMapping(value = "/{module_id}")
    public Mono<?> deleteModule(@PathVariable(value = "module_id") @NotBlank(message = LocaleMessageCodeConstants.MODULE_ID_CANT_EMPTY) String moduleId){
        return this.alSudaisModuleService.deleteModule(moduleId);
    }

    @GetMapping(value = "/historical/{module_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "module_id") String moduleId) {
        return this.alSudaisModuleService.fetchHistoricalData(moduleId);
    }

}