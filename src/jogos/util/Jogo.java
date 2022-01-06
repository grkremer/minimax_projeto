package jogos.util;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

import agentes.util.Agente;


public class Jogo {
    public static final int ALTURA_TABULEIRO = 5;
    public static final int LARGURA_TABULEIRO = 5;
    public static final int SEM_PECA = 0;
    public static final int PECA_BRANCA = 1;
    public static final int PECA_PRETA = 2;
    
    private String nome;
    private int[][] tabuleiro = new int[LARGURA_TABULEIRO][ALTURA_TABULEIRO];
    private int delayJogada = 0;
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

    public int[][] getCopiaTabuleiro(){
        int[][] copiaTabuleiro = new int[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                copiaTabuleiro[i][j] = tabuleiro[i][j];
            }
        }
        return copiaTabuleiro;
    }

    public void setTabuleiro(int[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public int getDelayJogada() {
        return delayJogada;
    }
    public void setDelayJogada(int delayJogada) {
        this.delayJogada = delayJogada;
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
    public void fazJogada(Jogada jogada, int[][] tabuleiro, boolean usaDelay) throws InterruptedException {
        for(Movimento movimento : jogada.getMovimentos()) {
            fazMovimento(movimento, tabuleiro);
            if(usaDelay)
                Thread.sleep(getDelayJogada());
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
    public Jogada jogadaDanoMinimo(Jogada antigaMelhorJogada, int corPeca) throws InterruptedException{
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
            if(jogador1.getCorPeca() == rodada)
            {
                System.out.println("\n*****************************\nVez do jogador1");
                j = jogador1.Mover(this, getTabuleiro());
                System.out.println(jogador1);
                
            }else{
                System.out.println("\n*****************************\nVez do jogador2");
                j = jogador2.Mover(this, getTabuleiro());
                System.out.println(jogador2);
            }
            fazJogada(j, getTabuleiro(), true);
            getHistoricoJogadas().add(j);
            rodada = invertePeca(rodada);
            Thread.sleep(2000);

            
        }

        if(verificaVitoria(jogador1.getCorPeca(), tabuleiro)){
            System.out.println("VITORIA jogador 1");
        }else{
            System.out.println("VITORIA jogador 2");
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
    public void desfazJogada(Jogada jogada, boolean usaDelay) throws InterruptedException {
        for(int i=jogada.getMovimentos().size()-1; i>=0; i--) {
            Movimento movimento = jogada.getMovimentos().get(i);
            desfazMovimento(movimento, getTabuleiro());
            if(usaDelay)
                Thread.sleep(getDelayJogada());
        }
    }

    private boolean selecionado = false;
    private int[] posSelecionado = {0, 0};
    public int[] getPosSelecionado() {
        return posSelecionado;
    }
    public void setPosSelecionado(int[] posSelecionado) {
        this.posSelecionado = posSelecionado;
    }
    public boolean isSelecionado() {
        return selecionado;
    }
    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
    
    private Jogada jogadaDaLista(int xInicial, int yInicial, int xFinal, int yFinal, ArrayList<Jogada> possiveisJogadas) {
        for(Jogada jogada : possiveisJogadas) {
            if(jogada.getMovimentos().get(0).getPosicao1()[0] == xInicial && jogada.getMovimentos().get(0).getPosicao1()[1] == yInicial) {
                int i=0;
                boolean move = true;
                while(move && i < jogada.getMovimentos().size()) {
                    move = jogada.getMovimentos().get(i).getAcao() == Movimento.Acao.MOVE;
                    if(move) i++;
                }               
                i--;
                if(jogada.getMovimentos().get(i).getPosicao2()[0] == xFinal && jogada.getMovimentos().get(i).getPosicao2()[1] == yFinal) {
                    return jogada;
                }
            }
        }
        return null;
    }
    private Jogada humanoMovePeca(int[] posClick, int corPeca) {
        if(!isSelecionado()) {
            setPosSelecionado(posClick);
            if(getTabuleiro()[posClick[0]][posClick[1]] == corPeca) {
                setSelecionado(true);
            }
            return null;
        }
        else {
            ArrayList<Jogada> possiveisJogadas = listaPossiveisJogadas(corPeca, getTabuleiro());
            Jogada jogada = jogadaDaLista(getPosSelecionado()[0], getPosSelecionado()[1], posClick[0], posClick[1], possiveisJogadas);
            if(!Objects.isNull(jogada)) {
                setSelecionado(false);
                return jogada;
            }
            else if(getTabuleiro()[posClick[0]][posClick[1]] == corPeca) {
                setPosSelecionado(posClick);
                return null;
            }
            else {
                setSelecionado(false);
                return null;
            }
        }
    }
    
    private Jogada humanoIserePeca(int[] posClick, int corPeca) {
        Jogada jogada = new Jogada(corPeca, posClick);
        if(verificaJogada(jogada, getTabuleiro())) {
            return jogada;
        }
        return null;
    }
    public Jogada humanoFazJogada(int[] posClick, int corPeca) {
        switch(proximaAcao(corPeca, getTabuleiro())) {
            case MOVE:
                return humanoMovePeca(posClick, corPeca);
            case INSERE:
                return humanoIserePeca(posClick, corPeca);
            default:
                return null;
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
