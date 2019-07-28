
/** This class used to read the questions and answers file to be used in the trivia game
 * @author Dean Ratzon
 * @version 12/01/19
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ReadFile {

	private ArrayList<Question> questions = new ArrayList<Question>();

	/**
	 * Constructs a Question objects list by reading the questions and answers from
	 * a file
	 * 
	 */
	public ReadFile() {

		try {
			Scanner input = new Scanner(new File("trivia.txt"));
			while (input.hasNext()) {
				String q = input.nextLine();
				String a1 = input.nextLine();
				String a2 = input.nextLine();
				String a3 = input.nextLine();
				String a4 = input.nextLine();
				String a5 = input.nextLine();

				Question current = new Question(q, a1, a2, a3, a4, Integer.parseInt(a5));
				questions.add(current);

			}
			input.close();

		} catch (java.io.FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File Not Found, Shutting down.");
			System.exit(0);
		}

	}

	/**
	 * This method returns a specific Question object from the questions list
	 * 
	 * @param i
	 *            represents the question's location in the list
	 * @return a Question object from the list
	 */
	public Question getQuestion(int i) {
		return new Question(questions.get(i));
	}

	/**
	 * This method returns the questions list
	 * 
	 * @returns the question list
	 */
	public ArrayList<Question> getQuestionList() {
		return new ArrayList<Question>(questions);
	}

}
