package org.pablofsc.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;

@QuarkusTest
public class SimulacaoServiceTest {

  @Inject
  SimulacaoService simulacaoService;

  @Test
  @Transactional
  public void deveSimularEmprestimoComSucesso() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.0;
    produto.prazoMaximoMeses = 24;
    produto.persist();

    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = produto.id;
    request.valorSolicitado = 10000.0f;
    request.prazoMeses = 12;

    SimulacaoResponse response = simulacaoService.simular(request);

    assertNotNull(response);
    assertEquals(produto, response.produto);
    assertEquals(10000.0, response.valorSolicitado);
    assertEquals(12, response.prazoMeses);
    assertNotNull(response.taxaJurosEfetivaMensal);
    assertTrue(response.valorTotalComJuros > 10000.0);
    assertNotNull(response.parcelaMensal);
    assertNotNull(response.memoriaCalculo);
    assertEquals(12, response.memoriaCalculo.size());

    assertTrue(response.valorSolicitado > 0, "Valor solicitado deve ser positivo");
    assertTrue(response.prazoMeses > 0, "Prazo deve ser positivo");
    assertTrue(response.taxaJurosEfetivaMensal >= 0, "Taxa efetiva mensal não pode ser negativa");
    assertTrue(response.parcelaMensal > 0, "Parcela mensal deve ser positiva");
    assertTrue(response.valorTotalComJuros >= response.valorSolicitado, "Valor total deve ser maior ou igual ao solicitado");
  }

  @Test
  @Transactional
  public void deveCalcularJurosCorretamenteParaTaxa12PorcentoAoAno() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo 12% a.a.";
    produto.taxaJurosAnual = 12.0;
    produto.prazoMaximoMeses = 24;
    produto.persist();

    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = produto.id;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    SimulacaoResponse response = simulacaoService.simular(request);

    assertEquals(0.00949, response.taxaJurosEfetivaMensal, 0.00001, "Taxa efetiva mensal incorreta para 12% a.a.");

    assertEquals(88.85, response.parcelaMensal, 1.0, "Parcela calculada incorretamente");

    assertEquals(response.parcelaMensal * 12, response.valorTotalComJuros, 0.10, "Valor total incorreto");
  }

  @Test
  @Transactional
  public void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 999L;
    request.valorSolicitado = 10000.0f;
    request.prazoMeses = 12;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      simulacaoService.simular(request);
    });

    assertEquals("Produto não encontrado", exception.getMessage());
  }

  @Test
  @Transactional
  public void deveLancarExcecaoQuandoPrazoExcedeLimite() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.0;
    produto.prazoMaximoMeses = 12;
    produto.persist();

    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = produto.id;
    request.valorSolicitado = 10000.0f;
    request.prazoMeses = 24;

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      simulacaoService.simular(request);
    });

    assertTrue(exception.getMessage().contains("excede o limite"));
  }
}
