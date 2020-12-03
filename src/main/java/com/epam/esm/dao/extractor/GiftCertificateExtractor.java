package com.epam.esm.dao.extractor;

import com.epam.esm.dao.util.GiftCertificateExtractorUtil;
import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException {
        return GiftCertificateExtractorUtil.extractGiftCertificates(rs);
    }
}
