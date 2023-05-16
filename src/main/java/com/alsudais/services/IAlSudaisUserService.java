package com.alsudais.services;

import com.alsudais.beans.request.UserMasterRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisUserService {

    public Mono<?> fetchUserById(String userId);

    public Mono<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updateUser(UserMasterRequestBean userMasterRequestBean);

    public Mono<?> fetchUserByTenantId(String tenantId);

    public Mono<?> fetchHistoricalData(String userId);
}
