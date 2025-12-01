package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.response.AdminStatsResponseDTO;
import com.ViviEstu.service.AdminStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsResponseDTO> getGeneralStats() {
        AdminStatsResponseDTO stats = adminStatsService.getGeneralStats();
        return ResponseEntity.ok(stats);
    }
}