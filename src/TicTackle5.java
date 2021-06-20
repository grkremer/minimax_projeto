import java.awt.Color;
import java.awt.Graphics;

public class TicTackle5 extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    TicTackle5() {
        super();
        setNome("Tic Tackle 5");
        setTabuleiro(new int[ALTURA_TABULEIRO][LARGURA_TABULEIRO]);
        iniciaJogo();
        setBackground(Color.white);
    }
    public void iniciaJogo() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[0][x] = PECA_BRANCA;
            }
            else {
                getTabuleiro()[0][x] = PECA_PRETA;
            }   
        }
        for(int y=1; y < ALTURA_TABULEIRO-1; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[y][x] = SEM_PECA;
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[ALTURA_TABULEIRO-1][x] = PECA_PRETA;
            }
            else {
                getTabuleiro()[ALTURA_TABULEIRO-1][x] = PECA_BRANCA;
            } 
        }
    }
    private int calculaPosicaoFila(int posicaoObjeto, int tamanhoObjeto, int pixelsFila, int tamanhoFila) {
        return ((pixelsFila/(tamanhoFila+1)) *(posicaoObjeto+1)) - (tamanhoObjeto/2);
    }
    private void desenhaPecas(Graphics g) {
        int posicaoX;
        int posicaoY;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                switch(getTabuleiro()[y][x]) {
                    case SEM_PECA: 
                        posicaoX = calculaPosicaoFila(x, (int)(TAMANHO_PECA/1.5), LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, (int)(TAMANHO_PECA/1.5), ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.lightGray);
                        g.fillOval(posicaoX, posicaoY, (int)(TAMANHO_PECA/1.5), (int)(TAMANHO_PECA/1.5));
                        break;
                    case PECA_BRANCA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.white);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        g.setColor(Color.black);
                        g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        break;
                    case PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.black);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        break;
                }
            }
        }
    }
    private void desenhaLinhas(Graphics g) {
    }
    @Override
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }
}
