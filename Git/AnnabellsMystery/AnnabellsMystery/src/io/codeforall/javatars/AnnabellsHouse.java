package io.codeforall.javatars;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class AnnabellsHouse implements Runnable {

    private final Socket clientSocket;
    private final PrintWriter writer;
    private final Prompt prompt;
    private final Pool pool;

    public AnnabellsHouse(Socket clientSocket, Map<Socket, PrintWriter> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.writer = clients.get(clientSocket);
        this.prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
        this.pool = new Pool(writer, clientSocket);
    }

    @Override
    public void run() {
        sendMessageToClient("Welcome to the game!");

        start();
    }

    public void start() {
        printMessage(Messages.HOUSE_INTRODUCTION);

        String[] options = {"Uncle", "Aunt"};
        MenuInputScanner story = new MenuInputScanner(options);
        story.setMessage(Messages.HOUSE_WELCOME_MESSAGE);
        int answer = prompt.getUserInput(story);

        StringInputScanner askName = new StringInputScanner();
        askName.setMessage(Messages.HOUSE_ASK_NAME);
        String username = prompt.getUserInput(askName);

        printMessage(String.format(Messages.HOUSE_ENTRY_MESSAGE, options[answer - 1], username));
        printMessage(Messages.HOUSE_STORY);
        printMessage(Messages.HOUSE_WAKEUP);

        String[] options2 = {"Go back to bed", "Follow the noise"};
        MenuInputScanner story2 = new MenuInputScanner(options2);
        story2.setMessage(Messages.HOUSE_NOISE_MESSAGE);
        int answer2 = prompt.getUserInput(story2);

        if (answer2 == 2) {
            pool.investigate();
        }
    }

    private void printMessage(String message) {
        writer.println(message);
    }

    private synchronized void sendMessageToClient(String message) {
        writer.println(message);
    }
}
