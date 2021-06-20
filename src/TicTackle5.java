import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
        setTabuleiro(new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO]);
        iniciaJogo();
        setBackground(Color.white);
        //fazJogada(0, 0, 1, 1);
        //printaPossiveisJogadas();
    }
    public void iniciaJogo() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][0] = PECA_BRANCA;
            }
            else {
                getTabuleiro()[x][0] = PECA_PRETA;
            }   
        }
        for(int y=1; y < ALTURA_TABULEIRO-1; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_PRETA;
            }
            else {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_BRANCA;
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
                switch(getTabuleiro()[x][y]) {
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
        g.setColor(Color.black);
        int inicioX;
        int inicioY;
        int fimX;
        int fimY;
        for(int x=0; x<LARGURA_TABULEIRO; x++) {
            inicioX = calculaPosicaoFila(x, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            fimX = inicioX;
            fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        for(int y=0; y<LARGURA_TABULEIRO; y++) {
            inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(y, 1, ALTURA_TELA, ALTURA_TABULEIRO);
            fimX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
            fimY = inicioY;
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila((int)Math.ceil(ALTURA_TABULEIRO/2), 1, ALTURA_TELA, ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila((int)Math.ceil(LARGURA_TABULEIRO/2), 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(LARGURA_TABULEIRO-1, 1, LARGURA_TELA, LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(ALTURA_TABULEIRO-1, 1, ALTURA_TELA, ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
    }
    @Override
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }
    public void fazJogada(int xInicial, int yInicial, int xFinal, int yFinal) {
        getTabuleiro()[xFinal][yFinal] = getTabuleiro()[xInicial][yInicial];
        getTabuleiro()[xInicial][yInicial] = SEM_PECA;
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
    }
    public boolean verificaJogada(int xInicial, int yInicial, int xFinal, int yFinal) {
        if(estaNosLimites(xInicial, yInicial) && estaNosLimites(xFinal, yFinal)) {
            if (getTabuleiro()[xFinal][yFinal] == SEM_PECA && getTabuleiro()[xInicial][yInicial] != SEM_PECA) {
                //Se quer se mover na diagonal
                if((Math.abs(xInicial - xFinal) == 1 ) && (Math.abs(yInicial - yFinal) == 1)) {
                    //Se x e y têm a mesma paridade
                    if(((xInicial % 2 == 0) && (yInicial % 2 == 0)) || ((xInicial % 2 == 1) && (yInicial % 2 == 1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //Se quer se mover na vertical/horizontal
                else if((Math.abs(xInicial - xFinal) <= 1 ) && (Math.abs(yInicial - yFinal) <= 1)) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
        
    }
    public void printaPossiveisJogadas() {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(PECA_BRANCA);
        for(int i=0; i < possiveisJogadas.size(); i++) {
            System.out.println("("+possiveisJogadas.get(i)[0]+", "+possiveisJogadas.get(i)[1]+") -> ("+possiveisJogadas.get(i)[2]+", "+possiveisJogadas.get(i)[3]+")");
        }
    }
    public ArrayList<int[]> listaPossiveisJogadas(int corPeca) {
        ArrayList<int[]> possiveisJogadas = new ArrayList<int[]>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    for(int i=0; i < regioes.length; i++) {
                        int novoX = x+regioes[i][0];
                        int novoY = y+regioes[i][1];
                        if(verificaJogada(x,y,novoX,novoY)) {
                            int[] possibilidade = new int[4];
                            possibilidade[0] = x;
                            possibilidade[1] = y;
                            possibilidade[2] = novoX;
                            possibilidade[3] = novoY;
                            possiveisJogadas.add(possibilidade);
                        }
                    }
                }
            }
        }
        return possiveisJogadas;
    }
    public void maquinaJoga() {
        
    }
}
