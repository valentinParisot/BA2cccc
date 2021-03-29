package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){

        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);
        String name1 = playerNames.get(PlayerId.PLAYER_1);
        String name2 = playerNames.get(PlayerId.PLAYER_2);
        Info i1 = new Info(name1);
        Info i2 = new Info(name2);


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
