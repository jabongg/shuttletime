package com.shuttletime.service;

import com.shuttletime.model.entity.Venue;
import com.shuttletime.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> getAllVenues() {
        List<Venue> venues = venueRepository.findAll();

        // printing venue location
        venues.forEach(v -> {
            System.out.println("Venue: " + v.getName() + " | Location: " + v.getLocation());
        });
        return venues;
    }
}

