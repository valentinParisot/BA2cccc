package ch.epfl.tchu;

/**
 * Preconditions
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Preconditions {

    //----------------------------------------------------------------------------------------------------

    private Preconditions() {}

    //----------------------------------------------------------------------------------------------------

    /**
     * throw IllegalArguementException if the condition is not respected
     * @param shouldBeTrue condition wich is the condition if it's false the exception is throw
     */

    public static void checkArgument(boolean shouldBeTrue){
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }

    //----------------------------------------------------------------------------------------------------
}

