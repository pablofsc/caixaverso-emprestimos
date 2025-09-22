package org.pablofsc.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.pablofsc.model.ProdutoEntity;

@QuarkusTest
public class ProdutoServiceTest {

  @Inject
  ProdutoService produtoService;

  @Test
  @Transactional
  public void deveListarTodosProdutos() {
    // Create some products
    ProdutoEntity produto1 = new ProdutoEntity();
    produto1.nome = "Empréstimo Pessoal";
    produto1.taxaJurosAnual = 12.5;
    produto1.prazoMaximoMeses = 24;
    produto1.persist();

    ProdutoEntity produto2 = new ProdutoEntity();
    produto2.nome = "Empréstimo Consignado";
    produto2.taxaJurosAnual = 10.0;
    produto2.prazoMaximoMeses = 36;
    produto2.persist();

    List<ProdutoEntity> result = produtoService.listAll();

    assertTrue(result.size() >= 2);
    assertTrue(result.stream().anyMatch(p -> "Empréstimo Pessoal".equals(p.nome)));
    assertTrue(result.stream().anyMatch(p -> "Empréstimo Consignado".equals(p.nome)));
  }

  @Test
  @Transactional
  public void deveRetornarProdutoPorId() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;
    produto.persist();

    ProdutoEntity result = produtoService.getById(produto.id);

    assertNotNull(result);
    assertEquals(produto.id, result.id);
    assertEquals("Empréstimo Pessoal", result.nome);
  }

  @Test
  @Transactional
  public void deveRetornarNullQuandoProdutoNaoEncontrado() {
    ProdutoEntity result = produtoService.getById(999L);

    assertNull(result);
  }

  @Test
  @Transactional
  public void deveCriarProduto() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    ProdutoEntity result = produtoService.create(produto);

    assertNotNull(result);
    assertNotNull(result.id);
    assertEquals("Empréstimo Pessoal", result.nome);
  }

  @Test
  @Transactional
  public void deveAtualizarProdutoExistente() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Antigo";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = 12;
    produto.persist();

    ProdutoEntity updateData = new ProdutoEntity();
    updateData.nome = "Empréstimo Atualizado";
    updateData.taxaJurosAnual = 11.0;
    updateData.prazoMaximoMeses = 24;

    ProdutoEntity result = produtoService.update(produto.id, updateData);

    assertNotNull(result);
    assertEquals(produto.id, result.id);
    assertEquals("Empréstimo Atualizado", result.nome);
    assertEquals(11.0, result.taxaJurosAnual);
    assertEquals(24, result.prazoMaximoMeses);
  }

  @Test
  @Transactional
  public void deveRetornarNullAoAtualizarProdutoInexistente() {
    ProdutoEntity updateData = new ProdutoEntity();
    updateData.nome = "Empréstimo Atualizado";
    updateData.taxaJurosAnual = 11.0;
    updateData.prazoMaximoMeses = 24;

    ProdutoEntity result = produtoService.update(999L, updateData);

    assertNull(result);
  }

  @Test
  @Transactional
  public void deveDeletarProdutoExistente() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;
    produto.persist();

    boolean result = produtoService.delete(produto.id);

    assertTrue(result);
  }

  @Test
  @Transactional
  public void deveRetornarFalseAoDeletarProdutoInexistente() {
    boolean result = produtoService.delete(999L);

    assertFalse(result);
  }
}
