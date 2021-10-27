package com.assignment.jpaAuditingConfig;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		// return Optional.of(System.getProperty("User.name"));
		return Optional.of("Admin");
	}

}
