package org.pablofsc.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.service.ProdutoService;

@QuarkusTest
public class ProdutosResourceTest {

  @InjectMock
  ProdutoService produtoService;

  @Test
  public void deveListarTodosProdutos() {
    ProdutoEntity produto1 = new ProdutoEntity();
    produto1.id = 1L;
    produto1.nome = "Empréstimo Pessoal";
    produto1.taxaJurosAnual = 12.5;
    produto1.prazoMaximoMeses = 24;

    ProdutoEntity produto2 = new ProdutoEntity();
    produto2.id = 2L;
    produto2.nome = "Empréstimo Consignado";
    produto2.taxaJurosAnual = 10.0;
    produto2.prazoMaximoMeses = 36;

    when(produtoService.listAll()).thenReturn(List.of(produto1, produto2));

    given()
        .when().get("/produtos")
        .then()
        .statusCode(200)
        .body("size()", is(2))
        .body("[0].nome", is("Empréstimo Pessoal"))
        .body("[1].nome", is("Empréstimo Consignado"));
  }

  @Test
  public void deveRetornarProdutoPorId() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.id = 1L;
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    when(produtoService.getById(1L)).thenReturn(produto);

    given()
        .when().get("/produtos/1")
        .then()
        .statusCode(200)
        .body("nome", is("Empréstimo Pessoal"))
        .body("taxaJurosAnual", is(12.5f))
        .body("prazoMaximoMeses", is(24));
  }

  @Test
  public void deveRetornar204QuandoProdutoNaoEncontrado() {
    when(produtoService.getById(1L)).thenReturn(null);

    given()
        .when().get("/produtos/1")
        .then()
        .statusCode(204);
  }

  @Test
  public void deveCriarProdutoComSucesso() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Pessoal";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    ProdutoEntity created = new ProdutoEntity();
    created.id = 1L;
    created.nome = "Empréstimo Pessoal";
    created.taxaJurosAnual = 12.5;
    created.prazoMaximoMeses = 24;

    when(produtoService.create(any(ProdutoEntity.class))).thenReturn(created);

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().post("/produtos")
        .then()
        .statusCode(201)
        .body("nome", is("Empréstimo Pessoal"));
  }

  @Test
  public void deveAtualizarProdutoComSucesso() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Atualizado";
    produto.taxaJurosAnual = 11.0;
    produto.prazoMaximoMeses = 30;

    ProdutoEntity updated = new ProdutoEntity();
    updated.id = 1L;
    updated.nome = "Empréstimo Atualizado";
    updated.taxaJurosAnual = 11.0;
    updated.prazoMaximoMeses = 30;

    when(produtoService.update(eq(1L), any(ProdutoEntity.class))).thenReturn(updated);

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().put("/produtos/1")
        .then()
        .statusCode(200)
        .body("nome", is("Empréstimo Atualizado"));
  }

  @Test
  public void deveRetornar404AoAtualizarProdutoInexistente() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Empréstimo Atualizado";
    produto.taxaJurosAnual = 11.0;
    produto.prazoMaximoMeses = 30;

    when(produtoService.update(eq(1L), any(ProdutoEntity.class))).thenReturn(null);

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().put("/produtos/1")
        .then()
        .statusCode(404);
  }

  @Test
  public void deveDeletarProdutoComSucesso() {
    when(produtoService.delete(1L)).thenReturn(true);

    given()
        .when().delete("/produtos/1")
        .then()
        .statusCode(204);
  }

  @Test
  public void deveRetornar404AoDeletarProdutoInexistente() {
    when(produtoService.delete(1L)).thenReturn(false);

    given()
        .when().delete("/produtos/1")
        .then()
        .statusCode(404);
  }

  @Test
  public void deveRejeitarCriacaoDeProdutoComNomeVazio() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = ""; // Nome vazio deve falhar na validação
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 24;

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().post("/produtos")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRejeitarCriacaoDeProdutoComTaxaJurosNegativa() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = -5.0; // Taxa negativa deve falhar na validação
    produto.prazoMaximoMeses = 24;

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().post("/produtos")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRejeitarCriacaoDeProdutoComPrazoZero() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = "Produto Teste";
    produto.taxaJurosAnual = 12.5;
    produto.prazoMaximoMeses = 0; // Prazo zero deve falhar na validação

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().post("/produtos")
        .then()
        .statusCode(400);
  }

  @Test
  public void deveRejeitarAtualizacaoComDadosInvalidos() {
    ProdutoEntity produto = new ProdutoEntity();
    produto.nome = null; // Nome nulo deve falhar na validação
    produto.taxaJurosAnual = -10.0; // Taxa negativa deve falhar na validação
    produto.prazoMaximoMeses = -5; // Prazo negativo deve falhar na validação

    given()
        .contentType(ContentType.JSON)
        .body(produto)
        .when().put("/produtos/1")
        .then()
        .statusCode(400);
  }
}
