package org.pablofsc.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoRequest {

  @NotNull(message = "ID do produto é obrigatório")
  @Min(value = 1, message = "ID do produto deve ser maior que 0")
  public Long idProduto;

  @NotNull(message = "Valor solicitado é obrigatório")
  @DecimalMin(value = "0.01", message = "Valor solicitado deve ser maior que 0")
  public Float valorSolicitado;

  @NotNull(message = "Prazo em meses é obrigatório")
  @Min(value = 1, message = "Prazo deve ser pelo menos 1 mês")
  public Integer prazoMeses;
}
