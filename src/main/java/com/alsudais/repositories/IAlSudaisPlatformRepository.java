package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisPlatformDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IAlSudaisPlatformRepository extends IBaseRepository<AlSudaisPlatformDetail, Long>{

    public Mono<AlSudaisPlatformDetail> findByApdPlatformId(String platformId);

    public Mono<AlSudaisPlatformDetail> findByApdPlatformNameIgnoreCase(String platformName);

    Flux<AlSudaisPlatformDetail> findAllBy(Pageable pageable);

}
