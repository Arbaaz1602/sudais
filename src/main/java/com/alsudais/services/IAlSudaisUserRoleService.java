package com.alsudais.services;

import com.alsudais.beans.request.UserRoleRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisUserRoleService {
    public Mono<?> fetchAllUserRole(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> addUserRole(UserRoleRequestBean userRoleRequestBean);

    public Mono<?> fetchUserRoleById(String userId);

    public Mono<?> updateUserRole(UserRoleRequestBean userRoleRequestBean);

    public Mono<?> deleteUserRoleById(String userId);

    public Mono<?> fetchHistoricalData(String userId);
}
