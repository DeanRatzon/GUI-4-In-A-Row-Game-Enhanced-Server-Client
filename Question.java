
/**This class represents a question object to be used in the trivia game
 * @author Dean Ratzon
 * @version 12/01/19
 */

import java.io.Serializable;

public class Question implements Serializable {

	private String question;
	private String[] answers = new String[4];
	private int correctAnswer;

	/**
	 * Constructs a question from given parameters
	 * 
	 * @param question
	 *            represents the question string
	 * @param answer1
	 *            represents the first answer string
	 * @param answer2
	 *            represents the second answer string
	 * @param answer3
	 *            represents the third answer string
	 * @param answer4
	 *            represents the fourth answer string
	 * @param correctAnswer
	 *            represents the correct answer's number
	 */
	public Question(String question, String answer1, String answer2, String answer3, String answer4,
			int correctAnswer) {

		this.question = question;
		this.correctAnswer = correctAnswer;
		answers[0] = answer1;
		answers[1] = answer2;
		answers[2] = answer3;
		answers[3] = answer4;

	}

	/**
	 * Copy constructor for a Question object
	 * 
	 * @param q
	 *            represents the Question object to be copied
	 */
	public Question(Question q) {
		this.question = new String(q.question);
		answers[0] = new String(q.answers[0]);
		answers[1] = new String(q.answers[1]);
		answers[2] = new String(q.answers[2]);
		answers[3] = new String(q.answers[3]);
		this.correctAnswer = q.correctAnswer;
	}

	/**
	 * Constructs an empty Question object
	 * 
	 */
	public Question() {
		question = null;
		answers[0] = null;
		answers[1] = null;
		answers[2] = null;
		answers[3] = null;
		correctAnswer = 0;
	}

	/**
	 * Returns a given number answer
	 * 
	 * @param answerNumber
	 *            represents the answer number to return
	 * @return a specific answer
	 */
	public String getAnswer(int answerNumber) {
		return answers[answerNumber];
	}

	/**
	 * Sets one of this Question's answers to a new given answer (value)
	 * 
	 * @param answerNumber
	 *            represents this Question's answer number that should have its
	 *            value changes
	 * @param newAnswer
	 *            represents the new answer value to be entered
	 */
	public void setAnswer(int answerNumber, String newAnswer) {
		answers[answerNumber] = newAnswer;
	}

	/**
	 * Returns this Question's object question string
	 * 
	 * @return this Question's question string
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * returns the this Question's correct answer number
	 * 
	 * @return this Question's correct answer number
	 */
	public int getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * Sets this Question's question string
	 * 
	 * @param question
	 *            represents the new question string to be set to this Question's
	 *            question string
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Sets this Question's correct answer number
	 * 
	 * @param correctAnswer
	 *            represents the new correct answer number to be set
	 */
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

}
