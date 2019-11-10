package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.Client;
import bgu.spl.net.protocol.DataBase;

import java.util.LinkedList;
import java.util.List;

public class postMsg extends absMessage {
    public postMsg(String msg) {
        super(msg);
        this.opCode =5;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (!dataBase.getClient(connectionId).isConnected()){
            errorMsg notConn = new errorMsg("Error 5");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        List<Client> toSend = new LinkedList<>();
        toSend.addAll(dataBase.getClient(connectionId).getFollowMe());
        String[] splitedMsg = msg.split(" ");
        for (int i=0 ; i<splitedMsg.length; i++)
        {
            String tag= splitedMsg[i].substring(0,1);
            if (tag.equals("@"))
            {
                if (!toSend.contains(dataBase.getClient(splitedMsg[i].substring(1))))
                toSend.add(dataBase.getClient(splitedMsg[i].substring(1)));
            }
        }
        for (Client c : toSend)
        {
            notificationMsg noti = new notificationMsg("NOTIFICATION "+ "1 " + dataBase.getClient(connectionId).getName() + " " + msg);
            if (c.isConnected()){
                noti.procces(c.getId(), connection, dataBase);
            }
            else{
                c.addWaitingMsg(noti);
            }
        }
        dataBase.getClient(connectionId).addMsg(this);
        dataBase.addToDataMsg(this);
        ackMsg ack = new ackMsg("ACK 5");
        ack.procces(connectionId, connection, dataBase);
    }
}
