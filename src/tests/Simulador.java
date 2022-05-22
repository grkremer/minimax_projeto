package tests;

import agentes.util.IAgent;
import jogos.util.Jogo;
import jogos.util.Jogada;
import interfaces.*;


import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Simulador {
    String IDJogo;
    Jogo jogo;
    IAgent agente1;
    IAgent agente2;
    IAgent agenteLog1;
    IAgent agenteLog2;

    
    public Simulador(Jogo jogo, IAgent agente1, IAgent agente2){
        this.jogo = jogo;
        this.agente1 = agente1;
        this.agente2 = agente2;
    }

    public void Simular(String fileName, int numeroRodadas) throws InterruptedException{
        int rodadasRestantes = numeroRodadas;
        int rodadaInicial = Jogo.PECA_BRANCA;
        
        Jogada j = null;
        int vitoriasA1 = 0;
        int vitoriasA2 = 0;
        int empates = 0;
        JanelaJogo janela = new JanelaJogo(jogo);
        
        
        List<List<String>> logLst = new ArrayList<List<String>>();
        String[] executionTime = new String[]{"10"};
          
        /* 
        try{
            createFile(fileName, jogo.getNome());
        }catch(FileAlreadyExistsException var){
            System.out.println(var);
            System.out.println("Check if filename already exist. ");
            
            return;
        }catch(IOException var){
            System.out.println(var);
            System.out.println("Check if the isnt a folder with the exact game name, if doesent exist, create one. ");
            return;
        }
        */

        while(rodadasRestantes > 0){
            jogo.inicializaTabuleiro();;
            int rodada = rodadaInicial;
            int turnos = 0;

            while(!jogo.verificaFimDeJogo(jogo.getTabuleiro())){
                turnos++;
                IAgent jogadorAtual;
                if(agente1.getCorPeca() == rodada)
                {
                    jogadorAtual = agente1;
                }else{
                    jogadorAtual = agente2;
                }
                
                j = jogadorAtual.Move(jogo, jogo.getTabuleiro(), executionTime);
                
                
                String[] stArgs = (jogadorAtual).ComputeStatistics();
                executionTime[0] = stArgs[2];
                
                List<String> plyLst = new ArrayList<String>();
                plyLst.add(String.valueOf(numeroRodadas - rodadasRestantes));
                plyLst.add(String.valueOf(turnos));
                Collections.addAll(plyLst, stArgs);
                plyLst.add(" ");
                logLst.add(plyLst);
                
                jogo.fazJogada(j, jogo.getTabuleiro(), true);
                rodada = jogo.invertePeca(rodada);
                if(turnos >= 50) break;
                //Thread.sleep(1000);


            }
            
            if(jogo.verificaVitoria(agente1.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 1");
                (logLst.get(logLst.size()-1)).set(10, "VITORIA");
                vitoriasA1++;
            }else if(jogo.verificaVitoria(agente2.getCorPeca(), jogo.getTabuleiro())){
                System.out.println("VITORIA jogador 2");
                (logLst.get(logLst.size()-1)).set(10, "VITORIA");
                vitoriasA2++;
            }else{
                System.out.println("EMPATE");
                (logLst.get(logLst.size()-1)).set(10, "EMPATE");
                empates++;
            }
            rodadaInicial = jogo.invertePeca(rodadaInicial);
            rodadasRestantes-=1;
            
            if(rodadasRestantes%5==0){
                writeLog(fileName, jogo.getNome(), logLst);
                logLst.clear();
            }
            
        }
        //writeLog(fileName, jogo.getNome(), logLst);
        //closeLog(fileName, jogo.getNome(), vitoriasA1, vitoriasA2, empates, agente1.getID(), agente2.getID());
        System.out.println("Vitorias Agente1: " + vitoriasA1 + "\nVitorias Agente2: " + vitoriasA2 + "\nEmpate: " + empates);
    
    }

    private void createFile(String fileName, String gameName)throws FileAlreadyExistsException, IOException{
        File arquivo = new File( "logs\\" + gameName + "\\" + fileName + ".csv" );  
        if(arquivo.exists()){
            throw new FileAlreadyExistsException("Filename already used, please change filename!");
        }else{
            FileWriter fw = new FileWriter( arquivo);
            BufferedWriter bw = new BufferedWriter( fw );
            bw.write("PARTIDA;TURNO;NOME_AGENTE;COR_PECA;PLY_TIME;MAX DEPTH; BFACTOR; TOTAL_NODES;MAX BRANCH;MAX BOARD;RESULTADO;\n");   
            bw.close();
            fw.close(); 
        }
    }

    private void closeLog(String fileName, String gameName, int vitoriasA1, int vitoriasA2, int empates, String agName1, String agName2) {
        File arquivo = new File( "logs\\" + gameName + "\\REPORT_" + fileName + ".csv" );
        try{
        
        
        FileWriter fw = new FileWriter( arquivo, true );
        BufferedWriter bw = new BufferedWriter( fw );
         
        bw.write(gameName + ";\n");
        bw.write("VITORIAS JOGADOR 1;" + String.valueOf(vitoriasA1) + ";"+agName1+";\n");
        bw.write("VITORIAS JOGADOR 2;" + String.valueOf(vitoriasA2) + ";"+agName2+";\n");
        bw.write("EMPATES;" + String.valueOf(empates) + ";\n");
        bw.write("TOTAL;" + String.valueOf(vitoriasA1+vitoriasA2+empates) + ";\n");

        bw.close();
        fw.close();
        }
        catch(IOException e){
            System.out.println("deu merda");
        }
    }

    private void writeLog(String fileName, String gameName, List<List<String>> logs){
        File arquivo = new File("logs\\" + gameName + "\\" + fileName + ".csv");
        try{
        
        
        FileWriter fw = new FileWriter( arquivo, true );
        BufferedWriter bw = new BufferedWriter( fw );
        
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
            System.out.println("deu ruim");
        }
    }
    /* 
    private void createLog(String gameName, int gameCounter, List<List<String>> logs){
        String id = "LOG_" + gameName + "_"; //+ String.valueOf(gameCounter);
        File arquivo = new File( "logs\\" + id + ".csv" );
        try{
        
        
        FileWriter fw = new FileWriter( arquivo, true );
        BufferedWriter bw = new BufferedWriter( fw );
        
        bw.write("TURNO;NOME_AGENTE;COR_PECA;PLY_TIME;MAX DEPTH; BFACTOR; TOTAL_NODES;MAX BRANCH;MAX BOARD;RESULTADO;\n");

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
    */

}
