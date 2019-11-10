package bgu.spl.net.protocol.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.protocol.DataBase;

import java.util.LinkedList;
import java.util.List;

public class followMsg extends absMessage {
    public followMsg(String msg) {
        super(msg);
        this.opCode = 4;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase)
    {
        if (!dataBase.getClient(connectionId).isConnected()){
            errorMsg notConn = new errorMsg("Error 4");
            notConn.procces(connectionId, connection, dataBase);
            return;
        }
        String[] splitedMsgs = msg.split(" ");
        String follow = splitedMsgs[0];
        String numOfUsers =splitedMsgs[1];
        String[] newMsg = new String[(splitedMsgs.length) - 2];
        for (int i=0; i<newMsg.length; i++){
            newMsg[i] = splitedMsgs[i+2];
        }
        List<String> userSucces = new LinkedList<>();
        if (follow.equals("0")) {
            for (String s : newMsg)
            {
               if (dataBase.contiansClient(s) && !(dataBase.getClient(connectionId).isFollow(s)))
               {
                   userSucces.add(s);
                   dataBase.getClient(connectionId).addFollow(dataBase.getClient(s));
                   dataBase.getClient(s).addFollowMe(dataBase.getClient(connectionId));
               }
            }
        }
        else {
            for (String s : newMsg)
            {
                if (dataBase.contiansClient(s) && (dataBase.getClient(connectionId).isFollow(s)))
                {
                    userSucces.add(s);
                    dataBase.getClient(connectionId).removeFollow(dataBase.getClient(s));
                    dataBase.getClient(s).removeFollowMe(dataBase.getClient(connectionId));
                }
            }
        }
        if (userSucces.size() == 0){
            errorMsg notSucc = new errorMsg("Error 4");
            notSucc.procces(connectionId, connection, dataBase);
            return;
        }
        else{
            String ret ="";
            for(String s : userSucces){
                ret = ret + " " + s;
            }
            ackMsg ack = new ackMsg("ACK 4 " + userSucces.size() + ret );
            ack.procces(connectionId, connection, dataBase);
        }
    }
}
