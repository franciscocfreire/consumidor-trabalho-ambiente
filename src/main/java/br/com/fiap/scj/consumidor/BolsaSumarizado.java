package br.com.fiap.scj.consumidor;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class BolsaSumarizado {

    private String uf;
    private int qtdBolsa;
    private BigDecimal valorParcela;

    public void adicionaBolsa() {
        this.qtdBolsa++;
    }
}
