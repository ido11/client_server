package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

public class notificationMsg extends absMessage {
    public notificationMsg(String msg) {
        super(msg);
        this.opCode =9;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        connection.send(connectionId, this);
    }
}
