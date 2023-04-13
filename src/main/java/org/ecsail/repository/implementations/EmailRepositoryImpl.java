package org.ecsail.repository.implementations;

import org.ecsail.dto.EmailDTO;
import org.ecsail.dto.Email_InformationDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.EmailRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class EmailRepositoryImpl implements EmailRepository {

    private final JdbcTemplate template;

    public EmailRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Email_InformationDTO> getEmailInfo() {
        return null;
    }

    @Override
    public List<EmailDTO> getEmail(int p_id) {
        return null;
    }

    @Override
    public EmailDTO getEmail(PersonDTO person) {
        return null;
    }
}
