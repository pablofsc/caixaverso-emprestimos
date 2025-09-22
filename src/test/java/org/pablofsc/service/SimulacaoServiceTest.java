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
    // Create a product with max 12 months
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.0;
    produto.prazoMaximoMeses = 12;
    produto.persist();

    // Request with 24 months
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
