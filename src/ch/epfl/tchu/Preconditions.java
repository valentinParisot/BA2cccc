package ch.epfl.tchu;

/**
 * @author Hugo Jeannin (329220)
 */
public final class Preconditions {

    //----------------------------------------------------------------------------------------------------

    private Preconditions() {}

    //----------------------------------------------------------------------------------------------------

    public static void checkArgument(boolean shouldBeTrue){
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }

    //----------------------------------------------------------------------------------------------------
}

