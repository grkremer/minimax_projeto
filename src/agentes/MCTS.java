package agentes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import agentes.util.*;
import jogos.util.Jogada;
import jogos.util.Jogo;
import logging.LogArvoreMonteCarlo;

/* 
    Monte-Carlo Tree Search (MCTS) using UCB1 policy
*/
public class MCTS implements Agente{
    
    int maxSimulacoes;
    double coeficienteExploracao;
    NodoMonteCarlo raiz;
    int COR_PECA;
    LogArvoreMonteCarlo log;
    
    private final String ID = "MCTS";
    private Jogo lastGamePlayed;
    protected float tempoExecucao;
    protected long startTime;
    protected long endTime;

    public MCTS(int COR_PECA, int maxSimulacoes, double coeficienteExploracao){
        this.maxSimulacoes = maxSimulacoes;
        this.coeficienteExploracao = coeficienteExploracao;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro) throws InterruptedException{
        
        
        Estado estadoInicial = new Estado(tabuleiro, COR_PECA, false, 0, COR_PECA, 0);
        initializeVariables();
        raiz = new NodoMonteCarlo(estadoInicial, null , null, jogo.listaPossiveisJogadas(estadoInicial.getTurnoJogador(), estadoInicial.getTabuleiro()));
        Jogada j = BuscaArvoreMonteCarlo(jogo);
        closeVariables();
        lastGamePlayed = jogo;

        System.out.println("nodes: " + String.valueOf(maxSimulacoes) + "\ttime: " + String.valueOf(tempoExecucao) + "s");
        return j;
        
    }

   private Jogada BuscaArvoreMonteCarlo(Jogo jogo) throws InterruptedException{ 
        
        for(int it = 0; Condicional(it); it++){
            NodoMonteCarlo novoNodo = selecionaNodo(jogo, raiz);
            double recompensa = simulaJogo(jogo, novoNodo);
            propagaResultado(novoNodo, recompensa);

        }
        return escolheMelhorJogada();
    }


