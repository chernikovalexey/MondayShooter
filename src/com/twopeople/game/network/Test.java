package com.twopeople.game.network;

import com.twopeople.game.Entity;
import com.twopeople.game.network.packet.AuthResponse;

/**
 * Created by podko_000
 * At 21:01 on 13.01.14
 */

public class Test implements Listener {
    public static void main(String[] args) {
        Server server = new Server();
        Client client = new Client(new Test());
        server.setClient(client);
        client.connect("localhost", "WORLDking");


        client = new Client(new Test());
        client.connect("localhost", "FightForFun");
        while (true) {}
    }

    @Override
    public void connectionSuccess(AuthResponse response) {
        System.out.println("Connection success. Id="+response.yourId);
        if(response.state!=null)
            System.out.println("State.length="+response.state.length);
    }

    @Override
    public void connectionFailed(Error e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Entity[] getState() {
        return new Entity[20];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void runningStart(float x, float y, int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void runningEnd(float x, float y, int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void movingDirectionChanged(float x, float y, float vx, float vy, int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void headingDirectionChanged(float x, float y, float vx, float vy, int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void shut(float x, float y, float vx, float vy, int shooterId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void disconnected(int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
