package ecommerce.external.fake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PagamentoSimuladoTest {
    
    @InjectMocks
    private PagamentoSimulado pagamentoSimulado;
    
    @Test
    void testAutorizarPagamentoOk() {
        var pagamento = pagamentoSimulado.autorizarPagamento(1L, 1000.50);
        
        assertTrue(pagamento.autorizado());
        assertEquals(1L, pagamento.transacaoId());
    }
    
    @Test
    void testAutorizarPagamentoNaoAutorizadoIdClienteInvalido() {
        var pagamento = pagamentoSimulado.autorizarPagamento(0L, 1000.50);
        
        assertFalse(pagamento.autorizado());
    }
    
    @Test
    void testAutorizarPagamentoNaoAutorizadoCustoTotalInvalido() {
        var pagamento = pagamentoSimulado.autorizarPagamento(1L, 0.0);
        
        assertFalse(pagamento.autorizado());
    }
    
    @Test
    void dummyTestCancelarPagamento() {
        pagamentoSimulado.cancelarPagamento(1L, 1L);
    }
}
