package com.bazlur.backgroundjobs.repository;

import com.bazlur.backgroundjobs.domain.Job;
import com.bazlur.backgroundjobs.domain.JobStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Repository
public interface JobQueueRepository extends ReactiveCrudRepository<Job, String> {

	Flux<Job> findAllByOrderByCreatedDateTimeDesc();

	@Query("SELECT * FROM jobs WHERE status IN (:status) ORDER by created_date_time DESC LIMIT 10")
	Flux<Job> findTop10(Collection<JobStatus> jobStatuses);
}
