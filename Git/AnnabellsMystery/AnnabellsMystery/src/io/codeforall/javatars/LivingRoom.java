package io.codeforall.javatars;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class LivingRoom implements Investigate {
    private final PrintWriter printWriter;
    private final Prompt prompt;
    private final GameRoom gameRoom = new GameRoom();
    private boolean hasDagger = false;

    public LivingRoom(PrintWriter printWriter, Socket clientSocket) throws IOException {
        this.printWriter = printWriter;
        this.prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
    }


    @Override
    public void investigate() {
        printMessage(LIVING_ROOM_INVESTIGATION_MESSAGE);
        printMessage(LIVING_ROOM_INTRODUCTION);

        boolean keepChoosing = true;

        while (keepChoosing) {
            String[] options = {LETTER, DAGGER, KEYS};
            MenuInputScanner story = new MenuInputScanner(options);
            story.setMessage(SELECTION);
            int answer = prompt.getUserInput(story);

            if (answer == 1) {
                printMessage(LETTER_MESSAGE);
            }

            if (answer == 2) {
                printMessage(DAGGER_MESSAGE);

                String[] options3 = {"Stay with it", "Leave it where it was."};
                MenuInputScanner story1 = new MenuInputScanner(options3);
                story1.setMessage(LIVING_ROOM_CHOISE);
                int answer2 = prompt.getUserInput(story1);

                if (answer2 == 1) {
                    hasDagger = true;
                    printMessage(LIVING_ROOM_CHOISE2);
                }

                if (answer2 == 3) {
                    continue;
                }
            }

            if (answer == 3) {
                printMessage(KEYS_DESCRIPTION);
                gameRoom.investigate();
                keepChoosing = false;
            }

            if (answer == 0 || answer > 3) {
                printMessage(DAGGAR_MESSAGE2);
            }
        }
    }

    private void printMessage(String message) {
        printWriter.println(message);
    }

    private final String LIVING_ROOM_INVESTIGATION_MESSAGE = "Arriving at the living room";
    private final String LIVING_ROOM_INTRODUCTION = "Upon entering the living room, you feel a chilling aura envelop you. Shadows flicker \n" +
            " in the dim light, revealing ancient furniture. At the center, a table holds an old bottle, a dagger, and an old key. \n" +
            " You sense a strange feeling of not being alone...";
    private final String LETTER = "Read the leter";
    private final String DAGGER = "Take the dagger";
    private final String KEYS = "Take the Keys";

    private final String KEYS_DESCRIPTION = "As I examine the key, I recognize it. She always carries this old key, and I suspect it belongs to a mysterious chest in the game room.";


    private final String SELECTION = "Which one should I choose?";

    private final String LETTER_MESSAGE = "Dear Anabela,\n" +
            "\n" +
            "It has been a pleasure and a rollercoaster of laughter sharing these moments with you. Who would have thought our voices would meet on the radio, right?\n" +
            "\n" +
            "Inside this envelope, there's a voucher for a scale. Not to measure weight, but to balance out the laughter we shared. May it remind you of the light and funny moments we had together.\n" +
            "\n" +
            "Until the next tune, Anabela!\n" +
            "\n" +
            "With affection and a guaranteed smile,";

    private final String DAGGER_MESSAGE = "A chilling sensation runs down my spine as I hold the dagger. It feels like I've awakened something dormant, something that was waiting for this moment to emerge.";

    private final String DAGGAR_MESSAGE2 = "I'll keep it for later, I might need it.";

    private final String LIVING_ROOM_CHOISE = "What would you like to do?";
    private final String LIVING_ROOM_CHOISE2 = "You made a good choice";

}