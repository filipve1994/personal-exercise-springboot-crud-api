package com.filipve1994.personalexercisespringbootcrudapi.persistence.auditing;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Just return a string representing the username
        return Optional.ofNullable("FVE").filter(s -> !s.isEmpty());
    }
}
