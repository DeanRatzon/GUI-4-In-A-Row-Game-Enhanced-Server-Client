
/** This class represents a thread which handles a the communication of a user's trivia game with the server
 * @author Dean Ratzon
 * @version 12/01/19
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class TriviaThread extends Thread {
	final int TOTAL_QUESTIONS = 20;
	private Socket socket = null;
	private ArrayList<Question> questions = new ArrayList<Question>();
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public TriviaThread(Socket socket) {
		this.socket = socket;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		ReadFile file = new ReadFile();

		for (int i = 0; i < TOTAL_QUESTIONS; i++) {

			Question q = file.getQuestion(i);
			questions.add(new Question(q));
		}
		int i = 0;
		while (questions.size() > 0) {
			i = getRandomQuestionNumber();
			try {
				out.writeObject(questions.get(i));
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
			questions.remove(i);
		}

		// try {
		// out.close();
		// in.close();
		// socket.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public int getRandomQuestionNumber() {
		Random r = new Random();
		int low = 0;
		int high = questions.size();
		int result = r.nextInt(high - low) + low;
		return result;

	}

}
