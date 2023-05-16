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
@Table(value = "identity_access_management.alsudais_role_detail")
@ReactiveAuditQuery(name = "role_detail_audit", query = "SELECT * FROM identity_access_management.alsudais_role_detail_aud WHERE ard_role_id = :role_id")
public class AlSudaisRoleDetail {
    @Id
    @Column(value = "ard_seq_id")
    private Long ardSeqId;
    @Column(value = "ard_role_id")
    private String ardRoleId;
    @Column(value = "ard_role_name")
    private String ardRoleName;
    @Column(value = "ard_platform_name")
    private String ardPlatformName;
    @Column(value = "ard_status")
    private String ardStatus;
    @CreatedBy
    @Column(value = "ard_created_by")
    private String ardCreatedBy;
    @CreatedDate
    @Column(value = "ard_created_date")
    private LocalDateTime ardCreatedDate;
    @LastModifiedBy
    @Column(value = "ard_modified_by")
    private String ardModifiedBy;
    @LastModifiedDate
    @Column(value = "ard_modified_date")
    private LocalDateTime ardModifiedDate;
}