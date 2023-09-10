package org.mrshim.menuservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "dish")
public class Dish {

    @Id
    private String id;
    private String name;
    private List<String> ingredients;
    private BigDecimal price;
    private String description;






}
