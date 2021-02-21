package com.bazlur.backgroundjobs.web;

import com.bazlur.backgroundjobs.service.JobQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin(value = {"http://localhost:3000"})
@RestController
public class JobController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

	private final JobQueueService jobQueueService;

	public JobController(JobQueueService jobQueueService) {
		this.jobQueueService = jobQueueService;
	}

	@PostMapping("/jobs")
	@ResponseStatus(HttpStatus.OK)
	public Mono<?> submitJob(@RequestBody JobDTO job) {
		LOGGER.info("Received request from client with: {}", job);

		return jobQueueService.enqueue(job);
	}

	@GetMapping("/jobs")
	@ResponseStatus(HttpStatus.OK)
	public Flux<JobDTO> findAll() {

		return jobQueueService.findAll();
	}
}
