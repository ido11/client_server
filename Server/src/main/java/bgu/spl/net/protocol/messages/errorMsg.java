package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;
import bgu.spl.net.api.*;

public class errorMsg extends absMessage {

    public errorMsg(String msg) {
        super(msg);
        this.opCode =11;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        connection.send(connectionId, this);
    }

}
