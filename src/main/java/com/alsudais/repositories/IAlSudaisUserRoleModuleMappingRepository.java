package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisUserRoleModuleMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;


@Repository
public interface IAlSudaisUserRoleModuleMappingRepository extends IBaseRepository<AlSudaisUserRoleModuleMapping, Long> {

    public Flux<AlSudaisUserRoleModuleMapping> findByAurmmUserIdAndAurmmRoleId(String userId, String roleId);

    public Flux<AlSudaisUserRoleModuleMapping> findByAurmmUserId(String userId);

    public Flux<AlSudaisUserRoleModuleMapping> findByAurmmRoleId(String roleId);

    public Flux<AlSudaisUserRoleModuleMapping> findAllBy(Pageable pageable);

    public Mono<Long> deleteByAurmmRoleId(String roleId);

    public Mono<Long> deleteByAurmmUserId(String userId);

    public Flux<AlSudaisUserRoleModuleMapping> findByAurmmRoleIdInAndAurmmModuleIdNotIn(Set<String> roleIds, Set<String> moduleIds);

    public Flux<AlSudaisUserRoleModuleMapping> findByAurmmRoleIdIn(Set<String> roleIds);

}
