package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.StationConnectivity;
import ch.epfl.tchu.game.Ticket;
import javafx.util.StringConverter;

public final class extenTicket extends StringConverter<Ticket> {

    private StationConnectivity s;
    public extenTicket(StationConnectivity sd){
        this.s = sd;
    }

    @Override
    public String toString(Ticket ticket){
        return ticket.toString(s);
    }

    @Override
    public Ticket fromString(String string){
        throw new UnsupportedOperationException();
    }


}
