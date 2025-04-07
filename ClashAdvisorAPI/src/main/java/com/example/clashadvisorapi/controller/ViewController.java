package com.example.clashadvisorapi.controller;

import com.example.clashadvisorapi.dto.PlayerDto;
import com.example.clashadvisorapi.service.ClashService;
import org.springframework.beans.factory.annotation.Autowired;
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
        PlayerDto player = clashService.getPlayerInfo(tag);
        model.addAttribute("player", player);
        return "village-info";  // doit pointer vers templates/village-info.html
    }
}
