package org.pablofsc.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

@Entity
@RegisterForReflection
@NoArgsConstructor
public class ProdutoEntity extends PanacheEntity {

  @NotBlank(message = "Nome é obrigatório")
  public String nome;

  @NotNull(message = "Taxa de juros anual é obrigatória")
  @DecimalMin(value = "0.0", message = "Taxa de juros deve ser maior ou igual a 0")
  public Double taxaJurosAnual;

  @NotNull(message = "Prazo máximo em meses é obrigatório")
  @Min(value = 1, message = "Prazo máximo deve ser pelo menos 1 mês")
  public Integer prazoMaximoMeses;
}
