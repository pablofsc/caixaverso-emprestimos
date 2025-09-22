package org.pablofsc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class SimulacaoResponseTest {

  @Test
  public void deveCriarSimulacaoResponseComValoresValidosEmprestimoComJuros() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(1, 1000.0, 50.0, 100.0, 950.0));
    memoria.add(new MemoriaCalculo(2, 950.0, 47.5, 102.5, 847.5));

    SimulacaoResponse sr = new SimulacaoResponse(produto, 1000.0, 12, 1.0, 1200.0, 100.0, memoria);

    assertEquals(produto, sr.produto);
    assertEquals(1000.0, sr.valorSolicitado);
    assertEquals(12, sr.prazoMeses);
    assertEquals(1.0, sr.taxaJurosEfetivaMensal);
    assertEquals(1200.0, sr.valorTotalComJuros);
    assertEquals(100.0, sr.parcelaMensal);
    assertEquals(memoria, sr.memoriaCalculo);

    assertTrue(sr.valorSolicitado > 0, "Valor solicitado deve ser positivo");
    assertTrue(sr.prazoMeses > 0, "Prazo deve ser positivo");
    assertTrue(sr.taxaJurosEfetivaMensal >= 0, "Taxa de juros não pode ser negativa");
    assertTrue(sr.valorTotalComJuros >= sr.valorSolicitado, "Valor total deve ser maior ou igual ao solicitado");
    assertTrue(sr.parcelaMensal > 0, "Parcela mensal deve ser positiva");
  }

  @Test
  public void deveValidarCoerenciaEntreValoresCalculados() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Teste";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = 24;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(1, 5000.0, 41.67, 208.33, 4791.67));
    memoria.add(new MemoriaCalculo(2, 4791.67, 39.93, 210.07, 4581.60));

    SimulacaoResponse sr = new SimulacaoResponse(produto, 5000.0, 24, 0.833, 6000.0, 250.0, memoria);

    double parcelasCalculadas = sr.parcelaMensal * sr.prazoMeses;
    assertEquals(sr.valorTotalComJuros, parcelasCalculadas, 0.01, "Valor total deve ser igual ao número de parcelas vezes o valor da parcela");

    assertTrue(sr.memoriaCalculo.size() > 0, "Deve ter pelo menos uma entrada na memória de cálculo");
    assertNotNull(sr.produto, "Produto não pode ser nulo");
  }
}
