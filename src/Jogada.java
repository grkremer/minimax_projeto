import java.util.ArrayList;

public class Jogada {
    private ArrayList<Movimento> movimentos = new ArrayList<Movimento>();

    public ArrayList<Movimento> getMovimentos() {
        return movimentos;
    }
    public void setMovimentos(ArrayList<Movimento> movimentos) {
        this.movimentos = movimentos;
    }

    Jogada(ArrayList<Movimento> movimentos) {
        setMovimentos(movimentos); 
    }
    Jogada(Movimento movimento) {
        getMovimentos().add(movimento); 
    }
    Jogada(int corPeca, int[] posicao) {
        getMovimentos().add(new Movimento(corPeca, posicao, posicao, Movimento.Acao.INSERE)); 
    }
    Jogada(int corPeca, int[][] posicoes) {
        getMovimentos().add(new Movimento(corPeca, posicoes[0], posicoes[1], Movimento.Acao.MOVE)); 
    }
    Jogada(int corPeca, ArrayList<int[][]> movimentos, ArrayList<int[]> pecasEliminadas) {
        for(int[][] posicoes : movimentos) {
            getMovimentos().add(new Movimento(corPeca, posicoes[0], posicoes[1], Movimento.Acao.MOVE)); 
        }
        for(int[] posicao : pecasEliminadas) {
            getMovimentos().add(new Movimento(corPeca, posicao, posicao, Movimento.Acao.REMOVE)); 
        }
    }
    Jogada(String textoJogada) {
        int corPeca = stringParaPeca(textoJogada.substring(0, 1));
        textoJogada = textoJogada.substring(3);
        String[] particoesTextoJogada = textoJogada.split(" ");
        if(textoJogada.contains("→")) {
            for(String textoPosicoes : particoesTextoJogada) {
                int[][] posicoes = stringParaPosicoesMovimento(textoPosicoes);
                getMovimentos().add(new Movimento(corPeca, posicoes[0], posicoes[1], Movimento.Acao.MOVE));
                if(Jogo.comeuPeca(posicoes[0], posicoes[1])) {
                    int [] posicao = Jogo.posPecaEliminada(posicoes[0], posicoes[1]);
                    getMovimentos().add(new Movimento(corPeca, posicao, posicao, Movimento.Acao.REMOVE)); 
                }
            }
        }
        else {
            int[] posicao = stringParaPosicao(particoesTextoJogada[0]);
            getMovimentos().add(new Movimento(corPeca, posicao, posicao, Movimento.Acao.INSERE)); 
        }
    }

    private int stringParaPeca(String siglaPeca) {
        switch(siglaPeca) {
            case "B":
                return Jogo.PECA_BRANCA;
            case "P":
                return Jogo.PECA_PRETA;
            case "_":
            default:
                return Jogo.SEM_PECA; 
        }
    }
    private int[] stringParaPosicao(String textoPosicao) {
        int[] posicao = new int[2];
        posicao[0] = Character.getNumericValue(textoPosicao.charAt(1));
        posicao[1] = Character.getNumericValue(textoPosicao.charAt(3));
        return posicao;
    }
    private int[][] stringParaPosicoesMovimento(String textoMovimento) {
        int[][] movimento = new int[2][2];
        movimento[0][0] = Character.getNumericValue(textoMovimento.charAt(1));
        movimento[0][1] = Character.getNumericValue(textoMovimento.charAt(3));
        movimento[1][0] = Character.getNumericValue(textoMovimento.charAt(7));
        movimento[1][1] = Character.getNumericValue(textoMovimento.charAt(9));
        return movimento;
    }

    @Override
    public String toString() { 
        String log = "";
        for(Movimento movimento : getMovimentos()) {
            if(movimento.getAcao() != Movimento.Acao.REMOVE) {
                log += movimento.toString() + "\n";
            }
        }
        return log.substring(0, log.length()-1);
    }
}
