import java.util.ArrayList;
import java.util.Arrays;

public class Tsoro extends JogoDaVelha4{
    private final int MAX_PECAS = 4;
    
    Tsoro() {
        super();
    }
    @Override
    public boolean verificaMovimento(int[][] movimento, int[][] tabuleiro) {
        int xInicial = movimento[0][0];
        int yInicial = movimento[0][1];
        int xFinal = movimento[1][0];
        int yFinal = movimento[1][1];
        if(estaNosLimites(xInicial, yInicial) && estaNosLimites(xFinal, yFinal)) {
            if (tabuleiro[xFinal][yFinal] == SEM_PECA && tabuleiro[xInicial][yInicial] != SEM_PECA) {
                //Se quer se mover na diagonal
                if((Math.abs(xInicial - xFinal) == 1 ) && (Math.abs(yInicial - yFinal) == 1)) {
                    //Se x e y têm a mesma paridade
                    if(((xInicial % 2 == 0) && (yInicial % 2 == 0)) || ((xInicial % 2 == 1) && (yInicial % 2 == 1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //Se quer se mover na vertical/horizontal
                else if((Math.abs(xInicial - xFinal) <= 1 ) && (Math.abs(yInicial - yFinal) <= 1)) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public ArrayList<Jogada> listaPossiveisJogadasInsercao(int corPeca, int[][] tabuleiro) {
        ArrayList<Jogada> possiveisJogadas = new ArrayList<Jogada>();
        boolean horizontal = verificaSimetriaHorizontal(tabuleiro);
        boolean vertical = verificaSimetriaVertical(tabuleiro);
        boolean diagonal = verificaSimetriaDiagonal(tabuleiro);
        boolean outraDiagonal = verificaSimetriaOutraDiagonal(tabuleiro);
        int alturaMax = ALTURA_TABULEIRO;
        int larguraMax = LARGURA_TABULEIRO;

        if(horizontal) {
            alturaMax = ALTURA_TABULEIRO-2;
        }
        if(vertical) {
            larguraMax = LARGURA_TABULEIRO-2;
        }
        for(int y=0; y < alturaMax; y++) {
            for(int x=0; condicaoLoopSimetria(larguraMax,diagonal,outraDiagonal,x,y); x++) {
                int[] posJogada = {x, y};
                Jogada possibilidade = new Jogada(corPeca, posJogada);
                if(verificaJogada(possibilidade, tabuleiro)) {
                    possiveisJogadas.add(possibilidade);
                }
            }
        }
        return possiveisJogadas;
    }
    public ArrayList<Jogada> listaPossiveisJogadasMovimento(int corPeca, int[][] tabuleiro) {
        ArrayList<Jogada> possiveisJogadas = new ArrayList<Jogada>();
        int[][] regioes = {{1,0},{0,1},{1,1},{-1,-1},{-1,1},{1,-1},{-1,0},{0,-1}};
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    for(int i=0; i < regioes.length; i++) {
                        int novoX = x+regioes[i][0];
                        int novoY = y+regioes[i][1];
                        int[][] movimento = {{x, y}, {novoX, novoY}};
                        if(verificaMovimento(movimento, tabuleiro)) {
                            Jogada possibilidade = new Jogada(corPeca, movimento);
                            possiveisJogadas.add(possibilidade);
                        }
                    }
                }
            }
        }
        return possiveisJogadas;
    }
    @Override
    public ArrayList<Jogada> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
        if(contaPecas(corPeca, tabuleiro) < MAX_PECAS) 
            return listaPossiveisJogadasInsercao(corPeca, tabuleiro);
        else 
            return listaPossiveisJogadasMovimento(corPeca, tabuleiro);
    }
    
    public float geraMaiorDistanciaMenor(int corPeca, int tabuleiro[][]) {
        int pecas[][] = new int[LARGURA_TABULEIRO][2];
        int i = 0;
        int j = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    pecas[i][0] = x;
                    pecas[i][1] = y;
                    i++;
                }
            }
        }
        float[] distancias = new float[LARGURA_TABULEIRO];
        float distancia = 0;
        
        for (i =0; i < LARGURA_TABULEIRO; i++){
            distancias[i] = Float.POSITIVE_INFINITY;
            for (j = 0; j < LARGURA_TABULEIRO; j++){
                if (i!= j) {
                    distancia =  (float)Math.sqrt(Math.pow((pecas [i][0] - pecas [j][0]),2) + Math.pow((pecas [i][1] - pecas [j][1]),2));
                    distancias[i] = Math.min(distancias[i], distancia);                  
                }
            }
        }
        Arrays.sort(distancias);
        return distancias[LARGURA_TABULEIRO-1];
    }
    public float geraMaiorDistanciaSegundaMenor(int corPeca, int tabuleiro[][]) {
        int pecas[][] = new int[LARGURA_TABULEIRO][2];
        int i = 0;
        int j = 0;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    pecas[i][0] = x;
                    pecas[i][1] = y;
                    i++;
                }
            }
        }
        float[] distancias = new float[LARGURA_TABULEIRO];
        float distancia = 0;
        
        for (i =0; i < LARGURA_TABULEIRO; i++) {
            distancias[i] = Float.POSITIVE_INFINITY;
            float menor = Float.POSITIVE_INFINITY;
            
            for (j = 0; j < LARGURA_TABULEIRO; j++) {
                if (i!= j) {
                    distancia =  (float)Math.sqrt(Math.pow((pecas [i][0] - pecas [j][0]),2) + Math.pow((pecas [i][1] - pecas [j][1]),2));
                    if (distancia <= menor) {
                        distancias[i] = menor;
                        menor = distancia;
                    }
                    else if(distancias[i] == Float.POSITIVE_INFINITY) {
                        distancias[i] = distancia;
                    }               
                }
            }
        }
        Arrays.sort(distancias);
        return distancias[LARGURA_TABULEIRO-1];
        
    }
    @Override
    public float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        float maxAlinhado =  (float)maximoAlinhado(corPeca, tabuleiro);
        float maxDistancia = geraMaiorDistanciaMenor(corPeca, tabuleiro);
        float custo;
        if(maxAlinhado >= 4) {
            custo = maxPontos;
        }
        else {
            float custoLinha = normalizaPontuacao(1, 4, (float)minPontos, (float)maxPontos, maxAlinhado);
            float custoDistancia = normalizaPontuacao(1, (float)Math.sqrt(20), (float)minPontos, (float)maxPontos, maxDistancia);
            float custoDistancia2 = normalizaPontuacao(1, (float)Math.sqrt(20), (float)minPontos, (float)maxPontos, geraMaiorDistanciaSegundaMenor(corPeca, tabuleiro));
            custo = custoLinha *0.5f + custoDistancia * -0.4f + custoDistancia2 * -0.1f;
        }
        return custo;
    }

    public void interpretaJogadaPlayerInsercao(int[] posClick) {
        Jogada jogada = new Jogada(getPecaPlayer(), posClick);
        if(verificaJogada(jogada, getTabuleiro())) {
            setJogadaDoPlayer(jogada);
            setVezDoPlayer(false);
        }
    }
    public void interpretaJogadaPlayerMovimento(int[] posClick) {
        if(!isSelecionado()) {
            setPosSelecionado(posClick);
            if(getTabuleiro()[posClick[0]][posClick[1]] == getPecaPlayer()) {
                setSelecionado(true);
            }
        }
        else {
            int[][] movimento = {getPosSelecionado(), posClick};
            if(verificaMovimento(movimento, getTabuleiro())) {
                Jogada jogada = new Jogada(getPecaPlayer(), movimento);
                setJogadaDoPlayer(jogada);
                setVezDoPlayer(false);
                setSelecionado(false);
            }
            else if(getTabuleiro()[posClick[0]][posClick[1]] == getPecaPlayer()) {
                setPosSelecionado(posClick);
            }
            else {
                setSelecionado(false);
            }
        }
    }
    @Override
    public void interpretaJogadaPlayer(int[] posClick) {
        if(contaPecas(getPecaPlayer(), getTabuleiro()) < MAX_PECAS) 
            interpretaJogadaPlayerInsercao(posClick);
        else 
            interpretaJogadaPlayerMovimento(posClick);
    }
    @Override
    public Jogada jogadaDanoMinimo(Jogada antigaMelhorJogada, int corPeca) {
        return antigaMelhorJogada;
    }
}
