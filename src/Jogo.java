import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Jogo extends JPanel implements ActionListener {
    static final int LARGURA_TELA = 600;
    static final int ALTURA_TELA = 600;
    static final int DELAY = 75;
    private Timer timer;
    private String nome;
    private int[][] tabuleiro;

    Jogo() {
        this.setPreferredSize(new Dimension(LARGURA_TELA,ALTURA_TELA));
        this.setFocusable(true);
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int[][] getTabuleiro() {
        return tabuleiro;
    }
    public void setTabuleiro(int[][] tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public Timer getTimer() {
        return timer;
    }
    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    public void iniciaTimer() {
        setTimer(new Timer(DELAY, this));
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
    public void desenhaTabuleiro(Graphics g) {
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }    
}
