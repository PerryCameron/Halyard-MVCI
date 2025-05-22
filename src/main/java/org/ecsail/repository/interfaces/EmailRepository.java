package org.ecsail.repository.interfaces;

import org.ecsail.fx.EmailDTOFx;
import org.ecsail.fx.Email_InformationDTO;
import org.ecsail.fx.PersonFx;

import java.util.List;

public interface EmailRepository {
    List<Email_InformationDTO> getEmailInfo();
    List<EmailDTOFx> getEmail(int p_id);
    EmailDTOFx getEmail(PersonFx person);
    int update(EmailDTOFx o);
    int insert(EmailDTOFx emailDTO);
    int delete(EmailDTOFx o);

    int deleteEmail(int pId);
}
