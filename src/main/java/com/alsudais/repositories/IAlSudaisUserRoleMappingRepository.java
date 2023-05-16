package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisUserRoleMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface IAlSudaisUserRoleMappingRepository extends IBaseRepository<AlSudaisUserRoleMapping, Long> {

    public Flux<AlSudaisUserRoleMapping> findByAurmUserIdAndAurmRoleIdIn(String userId, Set<String> roleIds);

    public Mono<Long> deleteByAurmUserId(String userId);

    public Flux<AlSudaisUserRoleMapping> findByAurmUserId(String userId);

    public Mono<Long> deleteByAurmUserIdAndAurmRoleIdIn(String userId, Set<String> roleIds);
}
