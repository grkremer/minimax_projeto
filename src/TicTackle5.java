import java.util.ArrayList;
import java.util.Arrays;

public class TicTackle5 extends Jogo {
    TicTackle5() {
        super();
        setNome("Tic Tackle 5");
        setProfundidade(5);
    }

    @Override
    public void inicializaTabuleiro() {
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][0] = PECA_BRANCA;
            }
            else {
                getTabuleiro()[x][0] = PECA_PRETA;
            }   
        }
        for(int y=1; y < ALTURA_TABULEIRO-1; y++) {
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                getTabuleiro()[x][y] = SEM_PECA;
            }
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(x % 2 == 0) {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_PRETA;
            }
            else {
                getTabuleiro()[x][ALTURA_TABULEIRO-1] = PECA_BRANCA;
            } 
        }
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

    @Override
    public ArrayList<Jogada> listaPossiveisJogadas(int corPeca, int[][] tabuleiro) {
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
    public int maximoAlinhado(int corPeca, int[][] tabuleiro) {
        int maximo = 0;
        int contagem;
        for(int y=0; y < ALTURA_TABULEIRO; y++) {
            contagem = 0;
            for(int x=0; x < LARGURA_TABULEIRO; x++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            contagem = 0;
            for(int y=0; y < ALTURA_TABULEIRO; y++) {
                if(tabuleiro[x][y] == corPeca) {
                    contagem++;
                    maximo = Math.max(maximo, contagem);
                }
                else {
                    contagem = 0;
                }
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=1; x < LARGURA_TABULEIRO; x++) {
            if(tabuleiro[x][x-1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=0; x < LARGURA_TABULEIRO-1; x++) {
            if(tabuleiro[x][x+1] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        } 
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 0; x--) {
            if(tabuleiro[x][(LARGURA_TABULEIRO-1)-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-1; x >= 1; x--) {
            if(tabuleiro[x-1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        if (maximo >= 4) {
            return maximo;
        }
        contagem = 0;
        for(int x=LARGURA_TABULEIRO-2; x >= 0; x--) {
            if(tabuleiro[x+1][LARGURA_TABULEIRO-1-x] == corPeca) {
                contagem++;
                maximo = Math.max(maximo, contagem);
            }
            else {
                contagem = 0;
            }
        }
        return maximo;
    }

    @Override
    public boolean verificaVitoria(int corPeca, int[][] tabuleiro) {
        if(maximoAlinhado(corPeca, tabuleiro) >= 4) {
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public boolean verificaFimDeJogo(int[][] tabuleiro) {
        return verificaVitoria(PECA_BRANCA, tabuleiro) || verificaVitoria(PECA_PRETA, tabuleiro);
    }
    
    private float geraCustoPeca(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
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

    @Override
    public float geraCusto(int corPeca, int[][] tabuleiro, int minPontos, int maxPontos) {
        if(verificaVitoria(corPeca, tabuleiro)) {
            return maxPontos;
        }
        else if(verificaVitoria(invertePeca(corPeca), tabuleiro)) {
            return minPontos;
        }
        else{
            return geraCustoPeca(corPeca, tabuleiro, minPontos, maxPontos)*0.3f + geraCustoPeca(invertePeca(corPeca), tabuleiro, minPontos, maxPontos)*-0.7f;
        }
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
    public void interpretaJogadaPlayer(int[] posClick) {
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
}
    
