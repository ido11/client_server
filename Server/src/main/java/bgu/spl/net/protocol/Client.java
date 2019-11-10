package bgu.spl.net.protocol;

import bgu.spl.net.protocol.messages.absMessage;

import java.util.LinkedList;
import java.util.List;

public class Client {

    private int id;
    private String name;
    private String password;
    private boolean isConnected;
    private List<Client> Ifollow;
    private List<Client> followMe;
    private List<absMessage> postedMsgs;
    private List<absMessage> waitingMsgs;


    public Client (String name, String password, int id)
    {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isConnected = false;
        this.Ifollow = new LinkedList<>();
        this.followMe = new LinkedList<>();
        this.postedMsgs = new LinkedList<>();
        this.waitingMsgs = new LinkedList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void addFollow(Client c)
    {
        Ifollow.add(c);
    }

    public void removeFollow(Client c)
    {
        Ifollow.remove(c);
    }

    public boolean isFollow(String name)
    {
        for(Client c : Ifollow)
        {
            if (c.getName().equals(name))
                return true;
        }
        return false;
    }

    public void addFollowMe(Client c)
    {
        this.followMe.add(c);
    }
    public void removeFollowMe(Client c) {this.followMe.remove(c);}

    public List<Client> getFollowMe() {
        return followMe;
    }

    public void addMsg(absMessage msg){
        this.postedMsgs.add(msg);
    }

    public List<absMessage> getPostedMsgs() {
        return postedMsgs;
    }

    public List<Client> getIfollow() {
        return Ifollow;
    }

    public void addWaitingMsg(absMessage msg){
        waitingMsgs.add(msg);
    }

    public void clearWaitingMsgs(){
        waitingMsgs.clear();
    }

    public List<absMessage> getWaitingMsgs() {
        return waitingMsgs;
    }
}
