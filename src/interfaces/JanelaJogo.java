package interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

import jogos.util.*;

public class JanelaJogo extends JFrame {
    private Jogo jogo;
    private JPanel telaJogo = new TelaJogo();

    public static final int LARGURA_TELA = 600;
    public static final int ALTURA_TELA = 600;
    private static final int TAMANHO_PECA = (int)LARGURA_TELA/(2*Jogo.LARGURA_TABULEIRO);
    private static final int DELAY_TIMER = 75;

    private int corPecaHumano = Jogo.SEM_PECA;
    private boolean humanoJogando = false;
    private Jogada jogadaDoHumano = null;
    
    private boolean assistindoReplay = false;
    private boolean proximaJogadaReplay = false;
    private boolean jogadaAnteriorReplay = false;

    public Jogo getJogo() {
        return jogo;
    }
    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
    public JPanel getTelaJogo() {
        return telaJogo;
    }
    public void setTelaJogo(JPanel telaJogo) {
        this.telaJogo = telaJogo;
    }
    public int getCorPecaHumano() {
        return corPecaHumano;
    }
    public void setCorPecaHumano(int corPecaHumano) {
        this.corPecaHumano = corPecaHumano;
    }
    public boolean isHumanoJogando() {
        return humanoJogando;
    }
    public void setHumanoJogando(boolean humanoJogando) {
        this.humanoJogando = humanoJogando;
    }
    public Jogada getJogadaDoHumano() {
        return jogadaDoHumano;
    }
    public void setJogadaDoHumano(Jogada jogadaDoHumano) {
        this.jogadaDoHumano = jogadaDoHumano;
    }
    public boolean isAssistindoReplay() {
        return assistindoReplay;
    }
    public void setAssistindoReplay(boolean assistindoReplay) {
        this.assistindoReplay = assistindoReplay;
    }
    public boolean isProximaJogadaReplay() {
        return proximaJogadaReplay;
    }
    public void setProximaJogadaReplay(boolean proximaJogadaReplay) {
        this.proximaJogadaReplay = proximaJogadaReplay;
    }
    public boolean isJogadaAnteriorReplay() {
        return jogadaAnteriorReplay;
    }
    public void setJogadaAnteriorReplay(boolean jogadaAnteriorReplay) {
        this.jogadaAnteriorReplay = jogadaAnteriorReplay;
    }

