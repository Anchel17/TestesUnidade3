package ecommerce.external.fake;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ecommerce.dto.DisponibilidadeDTO;
import ecommerce.dto.EstoqueBaixaDTO;
import ecommerce.external.IEstoqueExternal;

@Service
public class EstoqueSimulado implements IEstoqueExternal {

    @Override
    public EstoqueBaixaDTO darBaixa(List<Long> produtosIds, List<Long> produtosQuantidades) {
        var sucesso = Boolean.TRUE;
        
        for(var i = 0; i < produtosIds.size(); i++) {
            if(produtosQuantidades.get(i) <= 0) {
                sucesso = Boolean.FALSE;
                break;
            }
        }
        
        return new EstoqueBaixaDTO(sucesso);
    }

    @Override
    public DisponibilidadeDTO verificarDisponibilidade(List<Long> produtosIds, List<Long> produtosQuantidades) {
        var isProdutosDisponiveis = Boolean.FALSE;
        var listaProdutosIndisponiveis = new ArrayList<Long>();
        
        for(var i = 0; i < produtosIds.size(); i++) {
            if(produtosQuantidades.get(i) == 0){
                listaProdutosIndisponiveis.add(produtosIds.get(i));
            }
        }
        
        if(!produtosIds.isEmpty() && !produtosQuantidades.isEmpty()) {
            isProdutosDisponiveis = listaProdutosIndisponiveis.size() == produtosIds.size();
        }
        
        return new DisponibilidadeDTO(isProdutosDisponiveis, listaProdutosIndisponiveis);
    }
}
