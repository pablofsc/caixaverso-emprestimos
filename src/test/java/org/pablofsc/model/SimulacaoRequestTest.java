package org.pablofsc.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SimulacaoRequestTest {

  private static Validator validator;

  @BeforeAll
  public static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void deveValidarSimulacaoRequestValido() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty(), "SimulacaoRequest válido não deve ter violações");
  }

  @Test
  public void deveRejeitarIdProdutoNulo() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = null;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "ID do produto nulo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("ID do produto é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarIdProdutoMenorOuIgualZero() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 0L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "ID do produto menor ou igual a zero deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("ID do produto deve ser maior que 0", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarValorSolicitadoNulo() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = null;
    request.prazoMeses = 12;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Valor solicitado nulo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Valor solicitado é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarValorSolicitadoMenorOuIgualZero() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 0.0f;
    request.prazoMeses = 12;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Valor solicitado menor ou igual a zero deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Valor solicitado deve ser maior que 0", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarPrazoMesesNulo() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = null;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Prazo em meses nulo deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Prazo em meses é obrigatório", violations.iterator().next().getMessage());
  }

  @Test
  public void deveRejeitarPrazoMesesMenorQueUm() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 0;

    Set<ConstraintViolation<SimulacaoRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty(), "Prazo em meses menor que 1 deve causar violação");
    assertEquals(1, violations.size());
    assertEquals("Prazo deve ser pelo menos 1 mês", violations.iterator().next().getMessage());
  }
}
