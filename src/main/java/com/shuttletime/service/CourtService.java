package com.shuttletime.service;

import com.shuttletime.model.entity.BadmintonCourt;
import com.shuttletime.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    private final CourtRepository courtRepository;

    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public List<BadmintonCourt> getAllCourts() {
        return courtRepository.findAll();
    }
}
