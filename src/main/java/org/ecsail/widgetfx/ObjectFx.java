package org.ecsail.widgetfx;

import org.ecsail.dto.LoginDTO;

public class ObjectFx {

    public static LoginDTO createLoginDTO() {
        return  new LoginDTO(3306,3306, 22, "", "",
                "", "",
                "", "ECSC_SQL", System.getProperty("user.home") + "/.ssh/known_hosts",
                System.getProperty("user.home") + "/.ssh/id_rsa", true, true);
    }
}
