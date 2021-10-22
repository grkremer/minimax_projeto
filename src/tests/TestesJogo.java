package tests;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jogos.util.Jogo;

public class TestesJogo extends Jogo{
    @Test
    public void testEstaNosLimites() {
        assertEquals(true, estaNosLimites(1, 2));
        assertEquals(false, estaNosLimites(-1, 3));
        assertEquals(false, estaNosLimites(4, 7));
    }
    @Test
    public void testComeuPeca() {
        int[] posInicial = {2, 2};
        int[] posFinal = {3, 2};
        assertEquals(false, comeuPeca(posInicial, posFinal));
        posFinal[0] = 4;
        assertEquals(true, comeuPeca(posInicial, posFinal));
    }
    @Test
    public void testInserePeca() {
        int[][] tabuleiro1 = {{PECA_BRANCA, PECA_BRANCA, SEM_PECA}, {PECA_PRETA, PECA_BRANCA, SEM_PECA}, {PECA_PRETA, PECA_PRETA, PECA_BRANCA}};
        int[] posicao = {0, 2};
        int[][] tabuleiro2 = {{PECA_BRANCA, PECA_BRANCA, PECA_PRETA}, {PECA_PRETA, PECA_BRANCA, SEM_PECA}, {PECA_PRETA, PECA_PRETA, PECA_BRANCA}};
        inserePeca(posicao, PECA_PRETA, tabuleiro1);
        assertArrayEquals(tabuleiro2, tabuleiro1);
    }
}
