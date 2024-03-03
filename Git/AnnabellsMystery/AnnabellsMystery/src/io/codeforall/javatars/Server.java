package io.codeforall.javatars;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int PORT = 9090;
    private static final int MAX_PLAYERS = 2;
    private final Map<Socket, PrintWriter> clients = new ConcurrentHashMap<>();
    private int connectedPlayers = 0;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (connectedPlayers < MAX_PLAYERS) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket);

                connectedPlayers++;

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clients.put(clientSocket, writer);

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }

            startChat();
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startChat() {
        for (Socket socket : clients.keySet()) {
            PrintWriter writer = clients.get(socket);
            writer.println("Chat started! You can now chat with the other player.");
        }
    }

    private void startGame() {
        for (Socket socket : clients.keySet()) {
            try {
                new Thread(new AnnabellsHouse(socket, clients)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final BufferedReader reader;

        public ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    for (Socket socket : clients.keySet()) {
                        if (socket != clientSocket) {
                            PrintWriter writer = clients.get(socket);
                            writer.println("Jogador: " + message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
