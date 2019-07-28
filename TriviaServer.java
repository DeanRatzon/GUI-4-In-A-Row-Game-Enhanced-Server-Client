
/** This class represents the server for the online trivia game
 * @author Dean Ratzon
 * @version 12/01/19
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TriviaServer {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(3333);
		} catch (IOException e) {
			System.err.println("Could not listen to port 3333");
			System.exit(1);
		}
		System.out.println("server ready");

		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();

				Thread t = new TriviaThread(socket);
				t.start();
			} catch (IOException e) {
				System.err.println("Accept failed");
				serverSocket.close();
				System.exit(1);

			}

		}
	}
}