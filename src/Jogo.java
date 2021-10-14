import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

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
        LogArvoreJogo log = new LogArvoreJogo();
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, getMaximoJogadas());
        //Collections.shuffle(jogadas.getFilhos());
        jogadas.minimax();
        log.AvaliaArvore(jogadas);

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
        System.out.println(log.toString());
        return melhorJogada;    
    }

    public Jogada jogadaDaMaquina2(int corPeca, int profundidade) {
        LogArvoreJogo log = new LogArvoreJogo();
        //ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, getMaximoJogadas());
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, 5, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //Collections.shuffle(jogadas.getFilhos());
        //jogadas.minimaxAlphaBeta();
        log.AvaliaArvore(jogadas);

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
        System.out.println(log.toString());
        return melhorJogada;    
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
    
    private void minimaxFazJogada(int peca) {
        
        Jogada jogada = jogadaDaMaquina(peca, getProfundidade());
        fazJogada(jogada, getTabuleiro());
        getHistoricoJogadas().add(jogada);
    }

    private void podaFazJogada(int peca) {
        Minimax ag = new Minimax(6);
        //this, getTabuleiro(), corPeca, corPeca, 5, true, Integer.MIN_VALUE, Integer.MAX_VALUE
        long startTime = System.currentTimeMillis();
        Jogada jogada = ag.Poda(this, getTabuleiro(), peca, peca);//jogadaDaMaquina(peca, getProfundidade());
        long endTime = System.currentTimeMillis();
        float total = (endTime - startTime)/1000f;
        System.out.println( ag );
        System.out.println( total + "s");

        fazJogada(jogada, getTabuleiro());
        getHistoricoJogadas().add(jogada);
    }


    private void minimaxABFazJogada(int peca) {
        Jogada jogada = jogadaDaMaquina2(peca, getProfundidade());
        
        fazJogada(jogada, getTabuleiro());
        getHistoricoJogadas().add(jogada);
    }
    private void monteCarloFazJogada(int peca) {
        LogArvoreMonteCarlo log = new LogArvoreMonteCarlo();
        ArvoreMonteCarlo arvore = new ArvoreMonteCarlo(this, 10000, 1);
        Jogada jogada = arvore.Movimentar(new Estado(getTabuleiro(), peca, false, 0, peca, 0));
        log.AvaliaArvore(arvore.getRaiz());
        System.out.println(log);
        fazJogada(jogada, getTabuleiro());
        getHistoricoJogadas().add(jogada);
    }

    public Jogada monteCarlo(int peca){
        LogArvoreMonteCarlo log = new LogArvoreMonteCarlo();
        ArvoreMonteCarlo arvore = new ArvoreMonteCarlo(this, 10000, 1);
        Jogada jogada = arvore.Movimentar(new Estado(getTabuleiro(), peca, false, 0, peca, 0));
        log.AvaliaArvore(arvore.getRaiz());
        System.out.println(log);
        return jogada;
        //fazJogada(jogada, getTabuleiro());
        //getHistoricoJogadas().add(jogada);
    }
    
    public void monteCarloXMinimax() throws InterruptedException {
        setHistoricoJogadas(new ArrayList<Jogada>());
        inicializaTabuleiro();
        while(!verificaFimDeJogo(getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            monteCarloFazJogada(PECA_BRANCA);
            //Thread.sleep(4000);
            if(!verificaFimDeJogo(getTabuleiro())) {
                System.out.println("Vez das peças pretas");
                //minimaxFazJogada(PECA_PRETA);
                podaFazJogada(PECA_PRETA);
                //Thread.sleep(4000);
            }
        }
        salvaLogPartida();
    }

    public void MinimaxXMinimax() throws InterruptedException {
        setHistoricoJogadas(new ArrayList<Jogada>());
        inicializaTabuleiro();
        while(!verificaFimDeJogo(getTabuleiro())) {
            System.out.println("Vez das peças brancas");
            podaFazJogada(PECA_PRETA);
                
            Thread.sleep(1000);
            if(!verificaFimDeJogo(getTabuleiro())) {
                System.out.println("Vez das peças pretas");
                minimaxFazJogada(PECA_BRANCA);
            
                Thread.sleep(1000);
            }
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
    public Jogada jogadaDanoMinimo(Jogada antigaMelhorJogada, int corPeca) {
        return antigaMelhorJogada;
    }
    public Movimento.Acao proximaAcao(int corPeca, int[][] tabuleiro) {
        return Movimento.Acao.MOVE;
    }
}
