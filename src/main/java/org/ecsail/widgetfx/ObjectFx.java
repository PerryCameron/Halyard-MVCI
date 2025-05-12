package org.ecsail.widgetfx;

import org.ecsail.dto.LoginDTO;

public class ObjectFx {

    public static LoginDTO createLoginDTO() {
        // id, port, host, user, password, is Default
        return  new LoginDTO(0,8080, "host", "usr", "pass", true);
    }
}
