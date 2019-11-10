package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.Client;
import bgu.spl.net.protocol.DataBase;

import java.util.List;

public class userListMsg extends absMessage {
    public userListMsg(String msg) {
        super(msg);
        this.opCode =7;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (!dataBase.getClient(connectionId).isConnected()){
            errorMsg notConn = new errorMsg("Error 7");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        List<Client> Users = dataBase.getClientsList();
        int numOfUsers = Users.size();
        String ret = "ACK 7 " + numOfUsers;
        for (Client c : Users){
            ret = ret + " " + c.getName();
        }
        ackMsg ack = new ackMsg(ret);
        ack.procces(connectionId, connection, dataBase);
    }
}
