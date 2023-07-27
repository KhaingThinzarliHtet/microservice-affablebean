package com.example.affablebeanui.model;

import com.fasterxml.jackson.annotation.JsonTypeId;

import java.time.LocalDateTime;

public record CartItem(
        Integer id,
        String name,
        double price,
        String description,
        int quantity,
        LocalDateTime lastUpdate

) {
}
