package bgu.spl.net.protocol;

import java.util.HashMap;
import java.util.LinkedList;
import bgu.spl.net.protocol.messages.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataBase {
    private HashMap<Integer, Client>  clients;
    private LinkedList<Client> clientsList;
    private HashMap<Client, BlockingQueue <absMessage>> waitingMsg;
    private List allMsgs;

    public DataBase ()
    {
        clientsList = new LinkedList<>();
        waitingMsg = new HashMap<>();
        clients = new HashMap<>();
        allMsgs = new LinkedList();
    }

    public LinkedList<Client> getClientsList() {
        return clientsList;
    }

    public boolean contiansClient(String name)
    {
        for(Client client : clientsList)
        {
            if (client.getName().equals(name))
                return true;
        }
        return false;
    }
    public void addClient(Client c, int connectionId)
    {
        this.clientsList.add(c);
        BlockingQueue<absMessage> bq = new LinkedBlockingQueue<>();
        this.waitingMsg.put(c, bq);
        clients.put(connectionId, c);
    }
    public void addMsg(Client c, absMessage msg)
    {
        try {
            waitingMsg.get(c).put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isPassMatch(String name, String pass)
    {
        for(Client client : clientsList)
        {
            if (client.getName().equals(name) && client.getPassword().equals(pass))
                return true;
        }
        return false;
    }

    public Client getClient(String name)
    {
        for(Client client : clientsList)
        {
            if (client.getName().equals(name))
                return client;
        }
        return null;
    }

    public Client getClient(int connectionId)
    {
        return clients.get(connectionId);
    }

    public HashMap<Integer, Client> getClients() {
        return clients;
    }

    public void addToDataMsg(absMessage msg){
        this.allMsgs.add(msg);
    }
}
