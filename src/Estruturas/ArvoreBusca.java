package Estruturas;

public class ArvoreBusca{
    Nodo raiz;
    Jogo jogo;

    public ArvoreBusca(Jogo jogo, Estado inicial){
        this.raiz = new Nodo(inicial);
        this.jogo = jogo;
    }


    public  Jogada buscarJogada(Nodo raiz){ return null; };
    private HashMap<Jogada, Nodo> getFilhos(Nodo atual){ return atual.getFilhos(); };
    private ArrayList<Jogada> getPossveisJogadas(Nodo atual) { return atual.getPossiveisJogadas(); }

    private Estado novoEstado(Estado estadoAtual, Jogada novoMovimento){
        int[][] novoTabuleiro = estadoAtual.getCopiaTabuleiro();
        jogo.fazJogada(novoMovimento, novoTabuleiro);
        int turnoJogador = jogo.invertePeca(estadoAtual.getTurnoJogador());
        Boolean fimJogo = jogo.verificaFimDeJogo(novoTabuleiro);
        int jogadorVencedor = 0;
        if(fimJogo){
            if(jogo.verificaVitoria(turnoJogador, novoTabuleiro))
                jogadorVencedor = turnoJogador; //ver isso
            else
                jogadorVencedor = estadoAtual.getTurnoJogador();
        }
        return new Estado(novoTabuleiro, turnoJogador, fimJogo, jogadorVencedor, estadoAtual.getMarcaAgente());
    }

}