package com.itis.oris.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImportProduct {
    private Product product;
    private Integer count;
    private String supplier;
}
