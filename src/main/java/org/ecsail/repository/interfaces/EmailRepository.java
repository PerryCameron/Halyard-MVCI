package org.ecsail.repository.interfaces;

import org.ecsail.dto.EmailDTO;
import org.ecsail.dto.Email_InformationDTO;
import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface EmailRepository {
    List<Email_InformationDTO> getEmailInfo();
    List<EmailDTO> getEmail(int p_id);
    EmailDTO getEmail(PersonDTOFx person);
    int update(EmailDTO o);
    int insert(EmailDTO emailDTO);
    int delete(EmailDTO o);

    int deleteEmail(int pId);
}
