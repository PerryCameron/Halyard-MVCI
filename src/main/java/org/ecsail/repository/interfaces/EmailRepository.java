package org.ecsail.repository.interfaces;

import org.ecsail.fx.EmailFx;
import org.ecsail.fx.Email_InformationDTO;
import org.ecsail.fx.PersonFx;

import java.util.List;

public interface EmailRepository {
    List<Email_InformationDTO> getEmailInfo();
    List<EmailFx> getEmail(int p_id);
    EmailFx getEmail(PersonFx person);
    int update(EmailFx o);
    int insert(EmailFx emailDTO);
    int delete(EmailFx o);

    int deleteEmail(int pId);
}
