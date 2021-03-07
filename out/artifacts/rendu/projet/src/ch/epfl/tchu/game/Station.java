package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Station
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */
public final class Station {

    private final int id;
    private final String name;

    //----------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param id  (must be superior of 0)
     * @param name name
     * @throws IllegalArgumentException if id is less than 0
     */

    public Station(int id, String name){

            Preconditions.checkArgument(id>=0);
            this.id = id;
            this.name = name;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the id of this this station
     */

    public int id(){
        return this.id;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the name of this station
     */

    public String name(){
        return this.name;
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public String toString(){
         return name;
    }

    //----------------------------------------------------------------------------------------------------



}
