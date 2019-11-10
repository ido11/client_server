package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

public class ackMsg extends absMessage {
    public ackMsg(String msg) {
        super(msg);
        this.opCode =10;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        connection.send(connectionId, this);
    }
}
