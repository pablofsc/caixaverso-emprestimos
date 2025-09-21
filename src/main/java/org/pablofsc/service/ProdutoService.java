package org.pablofsc.service;

import java.util.List;

import org.pablofsc.model.ProdutoEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProdutoService {

  public List<ProdutoEntity> listAll() {
    return ProdutoEntity.listAll();
  }

  public ProdutoEntity getById(Long id) {
    return ProdutoEntity.findById(id);
  }

  @Transactional
  public ProdutoEntity create(ProdutoEntity produto) {
    produto.persist();
    return produto;
  }

  @Transactional
  public ProdutoEntity update(Long id, ProdutoEntity produto) {
    ProdutoEntity existing = ProdutoEntity.findById(id);

    if (existing == null) {
      return null;
    }

    existing.nome = produto.nome;
    existing.taxaJurosAnual = produto.taxaJurosAnual;
    existing.prazoMaximoMeses = produto.prazoMaximoMeses;
    existing.persist();

    return existing;
  }

  @Transactional
  public boolean delete(Long id) {
    return ProdutoEntity.deleteById(id);
  }
}
