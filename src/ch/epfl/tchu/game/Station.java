package ch.epfl.tchu.game;

public  final class Station {

    private final int id;
    private final String name;

    Station(int id, String name){
        if(id < 0) {
            throw new IllegalArgumentException("illegal capacity: " + id + " (must be >= 0)");
            }else {
            this.id = id;
            this.name = name;

        }
    }

    public int id(){
        return this.id;
    }

    public String name(){
        return this.name;
    }

    @Override
    public String toString(){
         return name;
    }



}
