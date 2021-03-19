package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.List;
import static ch.epfl.tchu.game.Constants.INITIAL_CAR_COUNT;

/**
 * PublicPlayerState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class PublicPlayerState {

    private final int ticketCount, cardCount;
    private final List<Route> routes;
    private int  carCount;
    private int claimPoints;

    //----------------------------------------------------------------------------------------------------

    /**
     * Builds the state of a player with the number of tickets and cards given,
     * and having seized the given routes
     * @param ticketCount number of tickets
     * @param cardCount number of cards
     * @param routes List of routes
     * @throws IllegalArgumentException if the number of tickets or the number of cards is strictly negative
     */

    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount>=0 && cardCount >=0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = routes;
        this.carCount = carCountCalc();
        this.claimPoints = claimPointsCalc();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return nzmber of ticket
     */

    public int ticketCount(){
        return ticketCount;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return number of card
     */

    public int cardCount(){
        return cardCount;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return List of routes
     */

    public List<Route> routes() {
        return routes;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * compute the number of cars
     * @return int car
     */

    private int carCountCalc(){

        int size = 0;
        for(Route r : routes){
            size += r.length();
        }
        return (INITIAL_CAR_COUNT - size);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * compute the number of claimPoints
     * @return int claimePoints
     */

    private int claimPointsCalc (){

        int points = 0;
        for(Route r : routes){
            points += r.claimPoints();
        }
        return points;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return number of car
     */

    public int carCount(){
        return carCount;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return number of claimPoints
     */

    public int claimPoints (){
        return claimPoints;
    }

    //----------------------------------------------------------------------------------------------------

}
