package org.ecsail.interfaces;

import org.ecsail.pojo.Login;

import java.util.ArrayList;

@FunctionalInterface
public interface LoginDTOListSupplier {
    ArrayList<Login> getLoginDTOs();
}
