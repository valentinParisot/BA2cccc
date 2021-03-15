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

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private int carCount;
    private int claimPoints;

    //----------------------------------------------------------------------------------------------------

    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount>=0 && cardCount >=0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = routes;
        this.carCount = carCountCalc();
        this.claimPoints = claimPointsCalc();
    }

    //----------------------------------------------------------------------------------------------------

    public int ticketCount(){
        return ticketCount;
    }

    //----------------------------------------------------------------------------------------------------

    public int cardCount(){
        return cardCount;
    }

    //----------------------------------------------------------------------------------------------------

    public List<Route> routes() {
        return routes;
    }

    //----------------------------------------------------------------------------------------------------

    public int carCountCalc(){

        int size = 0;
        for(Route r : routes){
            size += r.length();
        }
        return (INITIAL_CAR_COUNT - size);
    }

    //----------------------------------------------------------------------------------------------------

    public int claimPointsCalc (){

        int points = 0;
        for(Route r : routes){
            points += r.claimPoints();
        }
        return points;
    }

    //----------------------------------------------------------------------------------------------------

    public int carCount(){
        return carCount;
    }

    //----------------------------------------------------------------------------------------------------

    public int claimPoints (){
        return claimPoints;
    }

    //----------------------------------------------------------------------------------------------------

}
