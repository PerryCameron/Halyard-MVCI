package org.ecsail.repository.interfaces;

import org.ecsail.dto.EmailDTOFx;
import org.ecsail.dto.Email_InformationDTO;
import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface EmailRepository {
    List<Email_InformationDTO> getEmailInfo();
    List<EmailDTOFx> getEmail(int p_id);
    EmailDTOFx getEmail(PersonDTOFx person);
    int update(EmailDTOFx o);
    int insert(EmailDTOFx emailDTO);
    int delete(EmailDTOFx o);

    int deleteEmail(int pId);
}
