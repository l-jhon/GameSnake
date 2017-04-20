package jogo_cobra;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import utils.Constants;

public class Grid extends JPanel implements ActionListener {

	private int[] x = new int[Constants.TODOS_PONTOS];
	private int[] y = new int[Constants.TODOS_PONTOS];

	private int pontos = 1;
	private int comida_x;
	private int comida_y;
	private int morte_x;
	private int morte_y;

	private int PONTUA��O = 0;
	private int BONUS = 0;
	private int PONTUACAO_TOTAL = 0;
	
	String PONTUACAO_MSG = "PONTUA��O: " + PONTUA��O + BONUS;
	Font PONTUACAO_FONTE = new Font("Consolas", Font.BOLD, 12);
	FontMetrics PONTUACAO_METRICA = this.getFontMetrics(PONTUACAO_FONTE);

	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;

	private boolean playing = true;

	private Image bola;
	private Image comida;
	private Image cabeca;

	public Grid() {
		
		addKeyListener(new TAdapter());

		setBackground(Color.GRAY);
		setFocusable(true);
		setSize(Constants.WIDTH, Constants.HEIGHT);

		inicializaJogo();
		setImages();
	}
	
	public void inicializaJogo() {

		x[0] = 50;
		y[0] = 50;

		positionFood();

		Timer time = new Timer(Constants.DELAY, this);
		time.start();
	}
	
	public void setImages() {
		bola = new ImageIcon("C:\\Users\\pedro\\Desktop\\bola.png").getImage();
		comida = new ImageIcon("C:\\Users\\pedro\\Desktop\\comida.png").getImage();
		cabeca = new ImageIcon("C:\\Users\\pedro\\Desktop\\cabe�a.png").getImage();
	}

	public void positionFood() {
		int random = (int) (Math.random() * Constants.RAND_POSICAO);
		comida_x = (random * Constants.TAMANHO_PONTO);

		random = (int) (Math.random() * Constants.RAND_POSICAO);
		comida_y = (random * Constants.TAMANHO_PONTO);
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);

		if (playing) {
			
			g.drawImage(comida, comida_x, comida_y, this);

			for (int i = 0; i < pontos; i++) {
			
				if (i == 0) {
					g.drawImage(cabeca, x[i], y[i], this);
				} else {
					g.drawImage(bola, x[i], y[i], this);
				}
				
			}

			drawPoints(g);

			Toolkit.getDefaultToolkit().sync();

			g.dispose();
		} else {
			endGame(g);
		}
	}

	public void drawPoints(Graphics g) {
		
		PONTUACAO_TOTAL = PONTUA��O + BONUS;
		PONTUACAO_MSG = "PONTUA��O: " + PONTUACAO_TOTAL;
		PONTUACAO_METRICA = this.getFontMetrics(PONTUACAO_FONTE);

		g.setColor(Color.white);
		g.setFont(PONTUACAO_FONTE);
		g.drawString(PONTUACAO_MSG, (Constants.WIDTH - PONTUACAO_METRICA.stringWidth(PONTUACAO_MSG)) - 10, Constants.HEIGHT - 10);
	}

	// Desenha mensagem no final do jogo
	public void endGame(Graphics g) {

		String msgScoreTotal = "FIM DE JOGO! Sua pontua��o total: " + PONTUACAO_TOTAL;
		String msgScoreFood = "Pontua��o por alimento: " + PONTUA��O ;
		String msgBonus = " Seu Bonus: " + BONUS ;
		int sizeSnake = this.pontos - 1;
		String msgSizeSnake = " Tamanho da Cobra: " + sizeSnake;
		String msgPositionDeath = " Morte da Cobra: (" + morte_x + " , " + morte_y + ")" ;

		Font pequena = new Font("Consolas", Font.BOLD, 14);
		FontMetrics metrica = this.getFontMetrics(pequena);

		g.setColor(Color.white);	
		g.setFont(pequena);

		g.drawString(msgScoreTotal, (Constants.WIDTH - metrica.stringWidth(msgScoreTotal)) / 2, (Constants.HEIGHT / 2) - 20);
		g.drawString(msgScoreFood, (Constants.WIDTH - metrica.stringWidth(msgScoreFood)) / 2, Constants.HEIGHT / 2);
		g.drawString(msgBonus, (Constants.WIDTH - metrica.stringWidth(msgBonus)) / 2, (Constants.HEIGHT / 2) + 20);
		g.drawString(msgSizeSnake, (Constants.WIDTH - metrica.stringWidth(msgSizeSnake)) / 2, (Constants.HEIGHT / 2) + 40);
		g.drawString(msg_morte, (Constants.WIDTH - metrica.stringWidth(msg_morte)) / 2, (Constants.HEIGHT / 2) + 60);
		
	}

	public void checarComida() {
		if ((x[0] == comida_x) && (y[0] == comida_y)) {
			pontos++;
			PONTUA��O+=10;
			
			if (PONTUA��O % 30 == 0) {
				BONUS += 10;
				
				// CRIAR OBSTACULOS ****************************
			}
		
			positionFood();
		}
	}

	// M�todo para mover a cobrinha na tela
	public void mover() {
		// Para cada ponto da cobrinha desenha em (x,y)
		for (int i = pontos; i > 0; i--) {
			x[i] = x[(i - 1)];
			y[i] = y[(i - 1)];
		}

		// Se for para esquerda decrementa em x
		if (left) {
			x[0] -= Constants.TAMANHO_PONTO;
		}

		// Se for para direita incrementa em x
		if (right) {
			x[0] += Constants.TAMANHO_PONTO;
		}

		// Se for para cima decrementa em y
		if (up) {
			y[0] -= Constants.TAMANHO_PONTO;
		}

		// Se for para baixo incrementa em y
		if (down) {
			y[0] += Constants.TAMANHO_PONTO;
		}

	}

	// M�todo para checar colis�o entre a cobrinha e as bordas do jogo
	public void checarColisao() {
		// Para cada ponto, verifica se este est� em posi��o com outro ponto
		// se estiver ele avista que o jogador parou de jogar devido a colis�o
		for (int i = pontos; i > 0; i--) {
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                playing = false;
				morte_x = x[i];
				morte_y = y[i];
			}

		}
		
		// CHECAR OBSTACULOS ****************************

		if (y[0] > Constants.HEIGHT) {
			y[0] = 0;
			//estaJogando = false;
		}

		if (y[0] < 0) {
			y[0] = Constants.HEIGHT;
			//estaJogando = false;
		}

		if (x[0] > Constants.WIDTH) {
			x[0] = 0;
			//estaJogando = false;
		}

		if (x[0] < 0) {
			x[0] = Constants.WIDTH;
			//estaJogando = false;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (playing) {
			checarComida();
			checarColisao();
			mover();
		}

		repaint();
	}
	
	private class TAdapter extends KeyAdapter {

		// M�todo para verificar o que foi teclado
		@Override
		public void keyPressed(KeyEvent e) {
			// Obt�m o c�digo da tecla
			int key = e.getKeyCode();

			// Verifica os movimentos e manipula as vari�veis, para movimentar
			// corretamente sobre a tela
			if ((key == KeyEvent.VK_LEFT) && (!right)) {
				left = true;
				up = false;
				down = false;
			}

			if ((key == KeyEvent.VK_RIGHT) && (!left)) {
				right = true;
				up = false;
				down = false;
			}

			if ((key == KeyEvent.VK_UP) && (!down)) {
				up = true;
				left = false;
				right = false;
			}

			if ((key == KeyEvent.VK_DOWN) && (!up)) {
				down = true;
				left = false;
				right = false;
			}
		}
	}
}