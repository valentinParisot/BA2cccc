package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
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
        private List<Integer> integersBuilder;

        public Builder(int stationCount){
            Preconditions.checkArgument(stationCount>=0);
            integersBuilder = new ArrayList<Integer>(stationCount) {};
            for(Integer i : integersBuilder){
                integersBuilder.set(i, i);
            }
        }

        private int representative(int id){
            return integersBuilder.get(id);
        }

        public Builder connect(Station s1, Station s2){
            integersBuilder.set(s2.id(), representative(s1.id()));
            return this;
        }

        public StationPartition build(){
            return new StationPartition(integersBuilder);
        }
    }


}
