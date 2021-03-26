package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){

    }

    private void sendInfo(String info, Player p1, Player p2){
        p1.receiveInfo(info);
        p2.receiveInfo(info);
    }
    private void informState(PublicGameState newState, Player p1,PlayerState ps1, Player p2, PlayerState ps2){
        p1.updateState(newState, ps1);
        p2.updateState(newState,ps2);
    }
}
