package com.shuttletime.controller;

import com.shuttletime.model.entity.BadmintonCourt;
import com.shuttletime.service.CourtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courts")
@CrossOrigin(
        origins = {
                "http://localhost:5173",              // Local React dev frontend
                "https://shuttle-ui.vercel.app"      // Vercel frontend
        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class CourtController {

    @Autowired
    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    @Operation(summary = "Get all courts with their venues")
    public ResponseEntity<List<BadmintonCourt>> getAllCourts() {
        return ResponseEntity.ok(courtService.getAllCourts());
    }
}
