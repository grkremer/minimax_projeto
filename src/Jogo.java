import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Jogo extends JPanel implements ActionListener {
    public static final int LARGURA_TELA = 600;
    public static final int ALTURA_TELA = 600;
    public static final int ALTURA_TABULEIRO = 5;
    public static final int LARGURA_TABULEIRO = 5;
    public static final int SEM_PECA = 0;
    public static final int PECA_BRANCA = 1;
    public static final int PECA_PRETA = 2;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*LARGURA_TABULEIRO);
    private static final int DELAY_TIMER = 75;
    public static final int DELAY_JOGADA = 750;
    private Timer timer;
    private String nome;
    private int[][] tabuleiro;
    private int profundidade;
    private int maximoJogadas = Integer.MAX_VALUE;
    private int pecaPlayer = PECA_BRANCA;
    private boolean vezDoPlayer = false;
    private Jogada jogadaDoPlayer;
    private boolean selecionado = false;
    private int[] posSelecionado = {0, 0};

    Jogo() {
        setPreferredSize(new Dimension(LARGURA_TELA,ALTURA_TELA));
        setFocusable(true);
        setTabuleiro(new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO]);
        setBackground(Color.white);
        addMouseListener(new EventosMouse());
    }

    //  getters e setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int[][] getTabuleiro() {
        return tabuleiro;
    }
    public void setTabuleiro(int[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public Timer getTimer() {
        return timer;
    }
    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    public boolean isVezDoPlayer() {
        return vezDoPlayer;
    }
    public void setVezDoPlayer(boolean vezDoPlayer) {
        this.vezDoPlayer = vezDoPlayer;
    }
    public boolean isSelecionado() {
        return selecionado;
    }
    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
    public int[] getPosSelecionado() {
        return posSelecionado;
    }
    public void setPosSelecionado(int[] posSelecionado) {
        this.posSelecionado = posSelecionado;
    }
    public Jogada getJogadaDoPlayer() {
        return jogadaDoPlayer;
    }
    public void setJogadaDoPlayer(Jogada jogadaDoPlayer) {
        this.jogadaDoPlayer = jogadaDoPlayer;
    }
    public int getPecaPlayer() {
        return pecaPlayer;
    }
    public void setPecaPlayer(int pecaPlayer) {
        this.pecaPlayer = pecaPlayer;
    }
    public int getProfundidade() {
        return profundidade;
    }
    public void setProfundidade(int profundidade) {
        this.profundidade = profundidade;
    }
    public int getMaximoJogadas() {
        return maximoJogadas;
    }
    public void setMaximoJogadas(int maximoJogadas) {
        this.maximoJogadas = maximoJogadas;
    }

    //  funções da engine
    public void iniciaTimer() {
        setTimer(new Timer(DELAY_TIMER, this));
        getTimer().start();
    }
    public void paraTimer() {
        getTimer().stop();
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenhaTabuleiro(g);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }  
    
    //  funções de uso geral
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
                        if(isSelecionado() && getPosSelecionado()[0] == x && getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        else {
                            g.setColor(Color.black);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        break;
                    case PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, ALTURA_TABULEIRO);
                        g.setColor(Color.black);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        if(isSelecionado() && getPosSelecionado()[0] == x && getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
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
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }
    public int[][] criaCopiaTabuleiro(int[][] tabuleiro) {
        int[][] novoTabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                novoTabuleiro[x][y] = tabuleiro[x][y];
            }
        }
        return novoTabuleiro;
    }
    static public String pecaParaString(int peca) {
        switch(peca) {
            case PECA_BRANCA:
                return "PEÇA BRANCA";
            case PECA_PRETA:
                return "PEÇA PRETA";
            case SEM_PECA:
            default:
                return "SEM PEÇA"; 
        }
    }
    public int invertePeca(int peca) {
        if(peca == PECA_BRANCA) {
            return PECA_PRETA;
        }
        else {
            return PECA_BRANCA;
        }
    }
    public void inserePeca(int[] posicao, int corPeca, int[][] tabuleiro) {
        tabuleiro[posicao[0]][posicao[1]] = corPeca;
    }
    public void fazMovimento(int[][] movimento, int[][] tabuleiro) {
        int xInicial = movimento[0][0];
        int yInicial = movimento[0][1];
        int xFinal = movimento[1][0];
        int yFinal = movimento[1][1];
        tabuleiro[xFinal][yFinal] = tabuleiro[xInicial][yInicial];
        tabuleiro[xInicial][yInicial] = SEM_PECA;
    } 
    public void retiraPecas(ArrayList<int[]> pecasEliminadas, int[][] tabuleiro) {
        for(int[] peca : pecasEliminadas) {
            tabuleiro[peca[0]][peca[1]] = SEM_PECA;
        }
    }
    public void fazJogada(Jogada jogada) throws InterruptedException {
        if(jogada.getMovimentos().isEmpty()) {
            inserePeca(jogada.getPosicao(), jogada.getCorPeca(), getTabuleiro());
        }
        else {
            for(int i=0; i<jogada.getMovimentos().size(); i++){
                fazMovimento(jogada.getMovimentos().get(i), getTabuleiro());
                Thread.sleep(DELAY_JOGADA);
            }
            retiraPecas(jogada.getPecasEliminadas(), getTabuleiro());
        }
        jogada.print();
        Thread.sleep(DELAY_JOGADA);
    } 
    public void fazJogada(Jogada jogada, int[][] tabuleiro) {
        if(jogada.getMovimentos().isEmpty()) {
            inserePeca(jogada.getPosicao(), jogada.getCorPeca(), tabuleiro);
        }
        else {
            for(int i=0; i<jogada.getMovimentos().size(); i++){
                fazMovimento(jogada.getMovimentos().get(i), tabuleiro);
            }
            retiraPecas(jogada.getPecasEliminadas(), tabuleiro);
        }
    } 
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
    }
    public float normalizaPontuacao(float minimoAntigo, float maximoAntigo, float minimoNovo, float maximoNovo, float valor){
        return ((valor-minimoAntigo)/(maximoAntigo-minimoAntigo) * (maximoNovo-minimoNovo) + minimoNovo);
    }
    public int contaPecas(int corPeca, int[][] tabuleiro) {
        int cont = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) cont++;
            }
        }
        return cont;
    }
    public Jogada jogadaDaMaquina(int corPeca, int profundidade) {
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, getMaximoJogadas());
        Collections.shuffle(jogadas.getFilhos());
        jogadas.minimaxAlphaBeta();

        int pontuacaoMaxima = Integer.MIN_VALUE;
        int profundidadeMinima = Integer.MAX_VALUE;
        Jogada melhorJogada = jogadas.getFilho(0).getJogada();
        for(int i=0; i < jogadas.getFilhos().size(); i++) {
            if(pontuacaoMaxima < jogadas.getFilho(i).getPontos()) {
                melhorJogada = jogadas.getFilho(i).getJogada();
                pontuacaoMaxima = jogadas.getFilho(i).getPontos();
                if(pontuacaoMaxima == jogadas.MAX_PONTOS) {
                    profundidadeMinima = jogadas.getFilho(i).calculaProfundidade();
                }
            }
            else if(jogadas.getFilho(i).getPontos() == jogadas.MAX_PONTOS) {
                if(jogadas.getFilho(i).calculaProfundidade() < profundidadeMinima) {
                    melhorJogada = jogadas.getFilho(i).getJogada();
                    profundidadeMinima = jogadas.getFilho(i).calculaProfundidade();
                }
            }
        }
        if(pontuacaoMaxima == jogadas.MIN_PONTOS) {
            melhorJogada = jogadaDanoMinimo(melhorJogada, corPeca);
        }
        float chance = normalizaPontuacao(jogadas.MIN_PONTOS, jogadas.MAX_PONTOS, 0, 100, (float)pontuacaoMaxima);
        System.out.println("Chance de vitória: "+chance+"%");
        return melhorJogada;    
    }
    private void playerFazJogada() throws InterruptedException {
        setVezDoPlayer(true);
        while(isVezDoPlayer()) {
            Thread.sleep(1);
        }
        fazJogada(getJogadaDoPlayer());
    }
    public void partidaBotXPlayer() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        while(!verificaFimDeJogo(getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            if(getPecaPlayer() == PECA_BRANCA) {
                playerFazJogada();
                if(!verificaVitoria(PECA_BRANCA, getTabuleiro())) {
                    System.out.println("Vez das peças pretas");
                    fazJogada(jogadaDaMaquina(PECA_PRETA, getProfundidade()));
                }
            }
            else {
                fazJogada(jogadaDaMaquina(PECA_BRANCA, getProfundidade()));
                if(!verificaVitoria(PECA_BRANCA, getTabuleiro())) {
                    System.out.println("Vez das peças pretas");
                    playerFazJogada();
                }
            }
        }
        paraTimer();
    }
    public void partidaBotXBot() throws InterruptedException {
        inicializaTabuleiro();
        iniciaTimer();
        while(!verificaFimDeJogo(getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            fazJogada(jogadaDaMaquina(PECA_BRANCA, getProfundidade()), getTabuleiro());     
            if(!verificaVitoria(PECA_BRANCA, getTabuleiro())) {
                System.out.println("Vez das peças pretas");
                fazJogada(jogadaDaMaquina(PECA_PRETA, getProfundidade()), getTabuleiro());
            }
        }
        paraTimer();
    }

    //  funções específicas (para implementar com override)
    public boolean verificaJogada(Jogada jogada, int[][] tabuleiro) {
        return false;
    }
    public boolean verificaMovimento(int[][] movimento, int[][] tabuleiro) {
        return false;
    }
    public ArrayList<Jogada> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {     
        return new ArrayList<Jogada>();
    }
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return true;
    }
    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        return 0.0f;
    }
    public void inicializaTabuleiro() {

    }
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        return true;
    }
    public Jogada jogadaDanoMinimo(Jogada antigaMelhorJogada, int corPeca) {
        return antigaMelhorJogada;
    }
    public void interpretaJogadaPlayer(int[] posClick) {

    }

    // classe de que captura eventos do mouse
    public class EventosMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] posClick = {(int)(e.getX()*LARGURA_TABULEIRO)/LARGURA_TELA, (int)(e.getY()*ALTURA_TABULEIRO)/ALTURA_TELA};
            if(isVezDoPlayer()) {
                interpretaJogadaPlayer(posClick);
            }
        }
    }
}
