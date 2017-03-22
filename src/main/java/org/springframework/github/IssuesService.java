package org.springframework.github;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
class IssuesService {
	private final IssuesRepository repository;
	private final GaugeService gaugeService;

	IssuesService(IssuesRepository repository, GaugeService gaugeService) {
		this.repository = repository;
		this.gaugeService = gaugeService;
	}

	void save(String user, String repo) {
		this.repository.save(new Issues(user, repo));
		countIssues();
	}

	private void countIssues() {
		this.gaugeService.submit("issues.count", count());
	}

	List<IssueDto> allIssues() {
		List<IssueDto> dtos = new ArrayList<>();
		this.repository.findAll().forEach(i -> dtos.add(new IssueDto(i.getUsername(), i.getRepository())));
		return dtos;
	}

	long count() {
		return this.repository.count();
	}

	void deleteAll() {
		this.repository.deleteAll();
		countIssues();
	}

}

