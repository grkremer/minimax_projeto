public class ArvoreMinimax extends ArvoreBusca{
    
    int profundidade;
    
    @Override
    public Jogada buscarJogada(Estado atual){ 
        
        int max = 100000; // +infinito
        Jogada melhorJogada;
        for(Jogada j : jogo.listaPossiveisJogadas(atual.getTurnoJogador(), atual.getTabuleiro())){
            Estado novoEstado = novoEstado(atual, j);
            Nodo novoNodo = new Nodo(novoEstado, j);
            atual.addFilho();
            int value = min(novoNodo);
            novoNodo.addUtilidade(value);
            if(value > max){
                max = value;
                melhorJogada = j;
            }
        }
        return null; 
        
    };

    
}