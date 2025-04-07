package com.example.clashadvisorapi.controller;

import com.example.clashadvisorapi.service.ClashService;
import com.example.clashadvisorapi.dto.PlayerDto;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/village")
public class VillageController {

    private final ClashService clashService;

    public VillageController(ClashService clashService) {
        this.clashService = clashService;
    }

    @GetMapping("/view/{tag}")
    public String showVillageInfo(@PathVariable String tag, Model model) {
        PlayerDto player = clashService.getPlayerInfo(tag);
        model.addAttribute("player", player);
        return "village-info"; // ⇨ village-info.html dans /templates
    }
}

