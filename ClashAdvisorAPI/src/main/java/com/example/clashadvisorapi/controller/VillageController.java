package com.example.clashadvisorapi.controller;

import com.example.clashadvisorapi.service.ClashService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/village")
public class VillageController {

    private final ClashService clashService;

    public VillageController(ClashService clashService) {
        this.clashService = clashService;
    }

    @GetMapping("/info/{tag}")
    public ResponseEntity<?> getVillageInfo(@PathVariable String tag) {
        return ResponseEntity.ok(clashService.getPlayerInfo(tag));
    }
}

