package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisModuleDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface IAlSudaisModuleDetailRepository extends IBaseRepository<AlSudaisModuleDetail, Long> {

    public Mono<AlSudaisModuleDetail> findByAmdModuleId(String moduleId);

    public Mono<AlSudaisModuleDetail> findByAmdModuleNameIgnoreCase(String moduleName);

    public Flux<AlSudaisModuleDetail> findByAmdModuleIdIn(Set<String> moduleIds);

}