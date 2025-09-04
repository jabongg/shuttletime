package com.shuttletime.controller;

import com.shuttletime.model.entity.Venue;
import com.shuttletime.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/venues")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class VenueController {

    @Autowired
    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Operation(
            summary = "Get all venues",
            description = "Fetches all venues along with their courts." +
                    "One extra thing: If you later fetch /venues, you may face infinite recursion in JSON.\n" +
                    "To fix that, add Jackson annotations"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of venues fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }
}
