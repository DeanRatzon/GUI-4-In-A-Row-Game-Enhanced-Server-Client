
/** This class represents a client and its gui for the online trivia game
 * @author Dean Ratzon
 * @version 12/01/19
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Client extends JFrame {

	final int TOTAL_QUESTIONS = 20;
	final int TOTAL_ANSWERS = 4;
	final int POINTS_UP = 10;
	final int POINTS_DOWN = 5;
	private String answerTime;
	private final GridLayout gridLayout1;
	private JPanel ansPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	private JPanel pointsPanel = new JPanel();
	private JPanel midPanel = new JPanel();
	private JPanel timePanel = new JPanel();
	private JLabel timeLeft = new JLabel("Time Left: ");
	private JLabel time = new JLabel();
	private JButton[] answers = new JButton[TOTAL_ANSWERS];
	private JButton restart = new JButton("Restart");
	private JButton end = new JButton("End Game");
	private pressEvent actEvent = new pressEvent();
	private timeEvent timeEv = new timeEvent();
	private Timer timer = new Timer(1000, timeEv);
	private int points = 0;
	private JLabel currentQuestion = new JLabel("Question", SwingConstants.CENTER);
	private JLabel pointsSum = new JLabel("  Points: " + points, SwingConstants.LEFT);
	private int correctAnswer;
	private String correctAnswerString;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private String host;
	private int count = 0;

	public static void main(String[] args) throws IOException {
		String host = "localhost";
		String setTime = "10";
		if (args.length > 0) {
			host = args[0];
			setTime = args[1];
		}
		Client gui = new Client(host, setTime);
		gui.setSize(new Dimension(600, 400));
		gui.setVisible(true);
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Constructs the client gui
	 * 
	 * @param getHost
	 *            represents the host name
	 * @param setTime
	 *            represents the given time to answer each question
	 */
	public Client(String getHost, String setTime) {
		super("Trivia");
		host = getHost;
		answerTime = setTime;
		gridLayout1 = new GridLayout(4, 1, 5, 5);
		setLayout(gridLayout1);
		ansPanel.setLayout(new GridLayout(1, answers.length));

		// Welcome message, instructions and start a game
		JOptionPane.showMessageDialog(null, "Welcome to the Trivia! \n\nInstructions :\nYou get " + answerTime
				+ " seconds to complete each question \nPick an answer from the selection\nIf your answer is correct you earn "
				+ POINTS_UP + " points\nIf your answer is wrong or not in the time frame you lose " + POINTS_DOWN
				+ " points");
		int start = JOptionPane.showConfirmDialog(null, "Are you ready to start the Trivia?", "Welcome to the Trivia",
				JOptionPane.YES_NO_OPTION);
		if (start != JOptionPane.YES_OPTION)
			System.exit(0);

		// establish connection to the server
		System.out.println("Connecting to server");
		connect(host);

		// top panel (restart/end game)
		end.addActionListener(actEvent);
		restart.addActionListener(actEvent);
		topPanel.add(restart);
		topPanel.add(end);
		add(topPanel);

		// mid panel (points, time left)
		midPanel.setLayout(new BorderLayout());
		pointsSum.setFont(new Font("", Font.BOLD, 20));
		pointsPanel.add(pointsSum);
		midPanel.add(pointsPanel, BorderLayout.WEST);

		timeLeft.setFont(new Font("", Font.BOLD, 20));
		time.setFont(new Font("", Font.BOLD, 20));
		time.setText(setTime);
		timePanel.add(timeLeft);
		timePanel.add(time);
		midPanel.add(timePanel, BorderLayout.EAST);
		add(midPanel);

		// question and answers
		currentQuestion.setFont(new Font("", Font.BOLD, 24));
		add(currentQuestion);

		for (int i = 0; i < TOTAL_ANSWERS; i++) {
			answers[i] = new JButton();
			answers[i].addActionListener(actEvent);
		}
		ansPanel.add(answers[0]);
		ansPanel.add(answers[1]);
		ansPanel.add(answers[2]);
		ansPanel.add(answers[3]);
		add(ansPanel);
		nextQuestion();
	}

	/**
	 * Moves up to the next Question, reads a Question object from the server and
	 * displays it
	 * 
	 */
	public void nextQuestion() {
		if (count < TOTAL_QUESTIONS) {
			count++;
			Question q = null;
			try {
				q = (Question) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			currentQuestion.setText(q.getQuestion());
			correctAnswer = q.getCorrectAnswer();
			correctAnswerString = q.getAnswer(correctAnswer);
			ArrayList<String> ans = new ArrayList<String>();
			for (int i = 0; i < TOTAL_ANSWERS; i++) {
				ans.add(q.getAnswer(i));
			}
			Collections.shuffle(ans);
			for (int i = 0; i < TOTAL_ANSWERS; i++) {
				answers[i].setText(ans.get(i));
				answers[i].setBackground(null);
			}
			time.setText(answerTime);
			timer.start();
		} else {
			gameEnded();
		}

	}

	/**
	 * Establish connection to the server
	 * 
	 * @param host
	 *            represents the server host name
	 */
	public void connect(String host) {

		InetAddress ip = null;
		try {
			ip = InetAddress.getByName("localhost");
			socket = new Socket(ip, 3333);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			System.out.println("dont know host:" + ip);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("could not get the I/O for the connection to " + ip);
			System.exit(1);
		}
		System.out.println("Connected to server");
	}

	/**
	 * Disconnects from the server
	 * 
	 */
	public void closeConnection() {
		try {
			in.close();
			out.close();
			socket.close();
			System.out.println("Disconnected from the server");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Ending the current game, start a new game or close the application options
	 * 
	 */
	private void gameEnded() {
		timer.stop();
		int start = JOptionPane.showConfirmDialog(null,
				"Game over! Your score is: " + points + "\nDo you want to Play again?", "GAME OVER",
				JOptionPane.YES_NO_OPTION);
		if (start == JOptionPane.YES_OPTION)
			initialize();
		else
			System.exit(0);
	}

	/**
	 * Initializes the application in order to start a new game, client reconnects
	 * to the server
	 * 
	 */
	public void initialize() {
		closeConnection();
		connect(host);
		count = 0;
		int start = JOptionPane.showConfirmDialog(null, "Are you ready to start the Trivia?", "Start a new game",
				JOptionPane.YES_NO_OPTION);
		if (start != JOptionPane.YES_OPTION)
			System.exit(0);
		points = 0;
		pointsSum.setText("  Points: " + String.valueOf(points));

		timer.restart();
		timer.start();
	}

	/**
	 * Action Listener for selecting an answer
	 * 
	 * 
	 */
	public class pressEvent implements ActionListener {
		public void actionPerformed(ActionEvent actEvent) {
			timer.stop();
			if (actEvent.getSource() == restart) {

				initialize();
			}
			if (actEvent.getSource() == end) {
				closeConnection();
				System.exit(0);
			}
			for (int i = 0; i < TOTAL_ANSWERS; i++) {
				if (actEvent.getSource() == answers[i])
					// correct
					if (answers[i].getText().equals(correctAnswerString)) {
						answers[i].setBackground(Color.GREEN);

						points = points + POINTS_UP;
						pointsSum.setText("  Points: " + String.valueOf(points));
						JOptionPane.showMessageDialog(null, "Correct!");
						// wrong
					} else {
						for (int j = 0; j < TOTAL_ANSWERS; j++)
							if (answers[j].getText().equals(correctAnswerString)) {
								answers[j].setBackground(Color.GREEN);
								answers[i].setBackground(Color.RED);
								JOptionPane.showMessageDialog(null,
										"Wrong!, The correct answer is: " + correctAnswerString);
							}

						if (points > 0) {
							points = points - POINTS_DOWN;
							pointsSum.setText("  Points: " + String.valueOf(points));

						}
					}
			}

			nextQuestion();

		}
	}

	/**
	 * Action Listener for the time events
	 * 
	 * 
	 */
	public class timeEvent implements ActionListener {
		public void actionPerformed(ActionEvent timeEv) {
			if (Integer.parseInt(time.getText()) > 0) {
				time.setText(Integer.toString(Integer.parseInt(time.getText()) - 1));
			} else {
				timer.stop();
				if (points > 0) {
					points = points - POINTS_DOWN;
					pointsSum.setText("  Points: " + String.valueOf(points));
				}
				for (int j = 0; j < TOTAL_ANSWERS; j++)
					if (answers[j].getText().equals(correctAnswerString))
						answers[j].setBackground(Color.GREEN);
				JOptionPane.showMessageDialog(null, "Time is up!, The correct answer is: " + correctAnswerString);

				nextQuestion();
			}

		}

	}
}
