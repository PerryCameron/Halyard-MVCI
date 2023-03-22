package org.ecsail.iface;

import org.ecsail.dto.LoginDTO;

import java.util.ArrayList;

@FunctionalInterface
public interface LoginDTOListSupplier {
    ArrayList<LoginDTO> getLoginDTOs();
}
