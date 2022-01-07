package agentes.backup;

import java.util.Random;

import jogos.util.Jogada;
import jogos.util.Jogo;

import java.util.ArrayList;
import java.util.List;

public class ArvoreDeJogadas {
    private Jogada jogada;
    private int pontos;
    private List<ArvoreDeJogadas> filhos;
    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    
    public Jogada getJogada() {
        return jogada;
    }
    public void setJogada(Jogada jogada) {
        this.jogada = jogada;
    }
    public int getPontos() {
        return pontos;
    }
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    public List<ArvoreDeJogadas> getFilhos() {
        return filhos;
    }
    public void setFilhos(List<ArvoreDeJogadas> filhos) {
        this.filhos = filhos;
    }
    public ArvoreDeJogadas getFilho(int index) {
        return filhos.get(index);
    }
    public void setFilho(int index, ArvoreDeJogadas filho) {
        this.filhos.set(index, filho);
    }
    public void addFilho(ArvoreDeJogadas filho) {
        this.filhos.add(filho);
    }

    public ArvoreDeJogadas(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidadeMax, int maximoJogadas) throws InterruptedException {
        setFilhos(new ArrayList<ArvoreDeJogadas>());
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || jogo.verificaFimDeJogo(tabuleiro) || maximoJogadas <= possiveisJogadas.size()) {
            setPontos((int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS));
        }
        else {
            for(int i=0; i < possiveisJogadas.size(); i++) {
                int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
                jogo.fazJogada(possiveisJogadas.get(i), novoTabuleiro, false);
                int maximoProximasJogadas = (int)(maximoJogadas-possiveisJogadas.size())/possiveisJogadas.size();
                ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1, maximoProximasJogadas);
                proximasJogadas.setJogada(possiveisJogadas.get(i));
                this.addFilho(proximasJogadas);
            }
        }
    }

    

    public ArvoreDeJogadas(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidadeMax, boolean estaMaximizando, float alpha, float beta) throws InterruptedException {
        setFilhos(new ArrayList<ArvoreDeJogadas>());
        ArrayList<Jogada> possiveisJogadas = jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro);
        if(profundidadeMax == 0 || possiveisJogadas.size() == 0 || jogo.verificaFimDeJogo(tabuleiro)) {
            setPontos((int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS));
        }
        else {
            if(estaMaximizando) {
                int pontuacaoMaxima = Integer.MIN_VALUE;
                int pontuacaoFilho;
                for(Jogada jogada : possiveisJogadas) {
                    int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
                    jogo.fazJogada(jogada, novoTabuleiro, false);
                    ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1, false, alpha, beta);
                    proximasJogadas.setJogada(jogada);
                    this.addFilho(proximasJogadas);
                    pontuacaoFilho = proximasJogadas.getPontos();
                    pontuacaoMaxima = Math.max(pontuacaoMaxima, pontuacaoFilho);
                    if(pontuacaoMaxima >= beta)
                        break;
                    alpha = Math.max(alpha, pontuacaoMaxima);
                }
                setPontos(pontuacaoMaxima);
            }
            else {
                int pontuacaoMinima = Integer.MAX_VALUE;
                int pontuacaoFilho;
                for(Jogada jogada : possiveisJogadas) {
                    int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
                    jogo.fazJogada(jogada, novoTabuleiro, false);
                    ArvoreDeJogadas proximasJogadas = new ArvoreDeJogadas(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax-1, true, alpha, beta);
                    proximasJogadas.setJogada(jogada);
                    this.addFilho(proximasJogadas);

                    pontuacaoFilho = proximasJogadas.getPontos();
                    pontuacaoMinima = Math.min(pontuacaoMinima, pontuacaoFilho);
                    if(pontuacaoMinima <= alpha) 
                        break;
                    beta = Math.min(beta, pontuacaoMinima);
                }
                setPontos(pontuacaoMinima);
            }
        }
    }
    
    public ArvoreDeJogadas(int profundidade) {
        geraArvoreAleatoria(profundidade);
    }
    private int randomInt(float min, float max){
        Random random = new Random();
        return random.ints((int)min,(int)(max+1)).findFirst().getAsInt();
    }
    public int geraPontosAleatorios() {
        return randomInt(MIN_PONTOS, MAX_PONTOS);
    }
    public void geraArvoreAleatoria(int profundidade) {
        if(profundidade == 0) {
            setPontos(geraPontosAleatorios());
        }
        else {
            int numeroDeFilhos = randomInt(2,4);
            for(int i=0; i<numeroDeFilhos; i++) {
                addFilho(new ArvoreDeJogadas(profundidade-1));
            }
        }
    }

    public int calculaProfundidade() {
        if(getFilhos().isEmpty()) {
            return 0;
        }
        else {
            int profundidadeFilhos = 0;
            for(ArvoreDeJogadas filho : getFilhos()) {
                profundidadeFilhos = Math.max(profundidadeFilhos, filho.calculaProfundidade());
            }
            return profundidadeFilhos+1;
        }
    }
    private int numeroNosNivel(int nivelAlvo, int nivelAtual) {
        if(nivelAlvo == nivelAtual){
            return 1;
        }
        else{
            int resultado = 0;
            for(int i=0; i<getFilhos().size(); i++) {
                resultado += getFilho(i).numeroNosNivel(nivelAlvo, nivelAtual+1);
            }
            return resultado;
        }
    }
    public int numeroNosNivel(int nivel) {
        return numeroNosNivel(nivel, 0);
    }
    public int numeroNosTotal() {
        if(getFilhos().isEmpty()) {
            return 1;
        }
        else {
            int resultado = 1;
            for(int i=0; i<getFilhos().size(); i++) {
                resultado += getFilho(i).numeroNosTotal();
            }
            return resultado;
        }
    }

    public void printaArvore() {
        System.out.println(getPontos()+" ");
        for(int i=0; i<getFilhos().size(); i++) {
            getFilho(i).printaArvore();
        }
    }
    public float getMenorFolha() {
        if (getFilhos().isEmpty()) {
            return getPontos();
        }
        else {
            float menor = getFilho(0).getMenorFolha();
            for(int i=1; i<getFilhos().size(); i++) {
                menor = Math.min(menor,getFilho(i).getMenorFolha());
            }
            return menor;
        }
    }
    private int minimax(boolean estaMaximizando) {
        if(!getFilhos().isEmpty()) {
            if(estaMaximizando) {
                int pontuacaoMaxima = Integer.MIN_VALUE;
                int pontuacaoFilho;
                for(int i=0; i<getFilhos().size(); i++) {
                    pontuacaoFilho = getFilho(i).minimax(false);
                    pontuacaoMaxima = Math.max(pontuacaoMaxima, pontuacaoFilho);
                }
                setPontos(pontuacaoMaxima);
                return pontuacaoMaxima;
            }
            else {
                int pontuacaoMinima = Integer.MAX_VALUE;
                int pontuacaoFilho;
                for(int i=0; i<getFilhos().size(); i++) {
                    pontuacaoFilho = getFilho(i).minimax(true);
                    pontuacaoMinima = Math.min(pontuacaoMinima, pontuacaoFilho);
                }
                setPontos(pontuacaoMinima);
                return pontuacaoMinima;
            }
        }
        return getPontos();
    }
    public void minimax() {
        minimax(true);
    }
    private int minimaxAlphaBeta(boolean estaMaximizando, float alpha, float beta) {
        if(!getFilhos().isEmpty()) {
            if(estaMaximizando) {
                int pontuacaoMaxima = Integer.MIN_VALUE;
                int pontuacaoFilho;
                for(int i=0; i<getFilhos().size(); i++) {
                    pontuacaoFilho = getFilho(i).minimaxAlphaBeta(false, alpha, beta);
                    pontuacaoMaxima = Math.max(pontuacaoMaxima, pontuacaoFilho);
                    alpha = Math.max(alpha, pontuacaoFilho);
                    if(beta <= alpha) {
                        setFilhos(getFilhos().subList(0, i+1));
                        break;
                    }
                }
                setPontos(pontuacaoMaxima);
                return pontuacaoMaxima;
            }
            else {
                int pontuacaoMinima = Integer.MAX_VALUE;
                int pontuacaoFilho;
                for(int i=0; i<getFilhos().size(); i++) {
                    pontuacaoFilho = getFilho(i).minimaxAlphaBeta(true, alpha, beta);
                    pontuacaoMinima = Math.min(pontuacaoMinima, pontuacaoFilho);
                    beta = Math.min(beta, pontuacaoFilho);
                    if(beta <= alpha) {
                        setFilhos(getFilhos().subList(0, i+1));
                        break;
                    }
                }
                setPontos(pontuacaoMinima);
                return pontuacaoMinima;
            }
        }
        return getPontos();
    }
    
    public void minimaxAlphaBeta() {
        minimaxAlphaBeta(true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}