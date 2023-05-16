package com.alsudais.controllers;

import com.alsudais.beans.request.UserRoleModuleRequestBean;
import com.alsudais.services.IAlSudaisUserRoleModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user-role-module")
public class AlSudaisUserRoleModuleController {
    @Autowired
    private IAlSudaisUserRoleModuleService alSudaisUserRoleModuleService;

    @PostMapping(value = "/")
    public Mono<?> addUserRoleModule(@Valid @RequestBody UserRoleModuleRequestBean userRoleModuleRequestBean) {
        return this.alSudaisUserRoleModuleService.addUserRoleModule(userRoleModuleRequestBean);
    }

    @GetMapping(value = "/fetch-by-id")
    public Mono<?> fetchUserRoleModuleById(@RequestParam(value = "user_id", defaultValue = "null") String userId, @RequestParam(value = "role_id", defaultValue = "null") String roleId) {
        return this.alSudaisUserRoleModuleService.fetchUserRoleModuleById(userId, roleId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllUserRoleModule(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                          @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.alSudaisUserRoleModuleService.fetchAllUserRoleModule(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateUserRoleModule(@Valid @RequestBody UserRoleModuleRequestBean userRoleModuleRequestBean) {
        return this.alSudaisUserRoleModuleService.updateUserRoleModule(userRoleModuleRequestBean);
    }

    @DeleteMapping(value = "/")
    public Mono<?> deleteUserRoleModule(@RequestParam(value = "user_id", defaultValue = "null") String userId, @RequestParam(value = "role_id", defaultValue = "null") String roleId) {
        return this.alSudaisUserRoleModuleService.deleteUserRoleModule(userId, roleId);
    }

}
