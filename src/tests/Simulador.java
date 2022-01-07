package tests;

import agentes.util.Agente;
import jogos.*;
import jogos.util.Jogo;
import jogos.util.Jogada;
import agentes.*;
import interfaces.*;
public class Simulador {
    String IDJogo;
    Jogo jogo;
    Agente agente1;
    Agente agente2;
    Agente agenteLog1;
    Agente agenteLog2;

    public Simulador(String IDJogo, Agente agente1, Agente agente2){
        this.IDJogo = IDJogo;
        this.agente1 = agente1;
        this.agente2 = agente2;
        this.jogo = buscaJogo();
        buscaAgenteLog(agente1, agente2);
    }   

    private void buscaAgenteLog(Agente agente1, Agente agente2){
        if(agente1 instanceof Minimax){
            agenteLog1 = new ArvoreMinimax(agente1.getCorPeca(),  ((Minimax) agente1).getProfundidade());
        }
        if(agente2 instanceof Minimax){
            agenteLog2 = new ArvoreMinimax(agente2.getCorPeca(), ((Minimax) agente2).getProfundidade());
        }
        if(agente1 instanceof ABPrune){
            agenteLog1 = new ABPruneTree(agente1.getCorPeca(), ((ABPrune) agente1).getProfundidade());
        }
        if(agente2 instanceof ABPrune){
            agenteLog2 = new ABPruneTree(agente2.getCorPeca(), ((ABPrune) agente2).getProfundidade());
        }
    }

    private Jogo buscaJogo(){
        if(IDJogo == "ALQUERQUE"){
            return new Alquerque();
        }
        else if(IDJogo == "JOGODAVELHA4"){
            return new JogoDaVelha4();
        }
        else if(IDJogo == "TICKTACKLE5"){
            return new TicTackle5();
        }

        return null;
    }

    public void Simular(int numeroRodadas) throws InterruptedException{
        int rodadasRestantes = numeroRodadas;
        int rodadaInicial = Jogo.PECA_BRANCA;
        
        Jogada j = null;
        int vitoriasA1 = 0;
        int vitoriasA2 = 0;
        int empates = 0;
        //JanelaJogo janela = new JanelaJogo(jogo);
        while(rodadasRestantes > 0){
            jogo.inicializaTabuleiro();;
            int rodada = rodadaInicial;
        
            while(!jogo.verificaFimDeJogo(jogo.getTabuleiro())){
                if(agente1.getCorPeca() == rodada)
                {
                    j = agente1.Mover(jogo, jogo.getTabuleiro());
                    
                    
                    if(!(agente1 instanceof ArvoreMonteCarlo)){
                        //agenteLog1.Mover(jogo, jogo.getTabuleiro());
                        //System.out.println(agenteLog1);
                    }else{
                        System.out.println(agente1);
                    }
                    //System.out.println(agente1);
                }else{
                    
                    
                    j = agente2.Mover(jogo, jogo.getTabuleiro());
                    if(!(agente2 instanceof ArvoreMonteCarlo)){
                        //agenteLog2.Mover(jogo, jogo.getTabuleiro());
                        //System.out.println(agenteLog2);
                    }else{
                        System.out.println(agente2);
                    }
                    //System.out.println(agente2);
                    
                }
                jogo.fazJogada(j, jogo.getTabuleiro(), true);
                rodada = jogo.invertePeca(rodada);
                //Thread.sleep(2000);
            }
            if(jogo.verificaVitoria(agente1.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 1");
                vitoriasA1++;
            }else if(jogo.verificaVitoria(agente2.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 2");
                vitoriasA2++;
            }else{
                System.out.println("EMPATE");
                empates++;
            }

            rodadaInicial = jogo.invertePeca(rodadaInicial);
            rodadasRestantes-=1;
        }
        System.out.println("Vitorias Agente1: " + vitoriasA1 + "\nVitorias Agente2: " + vitoriasA2 + "\nEmpate: " + empates);
    
    }

}
