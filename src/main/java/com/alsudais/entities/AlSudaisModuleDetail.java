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
@Table(value = "identity_access_management.alsudais_module_detail")
@ReactiveAuditQuery(name = "module_detail_audit", query = "SELECT * FROM identity_access_management.alsudais_module_detail_aud WHERE amd_module_id = :module_id")
public class AlSudaisModuleDetail {
    @Id
    @Column(value = "amd_seq_id")
    private Long amdSeqId;
    @Column(value = "amd_module_id")
    private String amdModuleId;
    @Column(value = "amd_module_name")
    private String amdModuleName;
    @Column(value = "amd_parent_module_id")
    private String amdParentModuleId;
    @Column(value = "amd_sort_order")
    private Integer amdSortOrder;
    @Column(value = "amd_status")
    private  String amdStatus;
    @CreatedBy
    @Column(value = "amd_created_by")
    private String amdCreatedBy;
    @CreatedDate
    @Column(value = "amd_created_date")
    private LocalDateTime amdCreatedDate;
    @LastModifiedBy
    @Column(value = "amd_modified_by")
    private String amdModifiedBy;
    @LastModifiedDate
    @Column(value = "amd_modified_date")
    private LocalDateTime amdModifiedDate;
}