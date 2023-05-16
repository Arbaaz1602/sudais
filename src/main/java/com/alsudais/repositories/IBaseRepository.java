package com.alsudais.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@NoRepositoryBean
@Transactional
public interface IBaseRepository<T, ID extends Serializable> extends R2dbcRepository<T, ID>, Serializable { //ReactiveQueryByExampleExecutor<T>, ReactiveQuerydslPredicateExecutor<T>

}
