package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisUserCodeSequence;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IAlSudaisUserCodeSequenceRepository extends IBaseRepository<AlSudaisUserCodeSequence, Long> {

    @Lock(value = LockMode.PESSIMISTIC_WRITE)
    @Query("SELECT * FROM identity_access_management.alsudais_user_code_sequence WHERE aucs_identifier = :id FOR UPDATE")
    public Mono<AlSudaisUserCodeSequence> findByAucsIdentifier(@Param("id") String id);

}
