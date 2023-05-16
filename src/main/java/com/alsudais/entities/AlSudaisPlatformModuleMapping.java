package com.alsudais.entities;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.annotations.ReactiveAudited;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ReactiveAudited
@Table(value = "identity_access_management.alsudais_platform_module_mapping")
@ReactiveAuditQuery(name = "platform_module_mapping_audit", query = "SELECT * FROM identity_access_management.alsudais_platform_module_mapping_aud WHERE apmm_platform_id = :platform_id")
public class AlSudaisPlatformModuleMapping {

    @Id
    @Column(value = "apmm_seq_id")
    private Long apmmSeqId;
    @Column(value = "apmm_platform_id")
    private String apmmPlatformId;
    @Column(value = "apmm_module_id")
    private String apmmModuleId;
    @Column(value = "apmm_status")
    private String apmmStatus;
    @CreatedBy
    @Column(value = "apmm_created_by")
    private String apmmCreatedBy;
    @CreatedDate
    @Column(value = "apmm_created_date")
    private LocalDateTime apmmCreatedDate;
    @LastModifiedBy
    @Column(value = "apmm_modified_by")
    private String apmmModifiedBy;
    @LastModifiedDate
    @Column(value = "apmm_modified_date")
    private LocalDateTime apmmModifiedDate;

}
