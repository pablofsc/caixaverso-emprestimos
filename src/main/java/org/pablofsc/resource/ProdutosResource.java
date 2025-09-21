package org.pablofsc.resource;

import java.util.List;

import org.pablofsc.model.ProdutoEntity;
import org.pablofsc.service.ProdutoService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/produtos")
public class ProdutosResource {

  @Inject
  ProdutoService produtoService;

  @GET
  public List<ProdutoEntity> listAll() {
    return produtoService.listAll();
  }

  @GET
  @Path("/{id}")
  public ProdutoEntity getById(@PathParam("id") Long id) {
    return produtoService.getById(id);
  }

  @POST
  public Response create(ProdutoEntity produto) {
    ProdutoEntity created = produtoService.create(produto);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") Long id, ProdutoEntity produto) {
    ProdutoEntity updated = produtoService.update(id, produto);

    if (updated == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok(updated).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    boolean deleted = produtoService.delete(id);

    if (deleted) {
      return Response.noContent().build();
    }

    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
