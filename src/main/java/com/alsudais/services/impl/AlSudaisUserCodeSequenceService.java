package com.alsudais.services.impl;

import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisUserCodeSequence;
import com.alsudais.mappers.entity.pojo.IAlSudaisUserCodeSequenceEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserCodeSequencePojoEntityMapper;
import com.alsudais.repositories.IAlSudaisUserCodeSequenceRepository;
import com.alsudais.services.IAlSudaisUserCodeSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Duration;

@Service
@Transactional
public class AlSudaisUserCodeSequenceService implements IAlSudaisUserCodeSequenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisUserCodeSequence.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisUserCodeSequenceRepository alSudaisUserCodeSequenceRepository;
    @Autowired
    private IAlSudaisUserCodeSequencePojoEntityMapper alSudaisUserCodeSequencePojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Override
    public Mono<?> fetchUserCodeSequenceByIdentifier(String id) {
        return this.alSudaisUserCodeSequenceRepository.findByAucsIdentifier(id)
                .flatMap(alSudaisUserCodeSequence -> {
                    alSudaisUserCodeSequence.setAucsCounter(alSudaisUserCodeSequence.getAucsCounter() + BigInteger.ONE.intValue());
                    return Mono.just(alSudaisUserCodeSequence);
                })
                .switchIfEmpty(Mono.defer(() -> Mono.just(AlSudaisUserCodeSequence.builder().aucsIdentifier(id).aucsCounter(BigInteger.ONE.intValue()).build())))
                .flatMap(this.alSudaisUserCodeSequenceRepository::save)
                .map(IAlSudaisUserCodeSequenceEntityPojoMapper.INSTANCE::mapUserCodeSequenceEntityPojoMapping)
                .map(userCodeSequenceResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userCodeSequenceResponseBean).build());
    }

}
