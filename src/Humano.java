public class Humano implements Agente{
    JanelaJogo janelaJogo;
    int COR_PECA;
    public Humano(int COR_PECA, JanelaJogo janelaJogo){
        this.janelaJogo = janelaJogo;
        this.COR_PECA = COR_PECA;
    }

    public Jogada Mover(Jogo jogo, int[][] tabuleiro)
    {
        try{
            jogo.setPecaPlayer(COR_PECA);
            return janelaJogo.playerFazMovimento(jogo, tabuleiro, COR_PECA, COR_PECA);
        }
        catch(InterruptedException e){

        }
        return null;
    }

    public int getCorPeca(){
        return COR_PECA;
    }
}
