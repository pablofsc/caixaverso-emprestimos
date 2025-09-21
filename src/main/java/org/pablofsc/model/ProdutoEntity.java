package org.pablofsc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Entity;

@Entity
@RegisterForReflection
public class ProdutoEntity extends PanacheEntity {

  public String nome;
  public Double taxaJurosAnual;
  public Integer prazoMaximoMeses;
}
