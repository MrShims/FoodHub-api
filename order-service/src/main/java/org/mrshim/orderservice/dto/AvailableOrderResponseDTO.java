package org.mrshim.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableOrderResponseDTO {
    private Long id;

    private LocalDateTime updateDate;

    private List<AvailableOrderResponseListDTO> LineDishes;
}
