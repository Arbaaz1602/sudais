package com.alsudais.entities;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.annotations.ReactiveAudited;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(value = "identity_access_management.alsudais_user_code_sequence")
public class AlSudaisUserCodeSequence {

    @Id
    @Column(value = "aucs_seq_id")
    private Long aucsSeqId;
    @Column(value = "aucs_identifier")
    private String aucsIdentifier;
    @Column(value = "aucs_counter")
    private Integer aucsCounter;

}
