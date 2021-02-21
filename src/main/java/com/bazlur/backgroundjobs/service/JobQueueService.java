package com.bazlur.backgroundjobs.service;

import com.bazlur.backgroundjobs.domain.Job;
import com.bazlur.backgroundjobs.domain.JobStatus;
import com.bazlur.backgroundjobs.repository.JobQueueRepository;
import com.bazlur.backgroundjobs.web.JobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class JobQueueService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobQueueService.class);

	private final JobQueueRepository repository;

	public JobQueueService(JobQueueRepository repository) {
		this.repository = repository;
	}

	public Mono<?> enqueue(JobDTO jobDTO) {

		return repository.save(Job.builder()
						.jobName(jobDTO.getJobName())
						.uuid(UUID.randomUUID().toString())
						.callbackUrl(jobDTO.getCallbackUrl())
						.payload(jobDTO.getPayload())
						.status(JobStatus.QUEUED)
						.build());
	}

	public Flux<JobDTO> findAll() {

		return repository.findAllByOrderByCreatedDateTimeDesc()
						.map(job -> JobDTO.builder()
						.uuid(job.getUuid())
						.status(job.getStatus())
						.payload(job.getPayload())
						.callbackUrl(job.getCallbackUrl())
						.jobName(job.getJobName()).build());
	}
}
