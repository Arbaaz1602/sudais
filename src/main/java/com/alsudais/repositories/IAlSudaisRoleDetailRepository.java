package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisRoleDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface IAlSudaisRoleDetailRepository extends IBaseRepository<AlSudaisRoleDetail, Long> {

    public Mono<AlSudaisRoleDetail> findByArdRoleId(String roleId);

    public Flux<AlSudaisRoleDetail> findByArdRoleIdIn(Set<String> roleIds);

    public Mono<AlSudaisRoleDetail> findByArdRoleNameIgnoreCase(String roleName);

    Flux<AlSudaisRoleDetail> findAllBy(Pageable pageable);
}
