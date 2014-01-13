package com.twopeople.game.network;

import com.twopeople.game.network.packet.*;
/**
 * Created by podko_000
 * At 17:42 on 13.01.14
 */

public interface Listener {
    public void connectionSuccess(AuthResponse response);
}