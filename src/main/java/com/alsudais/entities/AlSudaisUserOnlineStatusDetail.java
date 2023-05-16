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
@Table(value = "identity_access_management.alsudais_user_online_status_detail")
@ReactiveAuditQuery(name = "user_online_status_audit", query = "SELECT * FROM identity_access_management.alsudais_user_online_status_detail_aud WHERE auad_user_id = :user_id")
public class AlSudaisUserOnlineStatusDetail {

    @Id
    @Column(value = "auad_seq_id")
    private Long auadSeqId;

    @Column(value = "auad_user_id")
    private String auadUserId;

    @Column(value = "auad_status")
    private String auadStatus;

    @CreatedBy
    @Column(value = "auad_created_by")
    private String auadCreatedBy;
    @CreatedDate
    @Column(value = "auad_created_date")
    private LocalDateTime auadCreatedDate;
    @LastModifiedBy
    @Column(value = "auad_modified_by")
    private String auadModifiedBy;
    @LastModifiedDate
    @Column(value = "auad_modified_date")
    private LocalDateTime auadModifiedDate;

}
