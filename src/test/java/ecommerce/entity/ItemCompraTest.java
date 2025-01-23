package ecommerce.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemCompraTest {

    @Mock
    private Produto mockProduto;

    @InjectMocks
    private ItemCompra itemCompra; 
	   
    @Test
    void testGetQuantidadeValida() {
    	itemCompra = new ItemCompra(1L, mockProduto, 5L);
    	
    	assertEquals(5L, itemCompra.getQuantidade());
    }

}
