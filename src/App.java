import jogos.*;
import jogos.util.Jogo;

import org.junit.runner.manipulation.Alphanumeric;

import agentes.*;
import agentes.Trees.MinimaxTree;
import interfaces.*;
import tests.*;
public class App {
    /* 
        OBSERVAÇÃO: o MINIMAX ta com shuffle
        OBSERVAÇÃO2: usar desconto 0.99f aumenta muito o tempo de execução do NegABPrune
        TODO: acompanhar as partidas, ABPRUNE ta estranho talvez seja a função custo no ticktackle não ta conseguindo finalizar a partida
        
        TODO2: acho que o NegTT tem coisa errada

        obs: parece que o NegABPrune ta mais fraco que o ABPrune
        Minimax 3 | NegABPrune 7 | 10 simul -> 10 empates
        NegABPrune 3 | NegABPrune 7 | 10 simul -> 10 empates
        Negamax 3 | NegABPrune 7 | 10 simul -> 10 empates

        ABPrune 3 | NegABPrune 7 | 10 simul -> 10 empates
    
        ABPrune 7 | NegABPrune 3 | 10 simul -> 5 vit ABPrune 5 empates *quando ABPrune com maior profundidade é usado, ele sempre ganha quando começa
        ABPrune 7 | Minimax 3 | 10 simul -> -> 5 vit ABPrune 5 empates

        * antes eu tava retornando o valor do alpha, agora corrigi e acredito que tenha obtido o resultado correto
        ABPrune 3 | NegABPrune 7 | 10 simul -> 5 vit NegABPrune 5 empates

        * agora corrigir o NegTT
        NegTT 7 | NegABPrune 3 | 10 simul -> 10 empates
        NegTT 7 | ABPrune 3 | 10 simul -> 5 empates 5 vitorórias PRO ABPRUNE! 
        *coloquei agora na hora de retornar o valor na tabela de transposição *sign e retornou 10 empates (jogou um pouco melhor)
        *IDEIA: talvez adicionar no hash o valor do sign

    */
    public static void main(String[] args) throws Exception {
        
        //Jogo jogo = new JogoDaVelha4();  // new Rastros(obj player 1, obj player 2);
        //JanelaJogo janela = new JanelaJogo(jogo);
        //jogo.jogar(new ABPruneTT(Jogo.PECA_PRETA, 6), new ABPrune(Jogo.PECA_BRANCA, 6));
        
        //Simulador s = new Simulador(new JogoDaVelha4(), new Random(Jogo.PECA_BRANCA), new Random(Jogo.PECA_PRETA));
        //Simulador s = new Simulador(new TicTackle5(), new Random(Jogo.PECA_BRANCA), new Random(Jogo.PECA_PRETA));
        s.Simular(1000);

        //jogo.carregaLog("logs/log.txt");
        //janela.replayHistoricoJogadas();
    }
}