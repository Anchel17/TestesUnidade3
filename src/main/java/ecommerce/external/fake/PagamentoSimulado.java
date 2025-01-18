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
        
        //verificar se substitui o id da transação sendo setado como clienteId
        //ou usa uma função para gerar números aleatórios
        var pagamentoDTO = new PagamentoDTO(isPagamentoAutorizado, clienteId);

        return pagamentoDTO;
    }

    @Override
    public void cancelarPagamento(Long clienteId, Long pagamentoTransacaoId) {
        // TODO Auto-generated method stub
        
    }
}
