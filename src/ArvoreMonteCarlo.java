import java.util.ArrayList;
import java.util.Collections;

public class ArvoreMonteCarlo {
    
    int maxSimulacoes;
    double valorExploracao;

    public ArvoreMonteCarlo(int maxSimulacoes, double valorExploracao){
        this.maxSimulacoes = maxSimulacoes;
        this.valorExploracao = valorExploracao;
    }

    /*
    public Jogada Movimentar(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int maximoJogadas) {
        
        Nodo raiz = new Nodo(tabuleiro, jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro));
        Jogada movimento = BuscaArvoreMonteCarlo(jogo, tabuleiro, raiz, corPecaJogador, corPecaAtual);
        return movimento;
    
    }

    private Jogada BuscaArvoreMonteCarlo(Jogo jogo, int[][] tabuleiro, Nodo raiz, int corPecaJogador, int corPecaAtual){ 
        for(int it = 0; Condicional(it); it++){
            Nodo nodo = selecionaNodo(raiz);
            //Nodo novoNodo = expandeNodo(nodo);
            double recompensa = simulaJogo(novoNodo);
            propagaResultado(nodo, recompensa);
        }
        return new Jogada();
    }

  
    //TODO: olhar o método de seleção
    private Nodo selecionaNodo(Nodo raiz){
        Nodo nodo = raiz;
        while(!nodo.filhosIsEmpty()){
            
            ArrayList<Jogada> acoes = nodo.getJogadas();
            

            if( nodo.quantidadeJogadas() > 0){
                Jogada proximaJogada = escolherJogada(acoes);
                //instanciar novo nodo
                return Expande(nodo, proximaJogada);
            }
            
            return melhorFilho(nodo);
        }
        return melhorFilho(nodo);
    }

    private Nodo melhorFilho(Nodo nodo){
        double maxUCB = 0;
        Nodo maxNodo = nodo; //null
        for(Nodo n : nodo.getFilhos()){
            double newUCB  = calculaUCB(n);
            if(maxUCB < newUCB){
                maxNodo = n;
                maxUCB = newUCB;
            }
        }
        return maxNodo;
    }

    //expande um nodo
    private Nodo expandeNodo(Nodo nodoPai, Jogada acao){ 
        //int[][] estado, Nodo pai,  Jogada acao, List<Jogada> possiveisMovimentos
        int[][] novoEstado = Jogo.fazJogada(nodoPai.getCopiaEstado(), acao);
        List<Jogada> possiveisMovimentos = Jogo.PossiveisJogadas();
        nodoPai.removePossivelAcao(jogada);
        
        Nodo novoNodo = new Nodo(nodoPai, novoEstado, acao);
        nodoPai.insereNovoFilho(novoNodo);
        
        return novoNodo;
    }
    
    private double simulaJogo(Nodo inicio){
        int[][] estado = inicio.getCopiaEstado();
        while(!fimDeJogo(estado))
        {
            ArrayList<Jogada> jogadas = jogo.getPossiveisJogadas(estado);
            Collections.shuffle(jogadas);
            Jogada j = jogadas.get(0);
            int[][] estado = jogo.fazJogada(j);

        }

        return valorUtilidade(estado);
    }

    private void propagaResultados(Nodo n, double recompensa)
    {
        Nodo bn = n;
        while(bn != raiz){
            bn.UpdateValorN();
            bn.UpdateValorQ(valorQ, recompensa);
            bn = n.GetPai(); 
        }
    }

    private float calculaUCB(Nodo n) {
        return 0;
    }
    
    private boolean Condicional(int iteracoes){
        return iteracoes < maxSimulacoes;
    }
    */
}

