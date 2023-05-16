package com.alsudais.services;

import com.alsudais.beans.request.ModuleRequestBean;
import com.alsudais.beans.response.ServiceResponseBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAlSudaisModuleService {
    public Mono<?> addModule(ModuleRequestBean moduleRequestBean);

    public Mono<?> fetchModuleByModuleId(String moduleId);

    public Mono<?> fetchAllModule(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updateModule(ModuleRequestBean moduleRequestBean);

    public Mono<?> deleteModule(String moduleId);

    public Mono<?> fetchHistoricalData(String moduleId);
}