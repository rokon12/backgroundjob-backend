package com.bazlur.backgroundjobs.service;

import com.bazlur.backgroundjobs.domain.Job;
import com.bazlur.backgroundjobs.domain.JobStatus;
import com.bazlur.backgroundjobs.repository.JobQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class JobScheduler {

	private final Random random = new Random();
	private final JobQueueRepository jobQueueRepository;
	private final RestTemplate restTemplate;

	public JobScheduler(JobQueueRepository jobQueueRepository, RestTemplate restTemplate) {
		this.jobQueueRepository = jobQueueRepository;
		this.restTemplate = restTemplate;
	}

	@Scheduled(fixedRateString = "${job.scheduler.repeat}")
	public void runBackgroundJob() {
		log.info("running background job");

		jobQueueRepository.findTop10(List.of(JobStatus.QUEUED, JobStatus.FAILED)).log()
						.flatMap(this::process)
						.flatMap(this::callBack)
						.subscribeOn(Schedulers.boundedElastic())
						.subscribe();
	}

	private Mono<Job> process(Job job) {

		return Mono.just(job)
						.flatMap(j -> updateStatus(j, JobStatus.STARTED))
						.map(this::failToProcess30PercentOfTheTime)
						.flatMap(j -> updateStatus(j, JobStatus.COMPLETED))
						.onErrorResume(throwable -> updateStatus(job, JobStatus.FAILED))
						.delayElement(Duration.ofSeconds(1));//Let's assume it takes 1 seconds to process
	}

	private Job failToProcess30PercentOfTheTime(Job job) {
		var value = random.nextInt(10);
		if (value < 4) {
			throw new RuntimeException("Job Failed");
		}
		return job;
	}

	private Mono<Job> updateStatus(Job job, JobStatus jobStatus) {
		job.setStatus(jobStatus);
		return jobQueueRepository.save(job);
	}

	private Mono<HttpStatus> callBack(Job job) {
		var callbackUrl = job.getCallbackUrl() + "?status=" + job.getStatus();
		var entity = new RequestEntity<>(HttpMethod.GET, URI.create(callbackUrl));

		return Mono.fromCallable(() -> restTemplate.exchange(entity, String.class)
						.getStatusCode())
						.onErrorReturn(HttpStatus.BAD_REQUEST);
	}
}
