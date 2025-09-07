package com.shuttletime.model.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private int amount;
    private String currency;
}
