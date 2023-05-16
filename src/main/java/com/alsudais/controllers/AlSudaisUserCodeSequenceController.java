package com.alsudais.controllers;

import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisUserCodeSequenceService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "${iam-service-version}/user-code-sequence")
public class AlSudaisUserCodeSequenceController {

    @Autowired
    private IAlSudaisUserCodeSequenceService alSudaisUserCodeSequenceService;

    @GetMapping(value = "/{id}")
    public Mono<?> fetchUserCodeSequenceByIdentifier(@PathVariable(value = "id") @NotBlank(message = LocaleMessageCodeConstants.USER_CODE_SEQUENCE_ID_CANT_EMPTY) String id){
        return this.alSudaisUserCodeSequenceService.fetchUserCodeSequenceByIdentifier(id);
    }

}
