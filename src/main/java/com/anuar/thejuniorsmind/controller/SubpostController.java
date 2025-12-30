package com.anuar.thejuniorsmind.controller;

import com.anuar.thejuniorsmind.dto.SubpostRequestDTO;
import com.anuar.thejuniorsmind.dto.SubpostResponseDTO;
import com.anuar.thejuniorsmind.service.SubpostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subposts")
@RequiredArgsConstructor
public class SubpostController {

    private final SubpostService subpostService;

    @PostMapping
    public ResponseEntity<SubpostResponseDTO> createSubpost(@Valid @RequestBody SubpostRequestDTO request) {
        SubpostResponseDTO created = subpostService.createSubpost(request);

        return ResponseEntity.created(URI.create("/api/subposts/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubpostResponseDTO> getSubpostById(@PathVariable Long id) {
        SubpostResponseDTO subpost = subpostService.getSubpostById(id);

        return ResponseEntity.ok(subpost);
    }

    @GetMapping
    public ResponseEntity<List<SubpostResponseDTO>> getAllSubposts() {
        List<SubpostResponseDTO> subposts = subpostService.getAllSubposts();

        return ResponseEntity.ok(subposts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubpostResponseDTO> updateSubpost(@PathVariable Long id,
                                                            @Valid @RequestBody SubpostRequestDTO request) {
        SubpostResponseDTO updated = subpostService.updateSubpost(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubpost(@PathVariable Long id) {
        subpostService.deleteSubpost(id);

        return ResponseEntity.noContent().build();
    }

}
