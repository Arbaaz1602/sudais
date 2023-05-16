package com.alsudais.entities;

import com.alsudais.annotations.ReactiveAudited;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ReactiveAudited
@Table(value = "identity_access_management.alsudais_user_role_module_mapping")
public class AlSudaisUserRoleModuleMapping {
    @Id
    @Column(value = "aurmm_seq_id")
    private Long aurmmSeqId;
    @Column(value = "aurmm_user_id")
    private String aurmmUserId;
    @Column(value = "aurmm_role_id")
    private String aurmmRoleId;
    @Column(value = "aurmm_module_id")
    private String aurmmModuleId;
    @Column(value = "aurmm_parent_module_id")
    private String aurmmParentModuleId;
    @Column(value = "aurmm_full_control")
    private Boolean aurmmFullControl;
    @Column(value = "aurmm_modify")
    private Boolean aurmmModify;
    @Column(value = "aurmm_read_and_execute")
    private Boolean aurmmReadAndExecute;
    @Column(value = "aurmm_approver")
    private Boolean aurmmApprover;
    @Column(value = "aurmm_read")
    private Boolean aurmmRead;
    @Column(value = "aurmm_write")
    private Boolean aurmmWrite;
    @Column(value = "aurmm_no_access")
    private Boolean aurmmNoAccess;
    @Column(value = "aurmm_remark")
    private String aurmmRemark;
    @Column(value = "aurmm_status")
    private String aurmmStatus;
    @CreatedBy
    @Column(value = "aurmm_created_by")
    private String aurmmCreatedBy;
    @CreatedDate
    @Column(value = "aurmm_created_date")
    private LocalDateTime aurmmCreatedDate;
    @LastModifiedBy
    @Column(value = "aurmm_modified_by")
    private String aurmmModifiedBy;
    @LastModifiedDate
    @Column(value = "aurmm_modified_date")
    private LocalDateTime aurmmModifiedDate;
}