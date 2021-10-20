

import java.util.Collections;
public class ArvoreMinimax implements Agente{
    int profundidade;
    int COR_PECA;
    Boolean podaLigada;
    LogArvoreJogo log;
    public ArvoreMinimax(int COR_PECA, int profundidade)
    {
        this.profundidade = profundidade;
        this.podaLigada = false;
        this.COR_PECA = COR_PECA;
    }

    public ArvoreMinimax(int COR_PECA, int profundidade, Boolean podaLigada)
    {
        this.profundidade = profundidade;
        this.podaLigada = podaLigada;
        this.COR_PECA = COR_PECA;
    }
    
    public Jogada Mover(Jogo jogo, int[][] tabuleiro){
        if(podaLigada){ return MoverComPoda(jogo, tabuleiro, COR_PECA, COR_PECA); }
        else return MoverSemPoda(jogo, tabuleiro, COR_PECA, COR_PECA);
    }

    public Jogada MoverComPoda(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual){
        log = new LogArvoreJogo();
        //ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, getMaximoJogadas());
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(jogo, tabuleiro, corPecaJogador, corPecaJogador, profundidade, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Collections.shuffle(jogadas.getFilhos());
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
            melhorJogada = jogo.jogadaDanoMinimo(melhorJogada, corPecaJogador);
        }
        float chance = jogo.normalizaPontuacao(jogadas.MIN_PONTOS, jogadas.MAX_PONTOS, 0, 100, (float)pontuacaoMaxima);
        System.out.println("Chance de vitória: "+chance+"%");
        return melhorJogada;
    }

    public Jogada MoverSemPoda(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual)
    {
        log = new LogArvoreJogo();
        //ArvoreDeJogadas jogadas = new ArvoreDeJogadas(this, getTabuleiro(), corPeca, corPeca, profundidade, getMaximoJogadas());
        ArvoreDeJogadas jogadas = new ArvoreDeJogadas(jogo, tabuleiro, corPecaJogador, corPecaJogador, profundidade, 100000000);
        Collections.shuffle(jogadas.getFilhos());
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
            melhorJogada = jogo.jogadaDanoMinimo(melhorJogada, corPecaJogador);
        }
        float chance = jogo.normalizaPontuacao(jogadas.MIN_PONTOS, jogadas.MAX_PONTOS, 0, 100, (float)pontuacaoMaxima);
        System.out.println("Chance de vitória: "+chance+"%");
        return melhorJogada; 
    }

    public int getCorPeca(){
        return COR_PECA;
    }

    @Override
    public String toString(){
        return log.toString();
    }

    
}
