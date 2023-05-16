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
@Table(value = "identity_access_management.alsudais_user_role_mapping")
@ReactiveAuditQuery(name = "user_role_mapping_audit", query = "SELECT * FROM identity_access_management.alsudais_user_role_mapping WHERE aurm_user_id = :user_id")
public class AlSudaisUserRoleMapping {
    @Id
    @Column(value = "aurm_seq_id")
    private Long aurmSeqId;
    @Column(value = "aurm_user_id")
    private String aurmUserId;
    @Column(value = "aurm_role_id")
    private String aurmRoleId;
    @Column(value = "aurm_status")
    private String aurmStatus;
    @CreatedBy
    @Column(value = "aurm_created_by")
    private String aurmCreatedBy;
    @CreatedDate
    @Column(value = "aurm_created_date")
    private LocalDateTime aurmCreatedDate;
    @LastModifiedBy
    @Column(value = "aurm_modified_by")
    private String aurmModifiedBy;
    @LastModifiedDate
    @Column(value = "aurm_modified_date")
    private LocalDateTime aurmModifiedDate;
}