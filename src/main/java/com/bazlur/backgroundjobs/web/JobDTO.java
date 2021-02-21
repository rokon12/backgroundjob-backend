package com.bazlur.backgroundjobs.web;

import com.bazlur.backgroundjobs.domain.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
	private String uuid;
	private String jobName;
	private String payload;
	private JobStatus status;
	private String callbackUrl;
}
