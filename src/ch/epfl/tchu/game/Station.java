package ch.epfl.tchu.game;

public  final class Station {

    public int id;
    public String name;

    Station(int id, String name){

        try {
            if(id < 0) {
                throw new IllegalArgumentException();
            }
            else {
                this.id = id;
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.name = name;
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
