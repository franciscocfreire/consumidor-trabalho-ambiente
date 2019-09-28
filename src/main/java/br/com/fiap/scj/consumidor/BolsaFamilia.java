package br.com.fiap.scj.consumidor;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class BolsaFamilia {


    private String mesReferencia;
    private String mesCompetencia;
    private String uf;
    private String codigoMunicipio;
    private String nomeMunicipio;
    private String nisFavorecido;
    private String nomeFavorecido;
    private BigDecimal valorParcela;

//    "MÊS REFERÊNCIA";"MÊS COMPETÊNCIA";"UF";"CÓDIGO MUNICÍPIO SIAFI";"NOME MUNICÍPIO";"NIS FAVORECIDO";"NOME FAVORECIDO";"VALOR PARCELA"
}
