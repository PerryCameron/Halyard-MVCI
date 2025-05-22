package org.ecsail.widgetfx;

import org.ecsail.pojo.Login;

public class ObjectFx {

    public static Login createLoginDTO() {
        // id, port, host, user, password, is Default
        return  new Login(0,8080, "host", "usr", "pass", true);
    }
}
