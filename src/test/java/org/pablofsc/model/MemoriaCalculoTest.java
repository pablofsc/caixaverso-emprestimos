package org.pablofsc.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemoriaCalculoTest {

  @Test
  public void deveCriarMemoriaCalculoComValoresNormais() {
    MemoriaCalculo mc = new MemoriaCalculo(1, 1000.0, 50.0, 100.0, 950.0);
    assertEquals(1, mc.mes);
    assertEquals(1000.0, mc.saldoDevedorInicial);
    assertEquals(50.0, mc.juros);
    assertEquals(100.0, mc.amortizacao);
    assertEquals(950.0, mc.saldoDevedorFinal);
  }

  @Test
  public void deveCriarMemoriaCalculoComValoresZero() {
    MemoriaCalculo mc = new MemoriaCalculo(0, 0.0, 0.0, 0.0, 0.0);
    assertEquals(0, mc.mes);
    assertEquals(0.0, mc.saldoDevedorInicial);
    assertEquals(0.0, mc.juros);
    assertEquals(0.0, mc.amortizacao);
    assertEquals(0.0, mc.saldoDevedorFinal);
  }

  @Test
  public void deveCriarMemoriaCalculoComValoresNegativos() {
    MemoriaCalculo mc = new MemoriaCalculo(-1, -1000.0, -50.0, -100.0, -950.0);
    assertEquals(-1, mc.mes);
    assertEquals(-1000.0, mc.saldoDevedorInicial);
    assertEquals(-50.0, mc.juros);
    assertEquals(-100.0, mc.amortizacao);
    assertEquals(-950.0, mc.saldoDevedorFinal);
  }
}
