package org.pablofsc.resource;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.service.ProdutoService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos de empréstimo")
public class ProdutosResource {

  @Inject
  ProdutoService produtoService;

  @GET
  @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos de empréstimo disponíveis")
  @APIResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(schema = @Schema(implementation = ProdutoEntity.class)))
  public List<ProdutoEntity> listAll() {
    return produtoService.listAll();
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu identificador")
  @APIResponses({
      @APIResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = ProdutoEntity.class))),
      @APIResponse(responseCode = "204", description = "Produto não encontrado")
  })
  public ProdutoEntity getById(@Parameter(description = "ID do produto", required = true) @PathParam("id") Long id) {
    return produtoService.getById(id);
  }

  @POST
  @Operation(summary = "Criar novo produto", description = "Cria um novo produto financeiro")
  @APIResponses({
      @APIResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProdutoEntity.class))),
      @APIResponse(responseCode = "400", description = "Dados inválidos fornecidos")
  })
  public Response create(@Valid ProdutoEntity produto) {
    ProdutoEntity created = produtoService.create(produto);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}")
  @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
  @APIResponses({
      @APIResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(schema = @Schema(implementation = ProdutoEntity.class))),
      @APIResponse(responseCode = "404", description = "Produto não encontrado"),
      @APIResponse(responseCode = "400", description = "Dados inválidos fornecidos")
  })
  public Response update(@Parameter(description = "ID do produto", required = true) @PathParam("id") Long id,
      @Valid ProdutoEntity produto) {
    ProdutoEntity updated = produtoService.update(id, produto);

    if (updated == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok(updated).build();
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Excluir produto", description = "Remove um produto do sistema")
  @APIResponses({
      @APIResponse(responseCode = "204", description = "Produto excluído com sucesso"),
      @APIResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public Response delete(@Parameter(description = "ID do produto", required = true) @PathParam("id") Long id) {
    boolean deleted = produtoService.delete(id);

    if (deleted) {
      return Response.noContent().build();
    }

    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
