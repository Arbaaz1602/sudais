package com.alsudais.services;

import com.alsudais.beans.request.RoleRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisRoleService {

    public Mono<?> addRole(RoleRequestBean roleRequestBean);

    public Mono<?> fetchRoleByRoleId(String roleId);
    public Mono<?> fetchAllRole(String filterCriteria, String sortCriteria, Integer page, Integer size);
    public Mono<?> updateRole(RoleRequestBean roleRequestBean);

    public Mono<?> deleteRole(String roleId);

    public Mono<?> fetchHistoricalData(String roleId);
}
