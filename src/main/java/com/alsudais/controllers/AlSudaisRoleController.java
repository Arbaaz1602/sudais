package com.alsudais.controllers;

import com.alsudais.beans.request.RoleRequestBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/role")
public class AlSudaisRoleController {
    @Autowired
    private IAlSudaisRoleService alSudaisRoleService;

    @PostMapping(value = "/")
    public Mono<?> addRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.alSudaisRoleService.addRole(roleRequestBean);
    }

    @GetMapping(value = "/{role_id}")
    public Mono<?> fetchRoleById(@PathVariable(value = "role_id") @NotBlank(message = LocaleMessageCodeConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return this.alSudaisRoleService.fetchRoleByRoleId(roleId);
    }

    @GetMapping(value = "/")
    public Mono<?> fetchAllRole(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "2147483647") Integer size) {
        return this.alSudaisRoleService.fetchAllRole(filterCriteria, sortCriteria, page, size);
    }

    @PutMapping(value = "/")
    public Mono<?> updateRole(@Valid @RequestBody RoleRequestBean roleRequestBean) {
        return this.alSudaisRoleService.updateRole(roleRequestBean);
    }

    @DeleteMapping(value = "/{role_id}")
    public Mono<?> deleteRole(@PathVariable(value = "role_id") @NotBlank(message = LocaleMessageCodeConstants.ROLE_ID_CANT_EMPTY) String roleId) {
        return this.alSudaisRoleService.deleteRole(roleId);
    }

    @GetMapping(value = "/historical/{role_id}")
    public Mono<?> fetchHistoricalData(@PathVariable(value = "role_id") String roleId) {
        return this.alSudaisRoleService.fetchHistoricalData(roleId);
    }
}
