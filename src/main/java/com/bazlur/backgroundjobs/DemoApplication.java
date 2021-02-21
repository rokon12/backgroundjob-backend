package com.bazlur.backgroundjobs;

import com.bazlur.backgroundjobs.service.FakeDataGenerator;
import com.bazlur.backgroundjobs.service.JobQueueService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public DemoApplication(JobQueueService jobQueueService, FakeDataGenerator fakeDataGenerator) {
		this.jobQueueService = jobQueueService;
		this.fakeDataGenerator = fakeDataGenerator;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private final JobQueueService jobQueueService;
	private final FakeDataGenerator fakeDataGenerator;

	@Override
	public void run(String... args) {
		jobQueueService.findAll().count().blockOptional()
						.ifPresent(value -> {
							if (value < 1) {
								fakeDataGenerator.generate(100);
							}
						});
	}
}
