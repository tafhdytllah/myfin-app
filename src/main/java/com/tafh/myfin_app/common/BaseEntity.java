package com.tafh.myfin_app.common;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class BaseEntity {

  @Id
  @Column(nullable = false, updatable = false, unique = true, length = 64)
  private String id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by", nullable = false, updatable = false)
  private String updatedBy;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  private String deletedBy;

  public boolean isDeleted() {
    return deletedAt != null;
  }

}
