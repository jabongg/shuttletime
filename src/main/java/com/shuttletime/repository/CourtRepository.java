package com.shuttletime.repository;

import com.shuttletime.model.entity.BadmintonCourt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<BadmintonCourt, Long> {
}
