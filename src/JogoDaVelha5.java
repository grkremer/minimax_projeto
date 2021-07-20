import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class JogoDaVelha5 extends Jogo {
    private static final int ALTURA_TABULEIRO = 5;
    private static final int LARGURA_TABULEIRO = 5;
    private static final int SEM_PECA = 0;
    private static final int PECA_BRANCA = 1;
    private static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    //private static final int MAXIMO_JOGADAS = 15000000;
    private static final int MAXIMO_JOGADAS = Integer.MAX_VALUE;
    private boolean vezDoPlayer = false;
    private int pecaPlayer = PECA_BRANCA;

    JogoDaVelha5() {
        super();
        setNome("Jogo da Velha 5");
        setTabuleiro(new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO]);
        setBackground(Color.white);
        addMouseListener(new EventosMouse());
    }
    public boolean isVezDoPlayer() {
        return vezDoPlayer;
    }
    public void setVezDoPlayer(boolean vezDoPlayer) {
        this.vezDoPlayer = vezDoPlayer;
    }
    public int getPecaPlayer() {
        return pecaPlayer;
    }
    public void setPecaPlayer(int pecaPlayer) {
        this.pecaPlayer = pecaPlayer;
    }

    private int calculaNivel(int jogada) {
        if(jogada == 0)
            return 6;
        else
            return 5;
    }
    public void partidaBotXPlayer() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        int jogadaMaquina = 0;
        while(!verificaVitoria(PECA_BRANCA) && !verificaVitoria(PECA_PRETA)) {
            setVezDoPlayer(true);
            System.out.println("Vez das peças brancas");
            while(isVezDoPlayer()) {
                Thread.sleep(1);
            }
            if(!verificaVitoria(PECA_BRANCA)) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,calculaNivel(jogadaMaquina));
                jogadaMaquina++;
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
            }
            int n = numeroDeAlinhamentosComVazios(PECA_PRETA,getTabuleiro());
            System.out.println("Alinhamentos: "+n);
        }
        paraTimer();
    }
    public void partidaPlayerXPlayer() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        while(!verificaVitoria(PECA_BRANCA) && !verificaVitoria(PECA_PRETA)) {
            setVezDoPlayer(true);
            System.out.println("Vez das peças brancas");
            verificaSimetriaHorizontal(getTabuleiro());
            verificaSimetriaVertical(getTabuleiro());
            verificaSimetriaDiagonal(getTabuleiro());
            verificaSimetriaOutraDiagonal(getTabuleiro());
            while(isVezDoPlayer()) {
                Thread.sleep(1);
            }
            if(!verificaVitoria(PECA_BRANCA)) {
                System.out.println("Vez das peças pretas");
            }
            System.out.println("------------");
        }
        paraTimer();
    }

    public void partidaBotXBot() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        float chance;
        while(!verificaVitoria(PECA_BRANCA) && !verificaVitoria(PECA_PRETA)) {
            System.out.println("Vez das peças brancas");
            chance = maquinaJoga(PECA_BRANCA,5);
            System.out.println("Chance de vitória das peças brancas: "+chance+"%");
            Thread.sleep(500);
            if(!verificaVitoria(PECA_BRANCA)) {
                System.out.println("Vez das peças pretas");
                chance = maquinaJoga(PECA_PRETA,5);
                System.out.println("Chance de vitória das peças pretas: "+chance+"%");
                Thread.sleep(250);
            }
        }
        getTimer().stop();
        repaint();
    }
    public void inicializaTabuleiro() {
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
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
    public void fazJogada(int x, int y, int peca) {
        getTabuleiro()[x][y] = peca;
    } 
    public int[][] fazJogada(int x, int y, int peca, int[][] tabuleiro) {
        tabuleiro[x][y] = peca;
        return tabuleiro;
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
    }
    public boolean verificaJogada(int x, int y) {
        if (estaNosLimites(x, y) && getTabuleiro()[x][y] == SEM_PECA) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaJogada(int x, int y, int[][] tabuleiro) {
        if (estaNosLimites(x, y) && tabuleiro[x][y] == SEM_PECA) {
            return true;
        }
        else {
            return false;
        }
    }
    public void printaPossiveisJogadas() {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(getTabuleiro());
        for(int i=0; i < possiveisJogadas.size(); i++) {
            System.out.println("("+possiveisJogadas.get(i)[0]+", "+possiveisJogadas.get(i)[1]+") -> ("+possiveisJogadas.get(i)[2]+", "+possiveisJogadas.get(i)[3]+")");
        }
    }
    private boolean condicaoLoopSimetria(int larguraMax, boolean temDiagonal, boolean temOutraDiagonal, int x, int y) {
        boolean condicaoLargura = x < larguraMax;
        boolean condicaoDiagonal = true;
        boolean condicaoOutraDiagonal = true;
        if(temOutraDiagonal) {
            condicaoDiagonal = (x+y) < LARGURA_TABULEIRO;
        }
        if(temDiagonal) {
            condicaoOutraDiagonal = x<=y;
        }
        return condicaoLargura && condicaoDiagonal && condicaoOutraDiagonal;
    }
    public ArrayList<int[]> listaPossiveisJogadas(int[][] tabuleiro) {
        ArrayList<int[]> possiveisJogadas = new ArrayList<int[]>();
        boolean horizontal = verificaSimetriaHorizontal(tabuleiro);
        boolean vertical = verificaSimetriaVertical(tabuleiro);
        boolean diagonal = verificaSimetriaDiagonal(tabuleiro);
        boolean outraDiagonal = verificaSimetriaOutraDiagonal(tabuleiro);
        int alturaMax = ALTURA_TABULEIRO;
        int larguraMax = LARGURA_TABULEIRO;

        if(horizontal) {
            alturaMax = ALTURA_TABULEIRO-2;
        }
        if(vertical) {
            larguraMax = LARGURA_TABULEIRO-2;
        }
        for(int y=0; y < alturaMax; y++) {
            for(int x=0; condicaoLoopSimetria(larguraMax,diagonal,outraDiagonal,x,y); x++) {
                if(verificaJogada(x,y,tabuleiro)) {
                    int[] possibilidade = new int[2];
                    possibilidade[0] = x;
                    possibilidade[1] = y;
                    possiveisJogadas.add(possibilidade);
                }
            }
        }
        return possiveisJogadas;
    }
    public int maximoAlinhado(int corPeca) {
        int maximo = 0;
        int contagem;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            contagem = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            contagem = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x-1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }

        if (maximo >= 4) {
            return maximo;
        }

        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(getTabuleiro()[x][x+1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }

        if (maximo >= 4) {
            return maximo;
        }

        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(getTabuleiro()[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(getTabuleiro()[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(getTabuleiro()[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        return maximo;
    }
    public int maximoAlinhado(int corPeca, int[][] tabuleiro) {
        int maximo = 0;
        int contagem;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            contagem = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            contagem = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        } 
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        return maximo;
    }

    public boolean tentaAcharTripla(int corPeca, int[][] tabuleiro) {

        for(int y=0; y < ALTURA_TABULEIRO; y++) //horizontal
            if(tabuleiro[0][y] == SEM_PECA && tabuleiro[4][y] == SEM_PECA)
                if (tabuleiro[1][y] == corPeca && tabuleiro[2][y] == corPeca && tabuleiro[3][y] == corPeca)
                    return true;

        for(int x=0; x < ALTURA_TABULEIRO; x++) //vertical
            if(tabuleiro[x][0] == SEM_PECA && tabuleiro[x][4] == SEM_PECA)
                if (tabuleiro[x][1] == corPeca && tabuleiro[x][2] == corPeca && tabuleiro[x][3] == corPeca)
                    return true;

        if (tabuleiro[0][0] == SEM_PECA && tabuleiro[1][1]== corPeca && tabuleiro [2][2] == corPeca && tabuleiro[3][3] == corPeca && tabuleiro [4][4] == SEM_PECA)
            return true;

        if (tabuleiro[0][4] == SEM_PECA && tabuleiro[1][3]== corPeca && tabuleiro [2][2] == corPeca && tabuleiro[3][1] == corPeca && tabuleiro [4][0] == SEM_PECA)
            return true;
        return false;
    }

    public boolean verificaVitoria(int corPeca) {
        if(maximoAlinhado(corPeca) >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(maximoAlinhado(corPeca, tabuleiro) >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro);
    }
    
    private int[][] criaCopiaTabuleiro(int[][] tabuleiro) {
        int[][] novoTabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                novoTabuleiro[x][y] = tabuleiro[x][y];
            }
        }
        return novoTabuleiro;
    }
    
    public int numeroDeAlinhamentos(int corPeca) {
        int contagem = 0;
        boolean encontrouPecaLinha;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            encontrouPecaLinha = false;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            encontrouPecaLinha = false;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(getTabuleiro()[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(getTabuleiro()[x][x-1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(getTabuleiro()[x][x+1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(getTabuleiro()[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(getTabuleiro()[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(getTabuleiro()[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        return contagem;
    }

    public int numeroDeAlinhamentos(int corPeca, int[][] tabuleiro) {
        int contagem = 0;
        boolean encontrouPecaLinha;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            encontrouPecaLinha = false;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            encontrouPecaLinha = false;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    if(encontrouPecaLinha)  contagem++;
                    else encontrouPecaLinha = true;
                }
            }
        }
        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }

        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        encontrouPecaLinha = false;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                if(encontrouPecaLinha)  contagem++;
                else encontrouPecaLinha = true;
            }
        }
        return contagem;
    }

    public int numeroDeAlinhamentosComVazios(int corPeca, int[][] tabuleiro) {
        int pontos = 0;
        boolean encontrouPecaLinha;

        int nCorPeca;
        int nOutraCor;
        int nConsecutivos;
        
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            encontrouPecaLinha = false;
            nCorPeca = 0;
            nOutraCor = 0;
            nConsecutivos = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    nCorPeca++;
                    if(encontrouPecaLinha) nConsecutivos++;
                    else encontrouPecaLinha = true;
                }
                else if(tabuleiro[x][y] == SEM_PECA) {
                    encontrouPecaLinha = false;
                }
                else {
                    nOutraCor++;
                    encontrouPecaLinha = false;
                }
            }
            if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[0][y] == invertePeca(corPeca) || tabuleiro[LARGURA_TABULEIRO-1][y] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);
        }

        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            encontrouPecaLinha = false;
            nCorPeca = 0;
            nOutraCor = 0;
            nConsecutivos = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    nCorPeca++;
                    if(encontrouPecaLinha) nConsecutivos++;
                    else encontrouPecaLinha = true;
                }
                else if(tabuleiro[x][y] == SEM_PECA) {
                    encontrouPecaLinha = false;
                }
                else {
                    nOutraCor++;
                    encontrouPecaLinha = false;
                }
            }
            if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[x][0] == invertePeca(corPeca) || tabuleiro[x][ALTURA_TABULEIRO-1] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);
        }

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[0][0] == invertePeca(corPeca) || tabuleiro[LARGURA_TABULEIRO-1][ALTURA_TABULEIRO-1] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x-1] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][x+1] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0 || (nOutraCor == 1 && (tabuleiro[4][0] == invertePeca(corPeca) || tabuleiro[0][4] == invertePeca(corPeca))))
                if(nCorPeca > 0)
                    pontos+=(nCorPeca+nConsecutivos-nOutraCor);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        encontrouPecaLinha = false;
        nCorPeca = 0;
        nOutraCor = 0;
        nConsecutivos = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                nCorPeca++;
                if(encontrouPecaLinha) nConsecutivos++;
                else encontrouPecaLinha = true;
            }
            else if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == SEM_PECA) {
                encontrouPecaLinha = false;
            }
            else {
                nOutraCor++;
                encontrouPecaLinha = false;
            }
        }
        if(nOutraCor == 0)
            if(nCorPeca > 0)
                pontos+=(nCorPeca+nConsecutivos);

        return pontos;
    }

    private float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        //ArvoreDeJogadas j = new ArvoreDeJogadas();
        //return j.geraPontosAleatorios();
        float pontosAlinhamentos = normalizaPontuacao(0.0f, 15.0f, (float)minPontos, (float)maxPontos, (float)numeroDeAlinhamentosComVazios(corPeca,tabuleiro));
        //float pontosMaximoAlinhado = normalizaPontuacao(0.0f, 4.0f, (float)minPontos, (float)maxPontos, (float)maximoAlinhado(corPeca,tabuleiro));
        return pontosAlinhamentos;
    }

    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        if(verificaVitoria(invertePeca(corPeca), tabuleiro)) {
            return minPontos;
        }
        else if(verificaVitoria(corPeca, tabuleiro)) {
            return maxPontos;
        }
        else{
            return geraCustoPeca(corPeca, tabuleiro, minPontos, maxPontos)*0.4f + geraCustoPeca(invertePeca(corPeca), tabuleiro, minPontos, maxPontos)*-0.6f;
        }        
    }
    public void constroiArvoreDeJogadas(int corPecaJogador, int corPecaAtual, ArvoreDeJogadas jogadas, int profundidadeMax, int maximoJogadas) {
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(jogadas.getCopiaTabuleiro());
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || verificaFimDeJogo(jogadas.getCopiaTabuleiro()) || maximoJogadas <= possiveisJogadas.size()) {
            jogadas.setPontos((int)geraCusto(corPecaJogador, jogadas.getCopiaTabuleiro(), jogadas.getMinPontos(), jogadas.getMaxPontos()));
            jogadas.setProfundidade(0);
        }
        else {
            for(int i=0; i < possiveisJogadas.size(); i++) {
                ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas();
                proximasJogadas.setDificulty(3);
                int[][] novoTabuleiro = criaCopiaTabuleiro(jogadas.getCopiaTabuleiro());
                novoTabuleiro = fazJogada(possiveisJogadas.get(i)[0], possiveisJogadas.get(i)[1], corPecaAtual, novoTabuleiro);
                proximasJogadas.setCopiaTabuleiro(novoTabuleiro);
                int maximoProximasJogadas = (int)(maximoJogadas-possiveisJogadas.size())/possiveisJogadas.size();
                if(corPecaAtual == PECA_BRANCA) {
                    constroiArvoreDeJogadas(corPecaJogador, PECA_PRETA, proximasJogadas, profundidadeMax-1, maximoProximasJogadas);
                }
                else {
                    constroiArvoreDeJogadas(corPecaJogador, PECA_BRANCA, proximasJogadas, profundidadeMax-1, maximoProximasJogadas);
                }
                jogadas.addFilho(proximasJogadas);
            }

            int maiorProfundidadeFilho = 0;
            for(int i=0; i < jogadas.getFilhos().size(); i++) {
                maiorProfundidadeFilho = Math.max(maiorProfundidadeFilho, jogadas.getFilho(i).getProfundidade());
            }
            jogadas.setProfundidade(maiorProfundidadeFilho+1);
        }
    }
    public ArvoreDeJogadas constroiArvoreDeJogadas(int corPeca, int profundidadeMax) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas();
        jogadas.setDificulty(3);
        jogadas.setCopiaTabuleiro(criaCopiaTabuleiro(getTabuleiro()));
        constroiArvoreDeJogadas(corPeca, corPeca, jogadas, profundidadeMax, MAXIMO_JOGADAS);
        return jogadas;
    }
    private int invertePeca(int peca) {
        if(peca == PECA_BRANCA) {
            return PECA_PRETA;
        }
        else {
            return PECA_BRANCA;
        }
    }
    private void minimizaDanos(int corPeca, int[][] tabuleiro) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas();
        jogadas.setDificulty(3);
        ArrayList<int[]> possiveisJogadas = listaPossiveisJogadas(tabuleiro);
        Collections.shuffle(possiveisJogadas);
        int pontuacaoMaxima = Integer.MIN_VALUE;
        int pontuacao;
        for(int i=0; i < possiveisJogadas.size(); i++) {
            int[][] novoTabuleiro = criaCopiaTabuleiro(tabuleiro);
            novoTabuleiro = fazJogada(possiveisJogadas.get(i)[0], possiveisJogadas.get(i)[1], invertePeca(corPeca), novoTabuleiro);
            pontuacao = (int)geraCusto(invertePeca(corPeca), novoTabuleiro, jogadas.getMinPontos(), jogadas.getMaxPontos());
            if(pontuacao > pontuacaoMaxima) {
                novoTabuleiro = fazJogada(possiveisJogadas.get(i)[0], possiveisJogadas.get(i)[1], corPeca, novoTabuleiro);
                pontuacaoMaxima = pontuacao;
                setTabuleiro(novoTabuleiro);
            }
        }
    }
    public float maquinaJoga(int corPeca, int profundidade) {
        ArvoreDeJogadas jogadas = constroiArvoreDeJogadas(corPeca, profundidade);
        Collections.shuffle(jogadas.getFilhos());
        jogadas.minimaxAlphaBeta();
        System.out.println("Profundidade: "+jogadas.getProfundidade());

        int[][] tabuleiroAntigo = criaCopiaTabuleiro(getTabuleiro());
        int pontuacaoMaxima = Integer.MIN_VALUE;
        int profundidadeMinima = Integer.MAX_VALUE;
        for(int i=0; i < jogadas.getFilhos().size(); i++) {
            if(jogadas.getFilho(i).isAcessado()) {
                if(pontuacaoMaxima < jogadas.getFilho(i).getPontos()) {
                    setTabuleiro(jogadas.getFilho(i).getCopiaTabuleiro());
                    pontuacaoMaxima = jogadas.getFilho(i).getPontos();
                    if(pontuacaoMaxima == jogadas.getMaxPontos()) {
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
                else if(jogadas.getFilho(i).getPontos() == jogadas.getMaxPontos()) {
                    if(jogadas.getFilho(i).getProfundidade() < profundidadeMinima) {
                        setTabuleiro(jogadas.getFilho(i).getCopiaTabuleiro());
                        profundidadeMinima = jogadas.getFilho(i).getProfundidade();
                    }
                }
            }
        }
        if(pontuacaoMaxima == jogadas.getMinPontos()) {
            minimizaDanos(corPeca, tabuleiroAntigo);
        }
        return normalizaPontuacao(jogadas.getMinPontos(), jogadas.getMaxPontos(), 0, 100, (float)pontuacaoMaxima);
    }
    
    private float normalizaPontuacao(float minimoAntigo, float maximoAntigo, float minimoNovo, float maximoNovo, float valor){
        return ((valor-minimoAntigo)/(maximoAntigo-minimoAntigo) * (maximoNovo-minimoNovo) + minimoNovo);
    }

    public class EventosMouse extends MouseAdapter {
        private int[] posClick;
        public int[] getPosClick() {
            return posClick;
        }
        public void setPosClick(int[] posClick) {
            this.posClick = posClick;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] pos = {e.getX(), e.getY()};
            setPosClick(pos);
            if(isVezDoPlayer()) {
                int[] posJogada = {(int)(getPosClick()[0]*LARGURA_TABULEIRO)/LARGURA_TELA,
                                 (int)(getPosClick()[1]*ALTURA_TABULEIRO)/ALTURA_TELA};
                if(verificaJogada(posJogada[0], posJogada[1])) {
                    fazJogada(posJogada[0], posJogada[1],getPecaPlayer());
                    setVezDoPlayer(false);
                }
            }
        }
    }

    public boolean verificaSimetriaVertical(int tabuleiro [][]){
        for (int x = 0; x < 2; x++){
            for(int y = 0; y < 5; y++){
                if (tabuleiro[x][y] != tabuleiro[4-x][y]) return false;
            }
        }
        //System.out.println("Simetria Vertical");
        return true;
    }

    public boolean verificaSimetriaHorizontal(int tabuleiro [][]){
        for (int y = 0; y < 2; y++){
            for(int x = 0; x < 5; x++){
                if (tabuleiro[x][y] != tabuleiro[x][4-y]) return false;
            }
        }
        //System.out.println("Simetria Horizontal");
        return true;
    }

    public boolean verificaSimetriaDiagonal(int tabuleiro [][]){
        if (tabuleiro[0][1] != tabuleiro[1][0]) return false;
        if (tabuleiro[0][2] != tabuleiro[2][0]) return false;
        if (tabuleiro[0][3] != tabuleiro[3][0]) return false;
        if (tabuleiro[0][4] != tabuleiro[4][0]) return false;
        if (tabuleiro[1][2] != tabuleiro[2][1]) return false;
        if (tabuleiro[1][3] != tabuleiro[3][1]) return false;
        if (tabuleiro[1][4] != tabuleiro[4][1]) return false;
        if (tabuleiro[2][3] != tabuleiro[3][2]) return false;
        if (tabuleiro[2][4] != tabuleiro[4][2]) return false;
        if (tabuleiro[3][4] != tabuleiro[4][3]) return false;
        //System.out.println("Simetria Diagonal");
        return true;
    }

    public boolean verificaSimetriaOutraDiagonal(int tabuleiro [][]){
        if (tabuleiro[0][0] != tabuleiro[4][4]) return false;
        if (tabuleiro[0][1] != tabuleiro[3][4]) return false;
        if (tabuleiro[0][2] != tabuleiro[2][4]) return false;
        if (tabuleiro[0][3] != tabuleiro[1][4]) return false;
        if (tabuleiro[1][0] != tabuleiro[4][3]) return false;
        if (tabuleiro[1][1] != tabuleiro[3][3]) return false;
        if (tabuleiro[1][2] != tabuleiro[2][3]) return false;
        if (tabuleiro[2][0] != tabuleiro[4][2]) return false;
        if (tabuleiro[2][1] != tabuleiro[3][2]) return false;
        if (tabuleiro[3][0] != tabuleiro[4][1]) return false;
        //System.out.println("Simetria Outra Diagonal");
        return true;
    }

}
