package com.alsudais.controllers;

import com.alsudais.beans.request.UserRoleRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisUserRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user-role")
public class AlSudaisUserRoleController {
    @Autowired
    private IAlSudaisUserRoleService alSudaisUserRoleService;

    @GetMapping(value = "/")
    public Mono<?> fetchAllUserRole(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                    @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.alSudaisUserRoleService.fetchAllUserRole(filterCriteria, sortCriteria, page, size);
    }

    @PostMapping(value = "/")
    public Mono<?> addUserRole(@Valid @RequestBody UserRoleRequestBean userRoleRequestBean) {
        return this.alSudaisUserRoleService.addUserRole(userRoleRequestBean);
    }

    @GetMapping(value = "/{user_id}")
    public Mono<?> fetchUserRoleById(@PathVariable(value = "user_id") @NotBlank(message = LocaleMessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.alSudaisUserRoleService.fetchUserRoleById(userId);
    }

    @PutMapping(value = "/")
    public Mono<?> updateUserRole(@Valid @RequestBody UserRoleRequestBean userRoleRequestBean) {
        return this.alSudaisUserRoleService.updateUserRole(userRoleRequestBean);
    }

    @DeleteMapping(value = "/{user_id}")
    public Mono<?> deleteUserRoleById(@PathVariable(value = "user_id") @NotBlank(message = LocaleMessageCodeConstants.USER_ID_CANT_EMPTY) String userId) {
        return this.alSudaisUserRoleService.deleteUserRoleById(userId);
    }

    @GetMapping(value = "/historical/{user_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "user_id") String userId) {
        return this.alSudaisUserRoleService.fetchHistoricalData(userId);
    }
}
