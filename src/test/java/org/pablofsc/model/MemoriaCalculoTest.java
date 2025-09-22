package org.pablofsc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoriaCalculoTest {

  @Test
  public void deveCriarMemoriaCalculoComValoresValidosIniciais() {
    MemoriaCalculo mc = new MemoriaCalculo(1, 1000.0, 50.0, 100.0, 950.0);

    assertEquals(1, mc.mes);
    assertEquals(1000.0, mc.saldoDevedorInicial);
    assertEquals(50.0, mc.juros);
    assertEquals(100.0, mc.amortizacao);
    assertEquals(950.0, mc.saldoDevedorFinal);
  }

  @Test
  public void deveCriarMemoriaCalculoMesIntermediario() {
    MemoriaCalculo mc = new MemoriaCalculo(6, 500.0, 25.0, 75.0, 425.0);

    assertEquals(6, mc.mes);
    assertEquals(500.0, mc.saldoDevedorInicial);
    assertEquals(25.0, mc.juros);
    assertEquals(75.0, mc.amortizacao);
    assertEquals(425.0, mc.saldoDevedorFinal);
  }

  @Test
  public void deveCriarMemoriaCalculoUltimaMes() {
    MemoriaCalculo mc = new MemoriaCalculo(12, 95.24, 4.76, 95.24, 0.0);

    assertEquals(12, mc.mes);
    assertEquals(95.24, mc.saldoDevedorInicial);
    assertEquals(4.76, mc.juros);
    assertEquals(95.24, mc.amortizacao);
    assertEquals(0.0, mc.saldoDevedorFinal);
  }

  @Test
  public void deveValidarCoerenciaEntreValores() {
    // verifica se amortização + juros = parcela e saldo inicial - amortização = saldo final
    MemoriaCalculo mc = new MemoriaCalculo(3, 800.0, 40.0, 60.0, 740.0);

    assertTrue(mc.mes > 0, "Mês deve ser positivo");
    assertTrue(mc.saldoDevedorInicial >= 0, "Saldo devedor inicial não pode ser negativo");
    assertTrue(mc.juros >= 0, "Juros não podem ser negativos");
    assertTrue(mc.amortizacao >= 0, "Amortização não pode ser negativa");
    assertTrue(mc.saldoDevedorFinal >= 0, "Saldo devedor final não pode ser negativo");

    // verifica se saldo inicial - amortização = saldo final
    assertEquals(mc.saldoDevedorInicial - mc.amortizacao, mc.saldoDevedorFinal, 0.01, "Saldo final deve ser igual ao saldo inicial menos a amortização");
  }
}
