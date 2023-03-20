package org.ecsail.widgetfx;

import org.ecsail.dto.LoginDTO;

public class ObjectFx {

    public static LoginDTO createLoginDTO() {
        return  new LoginDTO(3306,3306, 22, "", "",
                "", "",
                "", System.getProperty("user.home") + "/.ssh/known_hosts",
                System.getProperty("user.home") + "/.ssh/id_rsa", false, true);
    }

    public static LoginDTO createLoginDTO2() {
        return  new LoginDTO(3306,3306, 22, "hostname1", "user1",
                "pass12", "sshuser1",
                "pass1", System.getProperty("user.home") + "/.ssh/known_hosts",
                System.getProperty("user.home") + "/.ssh/id_rsa", false, true);
    }

    public static LoginDTO createLoginDTO3() {
        return  new LoginDTO(3306,3306, 22, "hostname2", "user2",
                "pass234", "sshuser2",
                "pass2", System.getProperty("user.home") + "/.ssh/known_hosts",
                System.getProperty("user.home") + "/.ssh/id_rsa", true, true);
    }

    public static LoginDTO createLoginDTO4() {
        return  new LoginDTO(3306,3306, 22, "hostname3", "user3",
                "pass2345", "sshuser3",
                "pass3", System.getProperty("user.home") + "/.ssh/known_hosts",
                System.getProperty("user.home") + "/.ssh/id_rsa", true, true);
    }
}
