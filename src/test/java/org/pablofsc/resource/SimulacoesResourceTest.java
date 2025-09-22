package org.pablofsc.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.pablofsc.model.MemoriaCalculo;
import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.model.SimulacaoRequest;
import org.pablofsc.model.SimulacaoResponse;
import org.pablofsc.service.SimulacaoService;

@QuarkusTest
public class SimulacoesResourceTest {

  @InjectMock
  SimulacaoService simulacaoService;

  @Test
  public void deveRetornarSimulacaoComSucessoParaValoresValidos() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    ProdutoEntity produto = new ProdutoEntity();
    produto.id = 1L;
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.0;
    produto.prazoMaximoMeses = 24;

    List<MemoriaCalculo> memoria = new ArrayList<>();
    memoria.add(new MemoriaCalculo(1, 1000.0, 10.0, 83.33, 916.67));

    SimulacaoResponse response = new SimulacaoResponse(produto, 1000.0, 12, 0.01, 1120.0, 93.33, memoria);

    when(simulacaoService.simular(any(SimulacaoRequest.class))).thenReturn(response);

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(200)
        .body("valorSolicitado", is(1000.0f))
        .body("prazoMeses", is(12))
        .body("valorTotalComJuros", is(1120.0f))
        .body("parcelaMensal", is(93.33f))
        .body("taxaJurosEfetivaMensal", is(0.01f))
        // Validações de domínio na resposta
        .body("valorSolicitado", greaterThan(0.0f))
        .body("prazoMeses", greaterThan(0))
        .body("taxaJurosEfetivaMensal", greaterThanOrEqualTo(0.0f))
        .body("valorTotalComJuros", greaterThanOrEqualTo(1000.0f))
        .body("parcelaMensal", greaterThan(0.0f));
  }

  @Test
  public void deveRetornarErroParaRequestComIdProdutoNulo() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = null; // ID nulo deve falhar na validação
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRetornarErroParaRequestComValorZero() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 0.0f; // Valor zero deve falhar na validação
    request.prazoMeses = 12;

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRetornarErroParaRequestComValorNegativo() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = -1000.0f; // Valor negativo deve falhar na validação
    request.prazoMeses = 12;

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRetornarErroParaRequestComPrazoZero() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 0; // Prazo zero deve falhar na validação

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRetornarErroQuandoServicoLancaExcecao() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 1L;
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    when(simulacaoService.simular(any(SimulacaoRequest.class)))
        .thenThrow(new IllegalArgumentException("Erro na simulação"));

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400)
        .body("error", is("Erro na simulação"));
  }

  @Test
  public void deveRetornarErroParaProdutoInexistente() {
    SimulacaoRequest request = new SimulacaoRequest();
    request.idProduto = 999L; // ID que não existe
    request.valorSolicitado = 1000.0f;
    request.prazoMeses = 12;

    when(simulacaoService.simular(any(SimulacaoRequest.class)))
        .thenThrow(new IllegalArgumentException("Produto não encontrado"));

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when().post("/simulacoes")
        .then()
        .statusCode(400)
        .body("error", is("Produto não encontrado"));
  }
}
