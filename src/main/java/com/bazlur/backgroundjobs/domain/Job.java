package com.bazlur.backgroundjobs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "jobs")
public class Job {
	@Id
	private String uuid;
	@Version
	private Long version;
	@Builder.Default
	@org.springframework.data.annotation.CreatedDate
	private LocalDateTime createdDateTime = LocalDateTime.now();
	@Builder.Default
	@org.springframework.data.annotation.LastModifiedDate
	private LocalDateTime lastUpdatedDateTime = LocalDateTime.now();
	private String jobName;
	private String payload;
	private JobStatus status;
	private String callbackUrl;
}
