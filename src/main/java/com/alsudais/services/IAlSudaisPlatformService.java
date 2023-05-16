package com.alsudais.services;

import com.alsudais.beans.request.PlatformRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisPlatformService {

    public Mono<?> addPlatform(PlatformRequestBean platformRequestBean);

    public Mono<?> fetchPlatformById(String platformId);

    public Mono<?> fetchAllPlatform(String filterCriteria, String sortCriteria, Integer page, Integer size);

    public Mono<?> updatePlatform(PlatformRequestBean platformRequestBean);

    public Mono<?> deletePlatform(String platform);

    public Mono<?> fetchHistoricalData(String platformId);
}
