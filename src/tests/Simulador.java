package tests;

import agentes.util.Agente;
import jogos.util.Jogo;
import jogos.util.Jogada;
import interfaces.*;


import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        //JanelaJogo janela = new JanelaJogo(jogo);
        
        
        List<List<String>> logLst = new ArrayList<List<String>>();
            
        while(rodadasRestantes > 0){
            jogo.inicializaTabuleiro();;
            int rodada = rodadaInicial;
            int turnos = 0;

            while(!jogo.verificaFimDeJogo(jogo.getTabuleiro())){
                turnos++;
                Agente jogadorAtual;
                if(agente1.getCorPeca() == rodada)
                {
                    jogadorAtual = agente1;
                }else{
                    jogadorAtual = agente2;
                }
                
                j = jogadorAtual.Mover(jogo, jogo.getTabuleiro());
                
                
                String[] stArgs = (jogadorAtual).ComputeStatistics();
                List<String> plyLst = new ArrayList<String>();
                plyLst.add(String.valueOf(turnos));
                Collections.addAll(plyLst, stArgs);
                plyLst.add(" ");
                logLst.add(plyLst);
                
                jogo.fazJogada(j, jogo.getTabuleiro(), true);
                rodada = jogo.invertePeca(rodada);
                if(turnos >= 30) break;
                //Thread.sleep(500);
            }
            
            if(jogo.verificaVitoria(agente1.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 1");
                (logLst.get(logLst.size()-1)).set(8, "VITORIA");
                vitoriasA1++;
            }else if(jogo.verificaVitoria(agente2.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 2");
                (logLst.get(logLst.size()-1)).set(8, "VITORIA");
                vitoriasA2++;
            }else{
                System.out.println("EMPATE");
                (logLst.get(logLst.size()-1)).set(8, "EMPATE");
                empates++;
            }
            rodadaInicial = jogo.invertePeca(rodadaInicial);
            rodadasRestantes-=1;
            
        }
        createLog(jogo.getNome(), rodadasRestantes, logLst);
        System.out.println("Vitorias Agente1: " + vitoriasA1 + "\nVitorias Agente2: " + vitoriasA2 + "\nEmpate: " + empates);
    
    }

    private void createLog(String gameName, int gameCounter, List<List<String>> logs){
        String id = "LOG_" + gameName + "_"; //+ String.valueOf(gameCounter);
        File arquivo = new File( "logs\\" + id + "LOG.csv" );

        try{
        FileWriter fw = new FileWriter( arquivo );
        BufferedWriter bw = new BufferedWriter( fw );
        bw.write("TURNO;NOME_AGENTE;COR_PECA;PLY_TIME;MAX DEPTH; MAX BRANCH; BFACTOR; TOTAL_NODES;RESULTADO;\n");

        for(int i = 0; i < logs.size(); i++){
            String tmp = "";
            for(int j = 0; j < logs.get(i).size(); j++){
                tmp += logs.get(i).get(j) + ";";
            }
            bw.write(tmp + "\n");
        }
        bw.close();
        fw.close();
        }
        catch(IOException e){
            System.out.println("deu merda");
        }
    }

}
