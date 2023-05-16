package com.alsudais.services;

import com.alsudais.beans.request.UserRoleModuleRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisUserRoleModuleService {

    public Mono<?> addUserRoleModule(UserRoleModuleRequestBean userRoleModuleRequestBean);

    public Mono<?> addUserRoleModuleInternal(UserRoleModuleRequestBean userRoleModuleRequestBean);

    public Mono<?> fetchUserRoleModuleById(String userId, String roleId);

    public Mono<?> fetchAllUserRoleModule(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updateUserRoleModule(UserRoleModuleRequestBean userRoleModuleRequestBean);

    public Mono<?> deleteUserRoleModule(String userId, String roleId);

}
