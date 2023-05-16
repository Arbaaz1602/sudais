package com.alsudais.services;

import com.alsudais.beans.request.PlatformModuleRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisPlatformModuleService {

    public Mono<?> addPlatformModule(PlatformModuleRequestBean platformModuleRequestBean);

    public Mono<?> fetchPlatformModuleById(String platformId);

    public Mono<?> updatePlatformModule(PlatformModuleRequestBean platformModuleRequestBean);

    public Mono<?> deletePlatformModuleById(String platformId);

    public Mono<?> fetchHistoricalData(String platformId);
}
