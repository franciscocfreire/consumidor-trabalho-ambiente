package br.com.fiap.scj.consumidor;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Consumidor {

    @RabbitListener(queues = "fiap.scj.mensagens")
    public void recebeMessagem(String msg) throws ParseException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        String ufPesquisado = msg;



        System.out.println("Message recebida: " + msg );

        List<BolsaFamilia> listaBolsafamilida = new ArrayList<>();
        String csvFile = "D:\\Downloads\\201901_BolsaFamilia_Pagamentos\\201901_BolsaFamilia_PagamentosAt.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {


                // use comma as separator
                String[] bolsa = line.split(cvsSplitBy);
                //    "MÊS REFERÊNCIA";"MÊS COMPETÊNCIA";"UF";"CÓDIGO MUNICÍPIO SIAFI";"NOME MUNICÍPIO";"NIS FAVORECIDO";"NOME FAVORECIDO";"VALOR PARCELA"

                String valorSemPonto = bolsa[7].replace(".","");
                String valorParcelaFormat = valorSemPonto.replace(',','.');
                valorParcelaFormat = valorParcelaFormat.replace("\"","");
                BolsaFamilia b = BolsaFamilia.builder()
                        .mesReferencia(bolsa[0].replace("\"",""))
                        .mesCompetencia(bolsa[1].replace("\"",""))
                        .uf(bolsa[2].replace("\"",""))
                        .codigoMunicipio(bolsa[3].replace("\"",""))
                        .nomeMunicipio(bolsa[4].replace("\"",""))
                        .nisFavorecido(bolsa[5].replace("\"",""))
                        .nomeFavorecido(bolsa[6].replace("\"",""))
                        .valorParcela(new BigDecimal(valorParcelaFormat))
                        .build();

                listaBolsafamilida.add(b);
            }


            List<BolsaFamilia> listBolsaFiltrado = listaBolsafamilida.stream()
                    .filter(bolsa -> bolsa.getUf().equals(ufPesquisado)).collect(Collectors.toList());

            BigDecimal qtdValorParcelaSomada = listBolsaFiltrado.stream()
                    .map(BolsaFamilia::getValorParcela)
                    .reduce(BigDecimal::add)
                    .get();




            System.out.println("Qtd: " + listaBolsafamilida.size());
            System.out.println("Qtd Filtrada: " + listBolsaFiltrado.size());
            System.out.println("Qtd valor parcela somada " + NumberFormat.getCurrencyInstance().format(qtdValorParcelaSomada));

            BolsaSumarizado file = BolsaSumarizado.builder().qtdBolsa( listBolsaFiltrado.size())
                    .valorParcela(qtdValorParcelaSomada)
                    .uf(ufPesquisado)
                    .build();


            Writer writer = Files.newBufferedWriter(Paths.get("bolsaFamiliaSumarizado.csv"));
            StatefulBeanToCsv<BolsaSumarizado> beanToCsv = new StatefulBeanToCsvBuilder(writer).build();

            beanToCsv.write(file);

            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
