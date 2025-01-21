package ecommerce.external.fake;

import org.springframework.stereotype.Service;

import ecommerce.dto.PagamentoDTO;
import ecommerce.external.IPagamentoExternal;

@Service
public class PagamentoSimulado implements IPagamentoExternal{
    
    @Override
    public PagamentoDTO autorizarPagamento(Long clienteId, Double custoTotal) {
        var isPagamentoAutorizado = Boolean.FALSE;
        
        if(clienteId > 0 && custoTotal > 0) {
            isPagamentoAutorizado = Boolean.TRUE;
        }

        return new PagamentoDTO(isPagamentoAutorizado, clienteId);
    }

    @Override
    public void cancelarPagamento(Long clienteId, Long pagamentoTransacaoId) {
        // TODO Auto-generated method stub
        
    }
}
