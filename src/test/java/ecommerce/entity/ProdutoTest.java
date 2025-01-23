package ecommerce.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoTest {

	private final Produto produto = new Produto(); 

    @Test
    void testGetIdValido() {
        produto.setId(1L); 
        assertEquals(1L, produto.getId()); 
    }

    @Test
    void testGetPesoValido() {
        produto.setPeso(10);
        assertEquals(10, produto.getPeso());
    }

}
