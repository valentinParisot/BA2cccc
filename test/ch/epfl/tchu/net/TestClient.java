package ch.epfl.tchu.net;

import java.io.IOException;

public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(), "localhost", 5100);
        playerClient.run();
        System.out.println("Client done!");
    }
}