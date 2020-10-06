package networkChess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {

	private ServerSocket serverSocket; 
	private Socket clientSocket;

	public DataInputStream input;
	public DataOutputStream output;


	public void initClient(String ip, int port){
		try {
			clientSocket = new Socket(ip, port);

			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void initServer(int port){
		try {
			serverSocket = new ServerSocket(port);
			clientSocket = serverSocket.accept();

			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void send(String message) {
		try {
			output.write(message.getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String readAsString() {
		try {
			return new String(input.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() throws IOException {
		if(serverSocket!=null) {
			serverSocket.close();
		}
		if(clientSocket!=null) {
			clientSocket.close();
		}
		input.close();
		output.close();
	}


	public void host(int port) throws IOException {
			initServer(port);
			String buffer = readAsString();
			if(buffer.equals("helloSever")) {
				send("helloClient");
			}else {
				throw new ProtocolException("Client did not respond");
			}
	}

	public void connect(int port, String ip) throws IOException {
		initClient(ip, port);
		send("helloSever");
		String buffer = readAsString();
		if(!buffer.equals("helloSever")) {
			throw new ProtocolException("Server did not respond");
		}
	}


}
