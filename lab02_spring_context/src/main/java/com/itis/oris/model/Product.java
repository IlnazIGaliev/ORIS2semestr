package com.itis.oris.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private String articleNumber;
    private Category category;
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(articleNumber, product.articleNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(articleNumber);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", articleNumber='" + articleNumber + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }
}
