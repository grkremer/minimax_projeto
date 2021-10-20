

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Jogo {
    public static final int ALTURA_TABULEIRO = 5;
    public static final int LARGURA_TABULEIRO = 5;
    public static final int SEM_PECA = 0;
    public static final int PECA_BRANCA = 1;
    public static final int PECA_PRETA = 2;
    
    private String nome;
    private int[][] tabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
    private int profundidade;
    private int maximoJogadas = Integer.MAX_VALUE;
    private int pecaPlayer = PECA_BRANCA;
    private ArrayList<Jogada> historicoJogadas;

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
    public int getPecaPlayer() {
        return pecaPlayer;
    }
    public void setPecaPlayer(int pecaPlayer) {
        this.pecaPlayer = pecaPlayer;
    }
    public ArrayList<Jogada> getHistoricoJogadas() {
        return historicoJogadas;
    }
    public void setHistoricoJogadas(ArrayList<Jogada> historicoJogadas) {
        this.historicoJogadas = historicoJogadas;
    }

    //  funções de uso geral
    public int[][] criaCopiaTabuleiro(int[][] tabuleiro) {
        int[][] novoTabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                novoTabuleiro[x][y] = tabuleiro[x][y];
            }
        }
        return novoTabuleiro;
    }
    public int invertePeca(int peca) {
        if(peca == PECA_BRANCA) {
            return PECA_PRETA;
        }
        else {
            return PECA_BRANCA;
        }
    }
    public int pecaEntrePecas(int x1, int y1, int x2, int y2, int[][] tabuleiro) {
        int[] posNovo = posPecaEntrePecas(x1, y1, x2, y2);
        return tabuleiro[posNovo[0]][posNovo[1]];
    }
    public static int[] posPecaEntrePecas(int x1, int y1, int x2, int y2) {
        int xNovo, yNovo;
        int[] posNovo = new int[2];

        if(x1 > x2) xNovo = x2 + 1;
        else if(x2 > x1) xNovo = x1 + 1;
        else xNovo = x1;

        if(y1 > y2) yNovo = y2 + 1;
        else if(y2 > y1) yNovo = y1 + 1;
        else yNovo = y1;

        posNovo[0] = xNovo;
        posNovo[1] = yNovo;
        return posNovo;
    }
    public static int[] posPecaEliminada(int[] posInicial, int[] posFinal) {
        return posPecaEntrePecas(posInicial[0], posInicial[1], posFinal[0], posFinal[1]);
    }
    public static boolean comeuPeca(int[] posInicial, int[] posFinal) {
        int xInicial = posInicial[0];
        int yInicial = posInicial[1];
        int xFinal = posFinal[0];
        int yFinal = posFinal[1];
        return Math.abs(xFinal - xInicial) == 2 || Math.abs(yFinal - yInicial) == 2;
    }
    public void removePecas(ArrayList<int[]> pecasEliminadas, int[][] tabuleiro) {
        for(int[] peca : pecasEliminadas) {
            tabuleiro[peca[0]][peca[1]] = SEM_PECA;
        }
    }
    
    public void movePeca(int[] posInicial, int[] posFinal, int[][] tabuleiro) {
        int xInicial = posInicial[0];
        int yInicial = posInicial[1];
        int xFinal = posFinal[0];
        int yFinal = posFinal[1];
        tabuleiro[xFinal][yFinal] = tabuleiro[xInicial][yInicial];
        tabuleiro[xInicial][yInicial] = SEM_PECA;
    } 
    public void inserePeca(int[] posicao, int corPeca, int[][] tabuleiro) {
        tabuleiro[posicao[0]][posicao[1]] = corPeca;
    }
    public void removePeca(int[] posicao, int[][] tabuleiro) {
        tabuleiro[posicao[0]][posicao[1]] = SEM_PECA;
    }
    public void fazMovimento(Movimento movimento, int[][] tabuleiro) {
        switch(movimento.getAcao()) {
            case INSERE:
                inserePeca(movimento.getPosicao1(), movimento.getCorPeca(), tabuleiro);
                break;
            case MOVE:
                movePeca(movimento.getPosicao1(), movimento.getPosicao2(), tabuleiro);
                break;
            case REMOVE:
                removePeca(movimento.getPosicao1(), tabuleiro);
                break;
            default:
                break;
        }
    }
    public void fazJogada(Jogada jogada, int[][] tabuleiro) {
        for(Movimento movimento : jogada.getMovimentos()) {
            fazMovimento(movimento, tabuleiro);
        }
    } 
    public boolean verificaJogada(Jogada jogada, int[][] tabuleiro) {
        for(Movimento movimento : jogada.getMovimentos()) {
            if(!verificaMovimento(movimento, tabuleiro))
                return false;
        }
        return true;
    }
    public boolean estaNosLimites(int x, int y) {
        if(x>=0 && x<LARGURA_TABULEIRO && y>=0 && y<ALTURA_TABULEIRO)
            return true;
        else
            return false;
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


    
    public float normalizaPontuacao(float minimoAntigo, float maximoAntigo, float minimoNovo, float maximoNovo, float valor){
        return ((valor-minimoAntigo)/(maximoAntigo-minimoAntigo) * (maximoNovo-minimoNovo) + minimoNovo);
    }
    public Jogada jogadaDanoMinimo(Jogada antigaMelhorJogada, int corPeca) {
        return antigaMelhorJogada;
    }
    
    public void salvaLogPartida() {
        try {
            PrintWriter arquivo = new PrintWriter("logs/log.txt");
            for(Jogada jogada : getHistoricoJogadas()) {
                arquivo.println(jogada.toString());
            }
            arquivo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void jogar(Agente jogador1, Agente jogador2) throws InterruptedException{
        setHistoricoJogadas(new ArrayList<Jogada>());
        inicializaTabuleiro();
        
        int rodada = PECA_BRANCA;
        while(!verificaFimDeJogo(getTabuleiro())) {
            
            Jogada j = null;
            //if(rodada == PECA_BRANCA)
            if(jogador1.getCorPeca() == rodada)
            {
                System.out.println("Vez do jogador1");
                j = jogador1.Mover(this, getTabuleiro());
                System.out.println(jogador1);
                
            }else{
                System.out.println("Vez do jogador2");
                j = jogador2.Mover(this, getTabuleiro());
                System.out.println(jogador2);
            }
            fazJogada(j, getTabuleiro());
            getHistoricoJogadas().add(j);
            rodada = invertePeca(rodada);
            //Thread.sleep(2000);

            
        }
        salvaLogPartida();

    }

    public void moveAoContrarioPeca(int[] posInicial, int[] posFinal, int[][] tabuleiro) {
        int xInicial = posInicial[0];
        int yInicial = posInicial[1];
        int xFinal = posFinal[0];
        int yFinal = posFinal[1];
        tabuleiro[xInicial][yInicial] = tabuleiro[xFinal][yFinal];
        tabuleiro[xFinal][yFinal] = SEM_PECA;
    } 
    public void desfazMovimento(Movimento movimento, int[][] tabuleiro) {
        switch(movimento.getAcao()) {
            case INSERE:
                removePeca(movimento.getPosicao1(), tabuleiro);
                break;
            case MOVE:
                moveAoContrarioPeca(movimento.getPosicao1(), movimento.getPosicao2(), tabuleiro);
                break;
            case REMOVE:
                inserePeca(movimento.getPosicao1(), invertePeca(movimento.getCorPeca()), tabuleiro);
                break;
            default:
                break;
        }
    }
    public void desfazJogada(Jogada jogada) {
        for(int i=jogada.getMovimentos().size()-1; i>=0; i--) {
            Movimento movimento = jogada.getMovimentos().get(i);
            desfazMovimento(movimento, getTabuleiro());
        }
    }
    
    //  funções específicas (para implementar com override)  
    public boolean verificaMovimento(Movimento movimento, int[][] tabuleiro) {
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
    
    public Movimento.Acao proximaAcao(int corPeca, int[][] tabuleiro) {
        return Movimento.Acao.MOVE;
    }
}
