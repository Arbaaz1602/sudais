package com.alsudais.grpc.services.impl;

import com.grpc.iam.ReactorIAMGrpcServiceGrpc;
import com.grpc.iam.SignInRequest;
import com.grpc.iam.SignInResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;


@GrpcService
@RequiredArgsConstructor
public class IAMGrpcServiceImpl extends ReactorIAMGrpcServiceGrpc.IAMGrpcServiceImplBase {

    @Override
    public Mono<SignInResponse> signIn(SignInRequest request) {
        return Mono.just(SignInResponse.newBuilder().setEmail("Susmit.khot@gamil.com").setToken("Token1234").build());
    }
}