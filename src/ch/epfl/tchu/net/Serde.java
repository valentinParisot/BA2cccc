package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
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
     * @param o object to serialize
     * @return the corresponding String
     */

    String serialize(Obj o);

    //----------------------------------------------------------------------------------------------------

    /**
     * @param s String to deserialize
     * @return the object corresponding
     */

    Obj deSerialize(String s);

    //----------------------------------------------------------------------------------------------------

    /**
     * @param serialize   serializing function
     * @param deSerialize deserializing function
     * @param <Obj>       the type of object corresponding to the serde
     * @return a corresponding serde
     */

    static <Obj> Serde<Obj> of(Function<Obj, String> serialize, Function<String, Obj> deSerialize) {
        return new Serde<>() {
            @Override
            public String serialize(Obj o) {
                if (o == null) {
                    return "";
                }
                return serialize.apply(o);
            }

            @Override
            public Obj deSerialize(String s) {
                if (s.equals("")) {
                    return null;
                }
                return deSerialize.apply(s);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param list  list of <@Obj>
     * @param <Obj> the type of object corresponding to the serde
     * @return a new serde serializing the index of each @Obj in @list
     */

    static <Obj> Serde<Obj> oneOf(List<Obj> list) {
        return new Serde<>() {
            @Override
            public String serialize(Obj o) {
                if (o == null) {
                    return "";
                }
                int n = list.indexOf(o);
                return Integer.toString(n);
            }

            @Override
            public Obj deSerialize(String s) {
                if (s.equals("")) {
                    return null;
                }
                int n = Integer.parseInt(s);
                return list.get(n);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param serde      an existing serde of @Obj
     * @param separation the separation between every @Obj
     * @param <Obj>      the type of object corresponding to @serde
     * @return a new serde which serialize lists of @Obj with @serde and separate them with @separation
     */

    static <Obj> Serde<List<Obj>> listOf(Serde<Obj> serde, String separation) {
        return new Serde<>() {
            @Override
            public String serialize(List<Obj> l) {
                if (l.size() == 0) {
                    return "";
                }
                List<String> list = new ArrayList<>();
                for (Obj o : l) {
                    list.add(serde.serialize(o));
                }
                return String.join(separation, list);
            }

            @Override
            public List<Obj> deSerialize(String s) {
                if (s.equals("")) {
                    return new ArrayList<>();
                }
                String[] list = s.split(Pattern.quote(separation), -1);
                List<Obj> obj = new ArrayList<>();
                for (String string : list) {
                    obj.add(serde.deSerialize(string));
                }
                return obj;
            }
        };
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param serde      same as listOf
     * @param separation same as listOf
     * @param <Obj>      same as listOf
     * @return same as listOf but with a SortedBag instead
     */

    static <Obj extends Comparable<Obj>> Serde<SortedBag<Obj>> bagOf(Serde<Obj> serde, String separation) {
        return new Serde<>() {

            final Serde<List<Obj>> listOf = Serde.listOf(serde, separation);

            @Override
            public String serialize(SortedBag<Obj> sb) {
                if (sb.size() == 0) {
                    return "";
                }
                List<Obj> list = sb.toList();
                return listOf.serialize(list);
            }

            @Override
            public SortedBag<Obj> deSerialize(String s) {
                if (s.isEmpty()) {//ajouter is empty
                    return SortedBag.of();
                }
                List<Obj> list = listOf.deSerialize(s);
                return SortedBag.of(list);
            }
        };
    }

    //----------------------------------------------------------------------------------------------------
}
