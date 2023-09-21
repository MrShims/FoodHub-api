package org.mrshim.transactionalservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyResponse {

    @JsonProperty("Valute")
    private Map<String, Currency> valute;

    public Map<String, Currency> getValute() {
        return valute;
    }

    public void setValute(Map<String, Currency> valute) {
        this.valute = valute;
    }

    @ToString
    public static class Currency {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private Double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }
}
