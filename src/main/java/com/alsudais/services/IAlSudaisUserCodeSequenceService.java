package com.alsudais.services;

import reactor.core.publisher.Mono;

public interface IAlSudaisUserCodeSequenceService {

    public Mono<?> fetchUserCodeSequenceByIdentifier(String id);

}
