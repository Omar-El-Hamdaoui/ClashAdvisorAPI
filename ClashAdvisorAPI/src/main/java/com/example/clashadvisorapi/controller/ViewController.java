package com.example.clashadvisorapi.controller;

import com.example.clashadvisorapi.dto.PlayerDto;
import com.example.clashadvisorapi.service.ClashService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequestMapping("/village")
public class ViewController {

    @Autowired
    private ClashService clashService;

    @GetMapping("/view/{tag}")
    public String showVillageHtml(@PathVariable String tag, Model model) {
        PlayerDto player = clashService.getPlayerInfo(tag);
        model.addAttribute("player", player);

        return "village-info";  // doit pointer vers templates/village-info.html
    }


    @GetMapping("/export/{tag}")
    public ResponseEntity<Resource> exportPlayer(@PathVariable String tag) {
        Resource csvResource = (Resource) clashService.exportPlayerAsCsv(tag);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=player.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvResource);
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    @PostMapping("/search")
    public String redirectToVillage(@RequestParam String tag) {
        if (!tag.startsWith("#")) {
            tag = "#" + tag;
        }
        String encodedTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
        return "redirect:/village/view/" + encodedTag;
    }

    @GetMapping("/search")
    public String searchPlayer(@RequestParam("tag") String tag, Model model) {
        try {
            PlayerDto player = clashService.getPlayerInfo(tag);
            model.addAttribute("player", player);
        } catch (Exception e) {
            model.addAttribute("error", "Joueur introuvable ou erreur d'API.");
        }
        return "index"; // Revenir sur la page de recherche avec les données
    }




}
