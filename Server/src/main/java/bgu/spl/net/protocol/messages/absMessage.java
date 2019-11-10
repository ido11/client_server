package bgu.spl.net.protocol.messages;

        import bgu.spl.net.api.bidi.Connections;
        import bgu.spl.net.protocol.DataBase;

public abstract class absMessage {

    protected String msg;
    protected DataBase dataBase;
    protected int opCode;

    public absMessage (String msg)
    {
        this.msg = msg;
    }

    public void procces(int connectionId, Connections connection, DataBase dataBase){}

    public String getMsg() {
        return msg;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public int getOpCode() {
        return opCode;
    }
}

