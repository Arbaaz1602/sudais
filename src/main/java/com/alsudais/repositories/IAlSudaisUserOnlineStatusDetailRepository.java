package com.alsudais.repositories;

import com.alsudais.entities.AlSudaisUserOnlineStatusDetail;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IAlSudaisUserOnlineStatusDetailRepository extends IBaseRepository<AlSudaisUserOnlineStatusDetail, Long>{

    public Mono<AlSudaisUserOnlineStatusDetail> findByAuadUserId(String userId);

}
