import java.util.HashMap;
public class Minimax {
    int numeroNodos;
    HashMap<Integer, Integer> nodosPorNivel;
    int profundidadeMax;
    int cortes;

    public final int MAX_PONTOS = 100;
    public final int MIN_PONTOS = -100;
    
    public Minimax(int profundadeMax){
        
        this.profundidadeMax = profundadeMax;
        numeroNodos = 0;
        cortes = 0;
        nodosPorNivel = new HashMap<Integer,Integer>();
    }

    Jogada Poda(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual){
        numeroNodos+=1;
        if(!nodosPorNivel.containsKey(profundidadeMax)) nodosPorNivel.put(profundidadeMax,1);
        else nodosPorNivel.put(profundidadeMax, nodosPorNivel.get(profundidadeMax)+1) ;
        
        
        int max = Integer.MIN_VALUE;;
        Jogada melhorJogada = null;
        float alpha = Float.NEGATIVE_INFINITY; 
        float beta  = Float.POSITIVE_INFINITY;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro);
            int valor = Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidadeMax, alpha, beta);
            if(valor > max)
            {
                melhorJogada = j;
                max = valor;
            }
        }
        return melhorJogada;
    }

    public int Max(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta){
        numeroNodos+=1;

        if(!nodosPorNivel.containsKey(profundidadeMax - profundidade)) nodosPorNivel.put(profundidadeMax - profundidade,1);
        else nodosPorNivel.put(profundidadeMax - profundidade, nodosPorNivel.get(profundidadeMax - profundidade)+1) ;
        
        
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        int valor = Integer.MIN_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro);
            
            valor = Math.max(valor, Min(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta));
            if(valor >= beta){ 
                cortes+=1;
                return valor;
            }
            alpha = Math.max(alpha, valor);
        }
        return valor;
    }

    public int Min(Jogo jogo, int[][] tabuleiro, int corPecaJogador, int corPecaAtual, int profundidade, float alpha, float beta){
        numeroNodos+=1;
        if(!nodosPorNivel.containsKey(profundidadeMax - profundidade)) nodosPorNivel.put(profundidadeMax - profundidade,1);
        else nodosPorNivel.put(profundidadeMax - profundidade, nodosPorNivel.get(profundidadeMax - profundidade)+1) ;
        
        if(profundidade == 0 ||jogo.verificaFimDeJogo(tabuleiro)){
            return (int)jogo.geraCusto(corPecaJogador, tabuleiro, MIN_PONTOS, MAX_PONTOS);
        }
        
        int valor = Integer.MAX_VALUE;
        for(Jogada j:jogo.listaPossiveisJogadas(corPecaAtual, tabuleiro)){
            int[][] novoTabuleiro = jogo.criaCopiaTabuleiro(tabuleiro);
            jogo.fazJogada(j, novoTabuleiro);
            
            valor = Math.min(valor, Max(jogo, novoTabuleiro, corPecaJogador, jogo.invertePeca(corPecaAtual), profundidade-1, alpha, beta));
            if(valor <= alpha) {
                cortes += 1;
                return valor;
            }
            beta = Math.min(beta, valor);
        }
        return valor;
    }

    @Override
    public String toString()
    {
        String filhosPorNivel = "";
        
        for(int i=0; i < nodosPorNivel.size(); i++)
        {
            int n = nodosPorNivel.get(i);
            filhosPorNivel += "(nvl " + i + ":" +  n + ") ";
        }
        return "\nMINIMAX\nnumero de nodos: " + numeroNodos + "\n" + filhosPorNivel + "\ncortes: " + cortes + "\n";
    }
}
