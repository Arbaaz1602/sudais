package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisUserMaster;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface IAlSudaisUserMasterRepository extends IBaseRepository<AlSudaisUserMaster, Long> { //RevisionRepository<AlSudaisUserMaster, Long, Long>

	public Mono<AlSudaisUserMaster> findByAumEmailIdIgnoreCase(String emailId);

	public Mono<AlSudaisUserMaster> findByAumEmailIdIgnoreCaseOrAumUserCodeIgnoreCase(String emailId, String userCode);

	public Mono<AlSudaisUserMaster> findByAumUserId(String userId);

	public Flux<AlSudaisUserMaster> findAllBy(Pageable pageable);

	public Flux<AlSudaisUserMaster> findByAumUserCodeStartsWithIgnoreCase(String tenantId);

}