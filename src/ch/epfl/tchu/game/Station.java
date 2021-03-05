package ch.epfl.tchu.game;

/**
 *
 * @author valentin Parisot (3266589
 */
public  final class Station {

    private final int id;
    private final String name;

    //----------------------------------------------------------------------------------------------------

    /**
     *
     *
     * @param id  (must be superior of 0)
     * @param name
     * @throws IllegalArgumentException if id is less than 0
     */

    public Station(int id, String name){
        if(id < 0) {
            throw new IllegalArgumentException("illegal capacity: " + id + " (must be >= 0)");
            }else {
            this.id = id;
            this.name = name;

        }
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
