package org.pablofsc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class SimulacaoResponseTest {

  @Test
  public void deveCriarSimulacaoResponseComValoresNormais() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(1, 1000.0, 50.0, 100.0, 950.0));

    SimulacaoResponse sr = new SimulacaoResponse(produto, 1000.0, 12, 1.0, 1200.0, 100.0, memoria);

    assertEquals(produto, sr.produto);
    assertEquals(1000.0, sr.valorSolicitado);
    assertEquals(12, sr.prazoMeses);
    assertEquals(1.0, sr.taxaJurosEfetivaMensal);
    assertEquals(1200.0, sr.valorTotalComJuros);
    assertEquals(100.0, sr.parcelaMensal);
    assertEquals(memoria, sr.memoriaCalculo);
  }

  @Test
  public void deveCriarSimulacaoResponseComValoresZero() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Zero";
    produto.taxaJurosAnual = 0.0;
    produto.prazoMaximoMeses = 1;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(0, 0.0, 0.0, 0.0, 0.0));

    SimulacaoResponse sr = new SimulacaoResponse(produto, 0.0, 0, 0.0, 0.0, 0.0, memoria);

    assertEquals(produto, sr.produto);
    assertEquals(0.0, sr.valorSolicitado);
    assertEquals(0, sr.prazoMeses);
    assertEquals(0.0, sr.taxaJurosEfetivaMensal);
    assertEquals(0.0, sr.valorTotalComJuros);
    assertEquals(0.0, sr.parcelaMensal);
    assertEquals(memoria, sr.memoriaCalculo);
  }

  @Test
  public void deveCriarSimulacaoResponseComValoresNegativos() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Negativo";
    produto.taxaJurosAnual = -5.0;
    produto.prazoMaximoMeses = -1;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(-1, -1000.0, -50.0, -100.0, -950.0));

    SimulacaoResponse sr = new SimulacaoResponse(produto, -1000.0, -12, -1.0, -1200.0, -100.0, memoria);

    assertEquals(produto, sr.produto);
    assertEquals(-1000.0, sr.valorSolicitado);
    assertEquals(-12, sr.prazoMeses);
    assertEquals(-1.0, sr.taxaJurosEfetivaMensal);
    assertEquals(-1200.0, sr.valorTotalComJuros);
    assertEquals(-100.0, sr.parcelaMensal);
    assertEquals(memoria, sr.memoriaCalculo);
  }
}
