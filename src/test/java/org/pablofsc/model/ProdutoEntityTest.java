package org.pablofsc.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProdutoEntityTest {

  private static Validator validator;

  @BeforeAll
  public static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void deveValidarProdutoValido() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertTrue(violations.isEmpty(), "Produto válido não deve ter violações");
  }

  @Test
  public void deveRejeitarNomeVazio() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = 12;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Nome vazio deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Nome é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarNomeNulo() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = null;
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = 12;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Nome nulo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Nome é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarTaxaJurosNula() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = null;
    produto.prazoMaximoMeses = 12;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Taxa de juros nula deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Taxa de juros anual é obrigatória", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarTaxaJurosNegativa() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = -5.0;
    produto.prazoMaximoMeses = 12;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Taxa de juros negativa deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Taxa de juros deve ser maior ou igual a 0", violations.iterator().next().getMessage());
  }

  @Test
  public void deveAceitarTaxaJurosZero() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = 0.0;
    produto.prazoMaximoMeses = 12;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertTrue(violations.isEmpty(), "Taxa de juros zero deve ser válida");
  }

  @Test
  public void deveRejeitarPrazoNulo() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = null;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Prazo máximo nulo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Prazo máximo em meses é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarPrazoZero() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = 0;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Prazo máximo zero deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Prazo máximo deve ser pelo menos 1 mês", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarPrazoNegativo() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = 10.0;
    produto.prazoMaximoMeses = -1;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Prazo máximo negativo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Prazo máximo deve ser pelo menos 1 mês", violations.iterator().next().getMessage());
  }

  @Test
  public void deveDetectarMultiplasViolacoes() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "";
    produto.taxaJurosAnual = null;
    produto.prazoMaximoMeses = 0;

    Set<ConstraintViolation<ProdutoEntity>> violations = validator.validate(produto);
    assertFalse(violations.isEmpty(), "Múltiplas violações devem ser detectadas");
    assertEquals(3, violations.size());
  }
}
