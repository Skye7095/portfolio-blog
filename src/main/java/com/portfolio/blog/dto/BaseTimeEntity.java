package com.portfolio.blog.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들(createDate, modifiedDate)도 컬럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class) // BaseTiemEntity클래스에 Auditing 기능 포함
public class BaseTimeEntity {
	
	@CreatedDate // Entity 저장 시 시간 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 조회한  Entity 값을 변경할 때 시간이 자동 저장
    private LocalDateTime updatedAt;
}
