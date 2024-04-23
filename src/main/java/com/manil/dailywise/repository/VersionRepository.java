package com.manil.dailywise.repository;

import com.manil.dailywise.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long> {
    Version findFirstByIsPublicOrderByCreatedAtDesc(Boolean isPublic);
    Version findByVersion(String version);
}
