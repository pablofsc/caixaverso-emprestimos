package org.pablofsc.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RegisterForReflection
@Schema(description = "Dados para solicitação de simulação de empréstimo")
public class SimulacaoRequest {

  @NotNull(message = "ID do produto é obrigatório")
  @Min(value = 1, message = "ID do produto deve ser maior que 0")
  @Schema(description = "Identificador do produto financeiro", example = "1", required = true)
  public Long idProduto;

  @NotNull(message = "Valor solicitado é obrigatório")
  @DecimalMin(value = "0.01", message = "Valor solicitado deve ser maior que 0")
  @Schema(description = "Valor do empréstimo solicitado", example = "10000.00", required = true)
  public Float valorSolicitado;

  @NotNull(message = "Prazo em meses é obrigatório")
  @Min(value = 1, message = "Prazo deve ser pelo menos 1 mês")
  @Schema(description = "Prazo do empréstimo em meses", example = "12", required = true)
  public Integer prazoMeses;
}
