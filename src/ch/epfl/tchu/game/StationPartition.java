package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public final class StationPartition implements StationConnectivity{

    private final List<Integer> integers;

    private StationPartition(List<Integer> integers){
        this.integers = integers;
    }

    public boolean connected(Station station1, Station station2){

        if(station1.id() > integers.size() || station2.id() > integers.size()){
            if(station1.id() == station2.id()){ return true; }
        }

        if (integers.get(station1.id()) == integers.get(station2.id())){
            return true;
        }
        return false;
    }

    public final static class Builder{

        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount>=0);

        }
        private int representative(int id){
            return 1;
        }
    }


}
