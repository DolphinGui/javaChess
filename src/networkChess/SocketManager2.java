package networkChess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;

public class SocketManager2 {
    private ServerSocket serverSocket; 
    private Socket clientSocket;
    
    private DataInputStream input;
    private DataOutputStream output;
    
    void initClient(String ip, int port){
	try {
	    clientSocket = new Socket(ip, port);
	    
	    input = new DataInputStream(clientSocket.getInputStream());
	    output = new DataOutputStream(clientSocket.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }
    
    void initServer(int port){
	try {
	    serverSocket = new ServerSocket(port);
	    clientSocket = serverSocket.accept();
	    
	    input = new DataInputStream(clientSocket.getInputStream());
	    output = new DataOutputStream(clientSocket.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
    }
    
    void send(BitSet message) {
	try {
	    output.write(message.toByteArray());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    void send(byte[] message) {
	try {
	    output.write(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    byte[] read() {
	try {
	    return input.readAllBytes();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }
    String readAsString() {
	try {
	    return new String(input.readAllBytes());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    void close() throws IOException {
	serverSocket.close();
	clientSocket.close();
	input.close();
	output.close();
    }
}
