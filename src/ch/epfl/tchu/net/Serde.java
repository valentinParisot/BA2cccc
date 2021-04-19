package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Serde
 * interface
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public interface Serde<Obj> {

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param o
     * @return
     */

    public abstract String serialize(Obj o);

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param s
     * @return
     */

    public abstract Obj deSerialize(String s);

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param serialize
     * @param deSerialize
     * @param <Obj>
     * @return
     */

    public static <Obj> Serde<Obj> of(Function<Obj, String> serialize, Function<String, Obj> deSerialize) {
        return new Serde<Obj>() {
            @Override
            public String serialize(Obj o){
                return serialize.apply(o);
            }

            @Override
            public Obj deSerialize(String s) {
                return deSerialize.apply(s);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param list
     * @param <Obj>
     * @return
     */

    public static <Obj> Serde<Obj> oneOf(List<Obj> list) {
        return new Serde<Obj>() {
            @Override
            public String serialize(Obj o) {
                int n = list.indexOf(o);
                return Integer.toString(n);
            }

            @Override
            public Obj deSerialize(String s) {
                int n = Integer.parseInt(s);
                return list.get(n);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param serde
     * @param separation
     * @param <Obj>
     * @return
     */

    public static <Obj> Serde<List<Obj>> listOf(Serde<Obj> serde, String separation) {
        return new Serde<List<Obj>>() {
            @Override
            public String serialize(List<Obj> l) {
                List<String> list = new ArrayList<>();
                for(Obj o : l){
                    list.add(serde.serialize(o));
                }
                return String.join(separation, list);
            }

            @Override
            public List<Obj> deSerialize(String s) {
                List<String> list = Arrays.asList(s.split(Pattern.quote(separation), -1));
                List<Obj> objs = new ArrayList<>();
                for(String string : list){
                    objs.add(serde.deSerialize(string));
                }
                return objs;
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param serde
     * @param separation
     * @param <Obj>
     * @return
     */

    public static <Obj extends Comparable<Obj>> Serde<SortedBag<Obj>> bagOf(Serde<Obj> serde, String separation) {
        return new Serde<SortedBag<Obj>>() {

            Serde<List<Obj>> listOf = Serde.listOf(serde, separation);

            @Override
            public String serialize(SortedBag<Obj> sb) {
                List<Obj> list = sb.toList();
                return listOf.serialize(list);
            }

            @Override
            public SortedBag<Obj> deSerialize(String s) {
                List<Obj> list = listOf.deSerialize(s);
                return SortedBag.of(list);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------
}
