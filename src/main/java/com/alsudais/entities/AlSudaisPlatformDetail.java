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
@Table(value = "identity_access_management.alsudais_platform_detail")
@ReactiveAuditQuery(name = "platform_detail_audit", query = "SELECT * FROM identity_access_management.alsudais_platform_detail_aud WHERE apd_platform_id = :platform_id")
public class AlSudaisPlatformDetail {

    @Id
    @Column(value = "apd_seq_id")
    private Long apdSeqId;

    @Column(value = "apd_platform_id")
    private String apdPlatformId;
    @Column(value = "apd_platform_name")
    private String apdPlatformName;

    @Column(value = "apd_status")
    private String apdStatus;
    @CreatedBy
    @Column(value = "apd_created_by")
    private String apdCreatedBy;
    @CreatedDate
    @Column(value = "apd_created_date")
    private LocalDateTime apdCreatedDate;
    @LastModifiedBy
    @Column(value = "apd_modified_by")
    private String apdModifiedBy;
    @LastModifiedDate
    @Column(value = "apd_modified_date")
    private LocalDateTime apdModifiedDate;

}