    public JanelaJogo(Jogo jogo) {
        setJogo(jogo);
        getJogo().setDelayJogada(500);

        this.add(getTelaJogo());
        this.setTitle(jogo.getNome());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    private int calculaPosicaoFila(int posicaoObjeto, int tamanhoObjeto, int pixelsFila, int tamanhoFila) {
        return ((pixelsFila/(tamanhoFila+1)) *(posicaoObjeto+1)) - (tamanhoObjeto/2);
    }
    private void desenhaPecas(Graphics g) {
        int posicaoX;
        int posicaoY;
        for(int y=0; y < Jogo.ALTURA_TABULEIRO; y++) {
            for(int x=0; x < Jogo.LARGURA_TABULEIRO; x++) {
                switch(getJogo().getTabuleiro()[x][y]) {
                    case Jogo.SEM_PECA: 
                        posicaoX = calculaPosicaoFila(x, (int)(TAMANHO_PECA/1.5), LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, (int)(TAMANHO_PECA/1.5), ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
                        g.setColor(Color.lightGray);
                        g.fillOval(posicaoX, posicaoY, (int)(TAMANHO_PECA/1.5), (int)(TAMANHO_PECA/1.5));
                        break;
                    case Jogo.PECA_BRANCA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
                        g.setColor(Color.white);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        if(getJogo().isSelecionado() && getJogo().getPosSelecionado()[0] == x && getJogo().getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        else {
                            g.setColor(Color.black);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        break;
                    case Jogo.PECA_PRETA:
                        posicaoX = calculaPosicaoFila(x, TAMANHO_PECA, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
                        posicaoY = calculaPosicaoFila(y, TAMANHO_PECA, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
                        g.setColor(Color.black);
                        g.fillOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        if(getJogo().isSelecionado() && getJogo().getPosSelecionado()[0] == x && getJogo().getPosSelecionado()[1] == y) {
                            g.setColor(Color.blue);
                            g.drawOval(posicaoX, posicaoY, TAMANHO_PECA, TAMANHO_PECA);
                        }
                        break;
                }
            }
        }
    }
    private void desenhaLinhas(Graphics g) {
        g.setColor(Color.black);
        int inicioX;
        int inicioY;
        int fimX;
        int fimY;
        for(int x=0; x<Jogo.LARGURA_TABULEIRO; x++) {
            inicioX = calculaPosicaoFila(x, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            fimX = inicioX;
            fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        for(int y=0; y<Jogo.LARGURA_TABULEIRO; y++) {
            inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            inicioY = calculaPosicaoFila(y, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
            fimX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
            fimY = inicioY;
            g.drawLine(inicioX, inicioY, fimX, fimY);
        }
        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(0, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        inicioY = calculaPosicaoFila((int)Math.ceil(Jogo.ALTURA_TABULEIRO/2), 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        fimX = calculaPosicaoFila((int)Math.ceil(Jogo.LARGURA_TABULEIRO/2), 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);

        inicioX = calculaPosicaoFila(Jogo.LARGURA_TABULEIRO-1, 1, LARGURA_TELA, Jogo.LARGURA_TABULEIRO);
        fimY = calculaPosicaoFila(0, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
        fimY = calculaPosicaoFila(Jogo.ALTURA_TABULEIRO-1, 1, ALTURA_TELA, Jogo.ALTURA_TABULEIRO);
        g.drawLine(inicioX, inicioY, fimX, fimY);
    }
    public void desenhaTabuleiro(Graphics g) {
        desenhaLinhas(g);
        desenhaPecas(g);
    }

    public void replayHistoricoJogadas() throws InterruptedException {
        setAssistindoReplay(true);
        getJogo().inicializaTabuleiro();
        int posicaoReplay = -1;
        while(isAssistindoReplay()) {
            if(isProximaJogadaReplay()) {
                if(posicaoReplay < getJogo().getHistoricoJogadas().size()-1) {
                    posicaoReplay++;
                    getJogo().fazJogada(getJogo().getHistoricoJogadas().get(posicaoReplay), getJogo().getTabuleiro(), true);
                }
                setProximaJogadaReplay(false);
            }
            if(isJogadaAnteriorReplay()) {
                if(posicaoReplay >= 0) {
                    getJogo().desfazJogada(getJogo().getHistoricoJogadas().get(posicaoReplay), true);
                    posicaoReplay--;
                }
                setJogadaAnteriorReplay(false);
            }
            Thread.sleep(1);
        }
    }
    public void carregaLog(String caminho) {
        try {
            File arquivo = new File(caminho);
            FileReader leitorDeArquivo = new FileReader(arquivo);
            BufferedReader leitorDeBuffer = new BufferedReader(leitorDeArquivo);   
            String textoJogada;  
            getJogo().setHistoricoJogadas(new ArrayList<Jogada>());
            while((textoJogada = leitorDeBuffer.readLine()) != null) {
                Jogada jogada = new Jogada(textoJogada);
                getJogo().getHistoricoJogadas().add(jogada);
            }
            leitorDeBuffer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public class TelaJogo extends JPanel implements ActionListener {
        private Timer timer;

        public Timer getTimer() {
            return timer;
        }
        public void setTimer(Timer timer) {
            this.timer = timer;
        }

        TelaJogo() {
            setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
            setFocusable(true);
            setBackground(Color.white);
            addMouseListener(new EventosMouse());
            addKeyListener(new EventosTeclado());
            iniciaTimer();
        }
            //  funções da engine
        public void iniciaTimer() {
            setTimer(new Timer(DELAY_TIMER, this));
            getTimer().start();
        }
        public void paraTimer() {
            getTimer().stop();
            repaint();
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenhaTabuleiro(g);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }  
    }

    // classe de que captura eventos do mouse
    public class EventosMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] posClick = {(int)(e.getX()*Jogo.LARGURA_TABULEIRO)/LARGURA_TELA, (int)(e.getY()*Jogo.ALTURA_TABULEIRO)/ALTURA_TELA};
            if(isHumanoJogando()) {
                setJogadaDoHumano(getJogo().humanoFazJogada(posClick, getCorPecaHumano()));
                if(!Objects.isNull(getJogadaDoHumano()))
                    setHumanoJogando(false);
            }
        }
    }

    public class EventosTeclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(isAssistindoReplay())
                        setJogadaAnteriorReplay(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    if(isAssistindoReplay())
                        setProximaJogadaReplay(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    setAssistindoReplay(false);
                    break;
                default:
                    break;
            }
        }
    }

}
