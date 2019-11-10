package bgu.spl.net.api;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.net.protocol.messages.*;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class MessageEncoderDecoderImp<T> implements MessageEncoderDecoder {

    private byte[] buffer = new byte[1024];
    private int len = 0;
    private byte lastByte = -1;
    private int counter =0;
    private int msg;
    private int zeroCount=0;
    private  int num =-1;
    private List<String> toRet = new LinkedList<>();

    public absMessage decodeNextByte(byte nextByte) {
        if (counter == 0){
            counter ++;
            toRet.clear();
            return null;
        }
        if (counter == 1){
            counter ++;
            msg = nextByte;
          //  return null;
        }
        if (msg ==1){
            if (counter ==2){
                counter++;
                return null;
            }
            if (nextByte ==0) {
                toRet.add(popString());
                zeroCount++;
            }
            else{
                pushByte(nextByte);
            }
            if (zeroCount ==2){
                zeroCount = 0;
               counter=0;
               msg=0;
               String ret ="";
               for (String s : toRet)
                   ret = ret + s + " ";
               return new registerMsg(ret);
            }
            else
                return null;
        }
        if (msg == 2){
            if (counter ==2){
                counter++;
                return null;
            }
            if (nextByte ==0) {
                toRet.add(popString());
                zeroCount++;
            }
            else{
                pushByte(nextByte);
            }
            if (zeroCount ==2){
                zeroCount = 0;
                counter=0;
                msg=0;
                String ret ="";
                for (String s : toRet)
                    ret = ret + s + " ";
                return new loginMsg(ret);
            }
            else
                return null;
        }
        if (msg ==3){
            counter = 0;
            msg = 0;
            return new logoutMsg("");
        }
        if (msg ==4){
            if (counter == 2) {
                counter ++;
                return null;
            }
            if (counter == 3){
                int i = nextByte;
                String s = i + "";
                toRet.add(s);
                counter++;
            }
            else if (counter == 4){
                num = nextByte;
                String s = num+ "";
                toRet.add(s);
                counter++;
            }
            else{
                if (nextByte ==0) {
                    toRet.add(popString());
                    zeroCount++;
                }
                else{
                    pushByte(nextByte);
                }
                if (zeroCount ==num){
                    zeroCount = 0;
                    counter=0;
                    msg=0;
                    String ret ="";
                    for (String s : toRet)
                        ret = ret + s + " ";
                    return new followMsg(ret);
                }
            }
        }
        if (msg ==5){
            if (counter ==2){
                counter++;
                return null;
            }
           if (nextByte == 0){
               counter =0;
               msg =0;
              return new postMsg(popString());
           }
           else{
               pushByte(nextByte);
           }
        }
        if (msg==6){
            if (counter ==2){
                counter++;
                return null;
            }
            if (nextByte ==0) {
                toRet.add(popString());
                zeroCount++;
            }
            else{
                pushByte(nextByte);
            }
            if (zeroCount ==2){
                zeroCount = 0;
                counter=0;
                msg=0;
                String ret ="";
                for (String s : toRet)
                    ret = ret + s + " ";
                return new pmMsg(ret);
            }
            else
                return null;
        }
        if (msg==7){
            counter = 0;
            msg = 0;
            return new userListMsg("");
        }
        if (msg==8){
            if (counter ==2){
            counter++;
            return null;
        }
            if (nextByte == 0){
                counter =0;
                msg =0;
                return new statMsg(popString());
            }
            else{
                pushByte(nextByte);
            }
        }
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= buffer.length)
            buffer= Arrays.copyOf(buffer, len * 2);
        buffer[len++] = nextByte;
    }
    private String popString() { //This method does the actual decoding
        String result = new String(buffer, 0, len, StandardCharsets.UTF_8); //Decoding
        len = 0;
        return result;
    }
    @Override
    public byte[] encode(Object message) {
        String msg = ((absMessage)message).getMsg();
        String[] splitedMsg = msg.split(" ");
        switch (splitedMsg[0])
        {
            case ("ACK"):
            {
                byte[] ack = shortToBytes((short)10);
                switch (splitedMsg[1])
                {
                    case ("1"):{
                        byte[] register = shortToBytes((short)1);
                        return addArray(ack, register);
                    }
                    case ("2"):{
                        byte[] login = shortToBytes((short)2);
                        return addArray(ack, login);
                    }
                    case ("3"):{
                        byte[] logout = shortToBytes((short)3);
                        return addArray(ack, logout);
                    }
                    case ("4"):
                        {
                            byte[] follow = shortToBytes((short)4);
                            byte[] arr1 = addArray(ack, follow);
                            byte[] numUsers = splitedMsg[2].getBytes();
                            byte[] arr2 = addArray(arr1, numUsers);
                       //     byte[] zero31 = shortToBytes((short)0);
                        //    byte[] arr2 = addArray(arr3, zero31);
                          for (int i =3; i<Integer.parseInt(splitedMsg[2]) +3; i++)
                          {
                              byte[] name = splitedMsg[i].getBytes();
                              byte[] zero ={0};
                              byte[] nameZero = addArray(name, zero);
                              arr2 = addArray(arr2, nameZero);
                          }
                          return arr2;
                        }
                    case ("5"):{
                        byte[] logout = shortToBytes((short)5);
                        return addArray(ack, logout);
                    }
                    case ("6"):{
                        byte[] logout = shortToBytes((short)6);
                        return addArray(ack, logout);
                    }
                    case ("7"):
                    {
                        byte[] userList = shortToBytes((short)7);
                        byte[] arr1 = addArray(ack, userList);
                        byte[] numUsers = splitedMsg[2].getBytes();
                        byte[] arr2 = addArray(arr1, numUsers);
                        for (int i =3; i<Integer.parseInt(splitedMsg[2]) +3; i++)
                        {
                            byte[] name = splitedMsg[i].getBytes();
                            byte[] zero ={0};
                            byte[] nameZero = addArray(name, zero);
                            arr2 = addArray(arr2, nameZero);
                        }
                        return arr2;
                    }
                    case ("8"):
                    {
                        byte[] stat = shortToBytes((short)8);
                        byte[] arr1 = addArray(ack, stat);
                        byte[] s2 =  splitedMsg[2].getBytes();
                        byte[] arr2 = addArray(arr1, s2);
                        byte[] s3 =  splitedMsg[3].getBytes();
                        byte[] arr3 = addArray(arr2, s3);
                        byte[] s4 =  splitedMsg[4].getBytes();
                        byte[] arr4 = addArray(arr3, s4);
                        return arr4;
                    }
                }
            }
            case ("NOTIFICATION"):
            {
                byte[] arr1 = shortToBytes((short)9);
                byte[] arr2 = splitedMsg[1].getBytes();
                byte[] add12 = addArray(arr1, arr2);
                byte[] arr3 = splitedMsg[2].getBytes();
                byte[] add123 = addArray(add12, arr3);
                byte[] zero = {0};
                byte[] add123zero = addArray(add123, zero);
                byte[] arr4 = splitedMsg[3].getBytes();
                byte[] add1234 = addArray(add123zero, arr4);
                byte[] zero2 = {0};
                byte[] ret = addArray(add1234, zero2);
                for (int i = 4; i<splitedMsg.length; i++){
                    byte[] tmp = splitedMsg[i].getBytes();
                    byte[] tmp2 = addArray(tmp, zero);
                    ret= addArray(ret, tmp2);
                }
                return addArray(ret, zero);
            }
            case ("Error"):
            {
                byte[] arr1 = shortToBytes((short)11);
                byte[] opCode = splitedMsg[1].getBytes();
                return addArray(arr1, opCode);
            }

        }
       return null;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public byte[] addArray(byte[] arr1, byte[] arr2)
    {
        int n = arr1.length + arr2.length;
        byte[] toRet = new byte[n];
        for (int i=0; i<arr1.length; i++)
        {
            toRet[i] = arr1[i];
        }
        for(int i=arr1.length; i<n; i++)
        {
            toRet[i] = arr2[i-arr1.length];
        }
        return toRet;
    }
}
