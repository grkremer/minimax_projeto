import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ArvoreMonteCarlo {
    
    int maxSimulacoes;
    double coeficienteExploracao;
    Nodo raiz;
    Jogo jogo;

    public ArvoreMonteCarlo(Jogo jogo, int maxSimulacoes, double coeficienteExploracao){
        this.maxSimulacoes = maxSimulacoes;
        this.coeficienteExploracao = coeficienteExploracao;
        this.jogo = jogo;
    }

    public Jogada Movimentar(Estado estadoInicial) {
        
        raiz = new Nodo(estadoInicial, null , null, jogo.listaPossiveisJogadas(estadoInicial.getTurnoJogador(), estadoInicial.getTabuleiro()));
        Jogada movimento = BuscaArvoreMonteCarlo();
        return movimento;
    
    }

    private Jogada BuscaArvoreMonteCarlo(){ 
        
        for(int it = 0; Condicional(it); it++){
            Nodo novoNodo = selecionaNodo(jogo, raiz);
            double recompensa = simulaJogo(novoNodo);
            propagaResultado(novoNodo, recompensa);

        }
        return escolheMelhorJogada();
    }


    private Nodo selecionaNodo(Jogo jogo, Nodo raiz){
        Nodo nodo = raiz;
        //enquanto o nodo não for uma folha
        while(!nodo.filhosIsEmpty()){
            
            Nodo aux = politicaCaminhamento(nodo);

            //se a politica retornar um nodo vazio, cria um novo filho pro nodo 
            if(aux.getAction() == null)
            {
                return expandeNodo(jogo, nodo);
            }else{ //caso contrário, retorna o filho selecionado
                //return aux; //flat
                nodo = aux; //não flat
            }
        
        }
                
        return expandeNodo(jogo, nodo);
    }


    // calcula UCB1 de todos os nodos filhos
    private Nodo politicaCaminhamento(Nodo nodo){
        
        Nodo novaAcao = null;
        double maxUCB = -100;
        Nodo maxNodo = null;

        //se ainda for possivel fazer um novo movimento no nodo,
        // avalia o valor de ucb de uma nova acao
        if(!nodo.possiveisMovimentosIsEmpty())
        {
            //da valor UCB para aplicar uma nova acao
            novaAcao = new Nodo(null, nodo, null, null);
            novaAcao.UpdateValorN();
            maxUCB = calculaUCB(novaAcao);
            maxNodo = novaAcao; //null
        }
        

        for(Nodo n : (nodo.getFilhos()).values()){
            double newUCB  = calculaUCB(n);
            if(maxUCB < newUCB){
                maxNodo = n;
                maxUCB = newUCB;
            }
        }
        return maxNodo;
    }

    //expande um nodo
    private Nodo expandeNodo(Jogo jogo, Nodo nodoPai){ 
        

        ArrayList<Jogada> movimentos = nodoPai.getPossiveisMovimentos();
        if(movimentos.isEmpty())
            return nodoPai;

        Collections.shuffle(movimentos);
        Jogada movimento = movimentos.get(0);
        
        Estado estadoPai = nodoPai.getEstado();
        Estado novoEstado = novoEstado(estadoPai, movimento);
        
        ArrayList<Jogada> possiveisMovimentos = jogo.listaPossiveisJogadas(novoEstado.getTurnoJogador(), novoEstado.getTabuleiro());
        nodoPai.removePossivelAcao(movimento);
        
        Nodo novoNodo = new Nodo(novoEstado, nodoPai, movimento, possiveisMovimentos);
        nodoPai.insereNovoFilho(movimento, novoNodo);
        
        return novoNodo;
    }
    
    //simula partida ate encontrar fim de jogo *nao adiciona nada arvore
    // * talvez não instanciar Estado durante a simulação
    private double simulaJogo(Nodo inicio){
        Estado estadoSimulado = inicio.getEstado();
        
        //só o proximo movimento com heuristica
        ArrayList<Jogada> movimentos = jogo.listaPossiveisJogadas(estadoSimulado.getTurnoJogador(), estadoSimulado.getCopiaTabuleiro());
        
        if(movimentos.isEmpty())
            return valorUtilidade(inicio.getEstado()); //TODO: pensar aqui

        Collections.shuffle(movimentos);
        Jogada movimento = movimentos.get(0);
        estadoSimulado = novoEstado(estadoSimulado, movimento); 

        int it = 0;
        while(!estadoSimulado.isFimJogo() && it < 1000)
        {
            it++;
            movimentos = jogo.listaPossiveisJogadas(estadoSimulado.getTurnoJogador(), estadoSimulado.getCopiaTabuleiro());
            if(movimentos.isEmpty())
                return valorUtilidade(estadoSimulado);
            Collections.shuffle(movimentos);
            movimento = movimentos.get(0);

            estadoSimulado = novoEstado(estadoSimulado, movimento); 
        }

        return valorUtilidade(estadoSimulado);
    }

    // atualiza arvore
    private void propagaResultado(Nodo n, double recompensa)
    {
        Nodo bn = n;
        while(!(bn == null)){
            bn.UpdateValorN();
            bn.UpdateValorQ(recompensa);
            bn = bn.getPai(); 
        }
    }

    private double calculaUCB(Nodo n) {
        double recompensa = n.getValorQ()/n.getValorN();
        double exploracao = coeficienteExploracao * Math.sqrt( (2*Math.log(n.getPai().getValorN()))/n.getValorN() );
        return recompensa + exploracao;
    }
    
    private boolean Condicional(int iteracoes){
        return iteracoes < maxSimulacoes;
    }


    //escolhe movimento a ser executado no tabuleiro
    private Jogada escolheMelhorJogada(){
        double bestReward = Double.NEGATIVE_INFINITY;
        Jogada j = null;
        for(Map.Entry<Jogada,Nodo> n: (raiz.getFilhos()).entrySet()){
            Nodo aux = n.getValue();
            double rw = (aux.getValorQ()/aux.getValorN()) - coeficienteExploracao * Math.sqrt( (2*Math.log(aux.getPai().getValorN()))/aux.getValorN() );
            
            if(rw > bestReward){
                bestReward = rw;
                j = n.getKey();
            }
        }
        return j;
    }

    private float valorUtilidade(Estado s){  // 1 * 0  ** 1 * 1 
        if(s.getMarcaAgente() == s.getVencedor())
            return 1;
        else if(jogo.invertePeca(s.getMarcaAgente()) == s.getVencedor())
            return -1;
        else
            return 0;
    }

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

 // *pode ser usada para melhorar desempenho
    private Jogada avaliaMovimentos(ArrayList<Jogada> movimentos, Estado atual){
        
        float max = -100;
        Jogada mov = null;
        for(Jogada m: movimentos){
            int[][] novoTabuleiro = atual.getCopiaTabuleiro();
            jogo.fazJogada(m, novoTabuleiro);
            float aux = (float)jogo.geraCusto(atual.getTurnoJogador(), novoTabuleiro, -100, 100);
            if(aux > max){
                max = aux;
                mov = m;
            }
        }
        return mov;
    }

    public Nodo getRaiz(){return raiz;}
    
}

