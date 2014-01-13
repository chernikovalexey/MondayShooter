package com.twopeople.game.network;

import com.twopeople.game.Entity;
import com.twopeople.game.network.packet.*;

/**
 * Created by podko_000
 * At 17:42 on 13.01.14
 */

public interface Listener {
    public void connectionSuccess(AuthResponse response);

    public void connectionFailed(Error e);

    public Entity[] getState();

    public void runningStart(float x, float y, int id);

    public void runningEnd(float x, float y, int id);

    public void movingDirectionChanged(float x, float y, float vx, float vy, int id);

    public void headingDirectionChanged(float x, float y, float vx, float vy, int id);
    
    public void shut(float x, float y, float vx, float vy, int shooterId);

    public void disconnected(int id);
}