package com.alsudais.feign.clients;

import com.alsudais.beans.CompanyShortInfoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@ReactiveFeignClient(value = "IAlSudaisContractEntitiesReactiveFeignClient", url = "${alsudais.contract.entities.service.url:https://central.alpha.sudaislogistics.com/contract-entities}")
public interface IAlSudaisContractEntitiesReactiveFeignClient {

    @GetMapping(value = "/companies/list-details")
    public Flux<CompanyShortInfoBean> fetchCompaniesById(@PathVariable(value = "company_ids") Set<Long> companyId);

}