    private NodoMonteCarlo selecionaNodo(Jogo jogo, NodoMonteCarlo raiz) throws InterruptedException{
        NodoMonteCarlo nodo = raiz;
        //enquanto o nodo não for uma folha
        while(!nodo.filhosIsEmpty()){
            
            NodoMonteCarlo aux = politicaCaminhamento(nodo);

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
    private NodoMonteCarlo politicaCaminhamento(NodoMonteCarlo nodo){
        
        NodoMonteCarlo novaAcao = null;
        double maxUCB = -100;
        NodoMonteCarlo maxNodo = null;

        //se ainda for possivel fazer um novo movimento no nodo,
        // avalia o valor de ucb de uma nova acao
        if(!nodo.possiveisMovimentosIsEmpty())
        {
            //da valor UCB para aplicar uma nova acao
            novaAcao = new NodoMonteCarlo(null, nodo, null, null);
            novaAcao.UpdateValorN();
            maxUCB = calculaUCB(novaAcao);
            maxNodo = novaAcao; //null
        }
        

        for(NodoMonteCarlo n : (nodo.getFilhos()).values()){
            double newUCB  = calculaUCB(n);
            if(maxUCB < newUCB){
                maxNodo = n;
                maxUCB = newUCB;
            }
        }
        return maxNodo;
    }

    //expande um nodo
    private NodoMonteCarlo expandeNodo(Jogo jogo, NodoMonteCarlo nodoPai) throws InterruptedException{ 
        

        ArrayList<Jogada> movimentos = nodoPai.getPossiveisMovimentos();
        if(movimentos.isEmpty())
            return nodoPai;

        Collections.shuffle(movimentos);
        Jogada movimento = movimentos.get(0);
        
        Estado estadoPai = nodoPai.getEstado();
        
        Estado novoEstado = novoEstado(jogo, estadoPai, movimento);
        ArrayList<Jogada> possiveisMovimentos = jogo.listaPossiveisJogadas(novoEstado.getTurnoJogador(), novoEstado.getTabuleiro());
        nodoPai.removePossivelAcao(movimento);
        NodoMonteCarlo novoNodo = new NodoMonteCarlo(novoEstado, nodoPai, movimento, possiveisMovimentos);
        nodoPai.insereNovoFilho(movimento, novoNodo);
        
        return novoNodo;
    }
    
    //simula partida ate encontrar fim de jogo *nao adiciona nada arvore
    // * talvez não instanciar Estado durante a simulação
    private double simulaJogo(Jogo jogo, NodoMonteCarlo inicio) throws InterruptedException{
        Estado estadoSimulado = inicio.getEstado();
        
        //só o proximo movimento com heuristica
        ArrayList<Jogada> movimentos = jogo.listaPossiveisJogadas(estadoSimulado.getTurnoJogador(), estadoSimulado.getCopiaTabuleiro());
        
        if(movimentos.isEmpty())
            return valorUtilidade(jogo, inicio.getEstado()); //TODO: pensar aqui

        Collections.shuffle(movimentos);
        Jogada movimento = movimentos.get(0);
        estadoSimulado = novoEstado(jogo, estadoSimulado, movimento); 

        int it = 0;
        while(!estadoSimulado.isFimJogo() && it < 100)
        {
            it++;
            movimentos = jogo.listaPossiveisJogadas(estadoSimulado.getTurnoJogador(), estadoSimulado.getCopiaTabuleiro());
            if(movimentos.isEmpty())
                return valorUtilidade(jogo, estadoSimulado);
            Collections.shuffle(movimentos);
            movimento = movimentos.get(0);

            estadoSimulado = novoEstado(jogo, estadoSimulado, movimento); 
        }

        return valorUtilidade(jogo, estadoSimulado);
    }

    // atualiza arvore
    private void propagaResultado(NodoMonteCarlo n, double recompensa)
    {
        NodoMonteCarlo bn = n;
        double recompensaDescontada = recompensa;
        while(!(bn == null)){
            bn.UpdateValorN();
            bn.UpdateValorQ(recompensaDescontada);
            //bn.Learn(recompensaDescontada);
            bn = bn.getPai(); 
            recompensaDescontada *= 0.9;
        }
    }

    private double calculaUCB(NodoMonteCarlo n) {
        double recompensa = n.getValorQ()/n.getValorN();
        //double recompensa = n.getValorQ();
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
        for(Map.Entry<Jogada,NodoMonteCarlo> n: (raiz.getFilhos()).entrySet()){
            NodoMonteCarlo aux = n.getValue();
            double rw = (aux.getValorQ()/aux.getValorN()) - coeficienteExploracao * Math.sqrt( (2*Math.log(aux.getPai().getValorN()))/aux.getValorN() );
            
            if(rw > bestReward){
                bestReward = rw;
                j = n.getKey();
            }
        }
        return j;
    }

    private float valorUtilidade(Jogo jogo, Estado s){  // 1 * 0  ** 1 * 1 
        float valorUt = 0;
        
        // 1 * (p^turnos)
        // ganhador1: 2 turnos 0,984, 0,99| 4 turnos  0,96, 0,980 =  diferença 0,02
        // ganhador2: 50 turnos 0,6, 0,94 | 100 turnos  0,33, 0,93 = diferença 0,3
        
        //float fatorDesconto = (float)Math.pow(0.9, Math.log(s.getTurnos()/2));
        float fatorDesconto = (float)Math.pow(0.99, s.getTurnos()/2);
        
        if(s.getMarcaAgente() == s.getVencedor())
            valorUt = 1;
        else if(jogo.invertePeca(s.getMarcaAgente()) == s.getVencedor())
            valorUt = -1;
        else
            valorUt = 0;
        
        return valorUt * fatorDesconto;
    }

    private Estado novoEstado(Jogo jogo, Estado estadoAtual, Jogada novoMovimento) throws InterruptedException{
        int[][] novoTabuleiro = estadoAtual.getCopiaTabuleiro();
        jogo.fazJogada(novoMovimento, novoTabuleiro, false);
        int turnoJogador = jogo.invertePeca(estadoAtual.getTurnoJogador());
        Boolean fimJogo = jogo.verificaFimDeJogo(novoTabuleiro);
        int jogadorVencedor = 0;
        if(fimJogo){
            if(jogo.verificaVitoria(turnoJogador, novoTabuleiro))
                jogadorVencedor = turnoJogador; //ver isso
            else
                jogadorVencedor = estadoAtual.getTurnoJogador();
        }
        return new Estado(novoTabuleiro, turnoJogador, fimJogo, jogadorVencedor, estadoAtual.getMarcaAgente(), estadoAtual.getTurnos()+1);

    }

    protected void initializeVariables(){
        tempoExecucao = 0;
        startTime = System.currentTimeMillis();  
    }

    protected void closeVariables(){
        endTime = System.currentTimeMillis();
        tempoExecucao = (endTime - startTime)/1000f;
    }

    @Override
    public String[] ComputeStatistics(){
        
        log = new LogArvoreMonteCarlo();
        
        log.AvaliaArvore(raiz);
        String[] thisArgs = getArgs();
        return thisArgs;
    }

    @Override
    public String[] getArgs(){
        return new String[]{String.valueOf(COR_PECA), this.ID, lastGamePlayed.getNome(), String.valueOf(tempoExecucao), String.valueOf(log.numeroNodos), String.valueOf(log.mediaBranching), String.valueOf(log.maxBranching), "0", "0"};
    }

    public NodoMonteCarlo getRaiz(){
        return raiz;
    }

    public int getCorPeca(){
        return COR_PECA;
    }
    @Override
    public String toString(){
        return log.toString();
    }

}

