package com.example.clashadvisorapi.dto;

import lombok.Data; //auto-génère getters, setters, toString, etc.

@Data
public class PlayerDto {
    private String name;
    private int townHallLevel;
    private int expLevel;
    private int trophies;
    private String clanName;
    private int warStars;
    private String leagueName;
    private String leagueIconUrl;
    private int clanLevel;
}