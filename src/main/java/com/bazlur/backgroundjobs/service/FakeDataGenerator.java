package com.bazlur.backgroundjobs.service;

import com.bazlur.backgroundjobs.domain.Job;
import com.bazlur.backgroundjobs.domain.JobStatus;
import com.bazlur.backgroundjobs.repository.JobQueueRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FakeDataGenerator {
	private final JobQueueRepository queueRepository;


	public FakeDataGenerator(JobQueueRepository queueRepository) {
		this.queueRepository = queueRepository;
	}

	public void generate(int count) {
		var fakeJobs = IntStream.rangeClosed(0, count)
						.mapToObj(value -> Job.builder()
										.uuid(UUID.randomUUID().toString())
										.jobName("Job#" + value)
										.payload("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus at.")
										.status(JobStatus.QUEUED)
										.callbackUrl("http://localhost:8091/callback")
										.build()).collect(Collectors.toList());

		queueRepository.saveAll(fakeJobs).subscribe();
	}
}
