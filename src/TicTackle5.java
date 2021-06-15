import java.awt.Color;
import java.awt.Graphics;

public class TicTackle5 extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    TicTackle5() {
        super();
        setNome("Tic Tackle 5");
        setTabuleiro(new int[ALTURA_TABULEIRO][LARGURA_TABULEIRO]);
        iniciaJogo();
        setBackground(Color.lightGray);
    }
    public void iniciaJogo() {
        int[][] tabuleiro = getTabuleiro();
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                tabuleiro[0][x] = PECA_BRANCA;
            }
            else {
                tabuleiro[0][x] = PECA_PRETA;
            }   
        }
        for(int y=1; y < ALTURA_TABULEIRO-1; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                tabuleiro[y][x] = SEM_PECA;
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                tabuleiro[ALTURA_TABULEIRO-1][x] = PECA_PRETA;
            }
            else {
                tabuleiro[ALTURA_TABULEIRO-1][x] = PECA_BRANCA;
            } 
        }
    }
    @Override
    public void desenhaTabuleiro(Graphics g) {
        
    }
}
