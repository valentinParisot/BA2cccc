package ch.epfl.tchu.net;

import java.util.List;
import java.util.function.Function;

public interface Serde<Obj> {

    public abstract String serialize(Obj o);

    public abstract Obj deSerialize(String s);


    public static <Obj> Serde of(Function<Obj, String> serialize, Function<String, Obj> deSerialize) {
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


    public static <Obj> Serde oneOf(List<Obj> list) {
        return new Serde<Obj>() {
            @Override
            public String serialize(Obj o) {
                int n = list.indexOf(o);
                return Integer.toString(n);
            }

            @Override
            public Obj deSerialize(String s) { // s sera toujours valide? (il faut un int)
                int n = Integer.parseInt(s);
                return list.get(n);
            }
        };
    }


    // généricité <List<Obj>> impossible
    public static <Obj> Serde listOf(Serde<Obj> serde, String separation) {
        return new Serde<Obj>() {
            @Override
            public String serialize(Obj o) {
                return null;
            }

            @Override
            public Obj deSerialize(String s) {
                return null;
            }
        };
    }

    public static <Obj> Serde bagOf() {
        return new Serde<Obj>() {
            @Override
            public String serialize(Obj o) {
                return null;
            }

            @Override
            public Obj deSerialize(String s) {
                return null;
            }
        };
    }
}
