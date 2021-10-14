public class Estado {
    private int[][] tabuleiro;
    private int vencedor;
    private int turnoJogador;
    private int marcaAgente;
    private Boolean fimJogo;
    int numeroTurnos;

    public Estado(int[][] tabuleiro, int turnoJogador, Boolean fimJogo, int vencedor, int marcaAgente, int turnos){
        this.tabuleiro = tabuleiro;
        this.vencedor = vencedor;
        this.fimJogo = fimJogo;
        this.marcaAgente = marcaAgente;
        this.turnoJogador = turnoJogador;
        this.numeroTurnos = turnos;
    }
    
    public int getTurnos(){ return numeroTurnos; } 
    public int[][] getTabuleiro(){ return tabuleiro; }
    public int getVencedor(){ return vencedor; }
    public int getTurnoJogador(){ return turnoJogador; }
    public Boolean isFimJogo(){ return fimJogo; }
    public int getMarcaAgente(){ return marcaAgente;}
    public int[][] getCopiaTabuleiro(){ 
        int[][] cpy = new int[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5;j++){
                cpy[i][j] = tabuleiro[i][j];
            }
        }
        return cpy; 
    }
}
