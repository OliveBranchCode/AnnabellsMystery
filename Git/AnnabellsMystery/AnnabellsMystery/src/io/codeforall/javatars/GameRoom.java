package io.codeforall.javatars;

public class GameRoom implements Investigate {
    public GameRoom() {
    }

    @Override
    public void investigate() {
        Graphics.graphicGameRoom();
        Graphics.graphicGameRoomTitle();
    }
}
