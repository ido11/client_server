package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

public class logoutMsg extends absMessage {
    public logoutMsg(String msg) {
        super(msg);
        this.opCode =3;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (dataBase.getClient(connectionId).isConnected()) {
            dataBase.getClient(connectionId).setConnected(false);
            ackMsg ack = new ackMsg("ACK 3");
            ack.procces(connectionId, connection, dataBase);
            connection.disconnect(connectionId);
        }
        else {
            errorMsg notLogged = new errorMsg("Error 3");
            notLogged.procces(connectionId, connection, dataBase);
        }
    }
}
