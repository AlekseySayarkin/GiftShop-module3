package com.epam.esm.dao.mapper;

import com.epam.esm.dao.util.GiftCertificateExtractorUtil;
import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificateExtractorUtil.extractGiftCertificate(rs);
    }
}
