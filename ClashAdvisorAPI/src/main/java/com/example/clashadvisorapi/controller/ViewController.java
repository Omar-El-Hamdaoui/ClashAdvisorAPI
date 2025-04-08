package com.example.clashadvisorapi.controller;

import com.example.clashadvisorapi.dto.PlayerDto;
import com.example.clashadvisorapi.service.ClashService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/village")
public class ViewController {

    @Autowired
    private ClashService clashService;

    @GetMapping("/view/{tag}")
    public String showVillageHtml(@PathVariable String tag, Model model) {
        System.out.println(">>> TAG REÇU = " + tag);  // 👈 pour log

        PlayerDto player = clashService.getPlayerInfo(tag);
        model.addAttribute("player", player);

        return "village-info";  // doit pointer vers templates/village-info.html
    }
    @GetMapping("/export/{tag}")
    public ResponseEntity<Resource> exportPlayer(@PathVariable String tag) {
        Resource resource = (Resource) clashService.exportPlayerAsCsv(tag);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"player.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}
