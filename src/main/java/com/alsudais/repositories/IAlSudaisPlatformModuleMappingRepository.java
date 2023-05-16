package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisPlatformModuleMapping;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface IAlSudaisPlatformModuleMappingRepository extends IBaseRepository<AlSudaisPlatformModuleMapping, Long>{

    public Flux<AlSudaisPlatformModuleMapping> findByApmmPlatformIdAndApmmModuleIdIn(String platformId, Set<String> moduleIds);

    public Mono<Long> deleteByApmmPlatformId(String platformId);

    public Flux<AlSudaisPlatformModuleMapping> findByApmmPlatformId(String platformId);

    public Mono<Long> deleteByApmmPlatformIdAndApmmModuleIdIn(String platformId, Set<String> moduleIds);

}
