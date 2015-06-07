package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.google.common.collect.Lists;

public class JdbcProfileTranslationDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProfileTranslationDao profileTranslationDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("profile-translation.sql");
    }

    @Test
    public void getUpdateIdForLatestSupplierWithTranslation() throws Exception {
        assertThat(profileTranslationDao.getUpdateIdForLatestSupplier(26L), is(999923L));
    }

    @Test
    public void getUpdateIdForLatestSupplierWithoutTranslation() throws Exception {
        assertThat(profileTranslationDao.getUpdateIdForLatestSupplier(999999L), is(nullValue()));
    }

    @Test
    public void loadByLatestSupplierIdForSupplierWithTranslation() {
        ProfileTranslation translation = profileTranslationDao.loadByLatestSupplierId(26L);

        assertThat(translation.getSnapshotId(), is(2502L));
        assertThat(translation.getCompanyDescription(), is("What was, what is, and what is becoming"));
        assertThat(translation.getCompanyDescriptionTranslation(), is("translated description"));
    }

    @Test
    public void loadByLatestSupplierIdForSupplierWithoutTranslation() {
        ProfileTranslation translation = profileTranslationDao.loadByLatestSupplierId(999999L);

        assertThat(translation.getSnapshotId(), is(999123L));
        assertThat(translation.getCompanyDescription(), is("Description 999999"));
        assertThat(translation.getCompanyDescriptionTranslation(), is(nullValue()));
    }

    @Test
    public void insertInsertsTranslationIntoTable() throws Exception {
        ProfileTranslation translation = new ProfileTranslation(999999L, "3Vl8Yw8V", 999123L, "Description 999999",
                "Translation 999999");

        profileTranslationDao.insert(translation);

        Map<String, Object> partyAuditTranslationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_translation WHERE party_audit_id = ?", translation.getSnapshotId());
        assertThat((Long) partyAuditTranslationRowMap.get("language_id"), is(Language.CHINESE.getId()));
        assertThat((Long) partyAuditTranslationRowMap.get("party_audit_id"), is(translation.getSnapshotId()));
        assertThat(partyAuditTranslationRowMap.get("company_description").toString(),
                is(translation.getCompanyDescriptionTranslation()));
    }

    @Test
    public void updateUpdatesExistingTranslationInTable() throws Exception {
        ProfileTranslation translation = new ProfileTranslation(26L, "xo0wK0qL", 2502L, "What was, what is, and what is becoming",
                "New Translation");

        profileTranslationDao.update(999923L, translation);

        Map<String, Object> partyAuditTranslationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_translation WHERE party_audit_id = ?", translation.getSnapshotId());
        assertThat((Long) partyAuditTranslationRowMap.get("language_id"), is(Language.CHINESE.getId()));
        assertThat((Long) partyAuditTranslationRowMap.get("party_audit_id"), is(translation.getSnapshotId()));
        assertThat(partyAuditTranslationRowMap.get("company_description").toString(),
                is(translation.getCompanyDescriptionTranslation()));
    }

}
