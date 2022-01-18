package tests;

import agentes.util.Agente;
import jogos.*;
import jogos.util.Jogo;
import jogos.util.Jogada;
import agentes.*;
import agentes.Trees.ABPruneTree;
import agentes.Negamax;

import interfaces.*;
public class Simulador {
    String IDJogo;
    Jogo jogo;
    Agente agente1;
    Agente agente2;
    Agente agenteLog1;
    Agente agenteLog2;

    
    public Simulador(Jogo jogo, Agente agente1, Agente agente2){
        this.jogo = jogo;
        this.agente1 = agente1;
        this.agente2 = agente2;
    }

    public void Simular(int numeroRodadas) throws InterruptedException{
        int rodadasRestantes = numeroRodadas;
        int rodadaInicial = Jogo.PECA_BRANCA;
        
        Jogada j = null;
        int vitoriasA1 = 0;
        int vitoriasA2 = 0;
        int empates = 0;
        JanelaJogo janela = new JanelaJogo(jogo);
        while(rodadasRestantes > 0){
            jogo.inicializaTabuleiro();;
            int rodada = rodadaInicial;
        
            while(!jogo.verificaFimDeJogo(jogo.getTabuleiro())){
                Agente jogadorAtual;
                if(agente1.getCorPeca() == rodada)
                {
                    jogadorAtual = agente1;
                }else{
                    jogadorAtual = agente2;
                }
                
                j = jogadorAtual.Mover(jogo, jogo.getTabuleiro());
                //String[] teste = (jogadorAtual).ComputeStatistics();
                
                //analisador.Mover(jogo, jogo.getTabuleiro());

                jogo.fazJogada(j, jogo.getTabuleiro(), true);
                rodada = jogo.invertePeca(rodada);
                //Thread.sleep(1000);
                
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
            Thread.sleep(1000);

            rodadaInicial = jogo.invertePeca(rodadaInicial);
            rodadasRestantes-=1;
        }
        System.out.println("Vitorias Agente1: " + vitoriasA1 + "\nVitorias Agente2: " + vitoriasA2 + "\nEmpate: " + empates);
    
    }

    private void SalvarLogArvore(Agente ag, Agente analisador){
        String header = "Jogador;Tecnica;Jogo;Turno;Tempo Execução;Nodos Explorados;Profundidade;Branching Factor;Max child;Podas;Transposições;";
        
    
    }

}
