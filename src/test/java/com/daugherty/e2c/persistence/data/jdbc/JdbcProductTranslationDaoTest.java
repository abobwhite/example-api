package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import com.daugherty.e2c.domain.Language;
import com.daugherty.e2c.domain.ProductTranslation;
import com.google.common.collect.Lists;

public class JdbcProductTranslationDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcProductTranslationDao productTranslationDao;

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList("product-translation.sql");
    }

    @Test
    public void getUpdateIdForLatestProductWithTranslation() throws Exception {
        assertThat(productTranslationDao.getUpdateIdForLatestProduct(9991L), is(9999992L));
    }

    @Test
    public void getUpdateIdForLatestProductWithoutTranslation() throws Exception {
        assertThat(productTranslationDao.getUpdateIdForLatestProduct(888L), is(nullValue()));
    }

    @Test
    public void loadByLatestProductIdForProductWithTranslation() {
        ProductTranslation translation = productTranslationDao.load(9991L);

        assertThat(translation.getId(), is(9991L));
        assertThat(translation.getCountry(), is("England"));
        assertThat(translation.getFobPort(), is("freight on board"));
        assertThat(translation.getFobPrice(), is("203.42"));
        assertThat(translation.getKeySpecification(), is("key specification"));
        assertThat(translation.getLeadTime(), is("lead time"));
        assertThat(translation.getMinimumOrder(), is("10000"));
        assertThat(translation.getModelNumber(), is("model number"));
        assertThat(translation.getPaymentTerms(), is("payment term"));
        assertThat(translation.getProductName(), is("product name"));
        assertThat(translation.getProductDescription(), is("product description"));
        assertThat(translation.getKeyWords(), is("these are key words"));
        assertThat(translation.getMetaTags(), is("these are meta tags"));
        assertThat(translation.getProductNameTranslation(), is("latest translated product name"));
        assertThat(translation.getProductDescriptionTranslation(), is("latest translated product description"));
        assertThat(translation.getPaymentTermsTranslation(), is("latest translated payment term"));
        assertThat(translation.getKeySpecificationTranslation(), is("latest translated key specification"));
        assertThat(translation.getKeyWordsTranslation(), is("latest key words translations"));
        assertThat(translation.getMetaTagsTranslation(), is("latest meta tags translations"));
    }

    @Test
    public void loadByLatestProductIdForProductWithoutTranslation() {
        ProductTranslation translation = productTranslationDao.load(888L);

        assertThat(translation.getId(), is(888L));
        assertThat(translation.getCountry(), is("England"));
        assertThat(translation.getFobPort(), is("freight on board"));
        assertThat(translation.getFobPrice(), is("203.42"));
        assertThat(translation.getKeySpecification(), is("a key specification"));
        assertThat(translation.getLeadTime(), is("lead time"));
        assertThat(translation.getMinimumOrder(), is("10000"));
        assertThat(translation.getModelNumber(), is("model number 23"));
        assertThat(translation.getPaymentTerms(), is("payment term"));
        assertThat(translation.getProductName(), is("Cool Product"));
        assertThat(translation.getProductDescription(), is("Cool Product Description"));
        assertThat(translation.getKeyWords(), is("here's some key words"));
        assertThat(translation.getMetaTags(), is("Here's some meta tags"));
        assertThat(translation.getProductNameTranslation(), is(nullValue()));
        assertThat(translation.getProductDescriptionTranslation(), is(nullValue()));
        assertThat(translation.getPaymentTermsTranslation(), is(nullValue()));
        assertThat(translation.getKeySpecificationTranslation(), is(nullValue()));
        assertThat(translation.getKeyWordsTranslation(), is(nullValue()));
        assertThat(translation.getMetaTagsTranslation(), is(nullValue()));
    }

    @Test
    public void findBySnapshotIdsThatExistInLanguageReturnsJustTranslatedFields() throws Exception {
        Map<Long, ProductTranslation> translationsBySnapshotIds = productTranslationDao.findBySnapshotIds(
                Lists.newArrayList(999124L), Locale.CHINESE);

        assertThat(translationsBySnapshotIds, is(notNullValue()));
        assertThat(translationsBySnapshotIds.size(), is(1));
        ProductTranslation translation = translationsBySnapshotIds.get(999124L);
        assertThat(translation.getProductNameTranslation(), is("latest translated product name"));
        assertThat(translation.getProductDescriptionTranslation(), is("latest translated product description"));
        assertThat(translation.getPaymentTermsTranslation(), is("latest translated payment term"));
        assertThat(translation.getKeySpecificationTranslation(), is("latest translated key specification"));
        assertThat(translation.getKeyWordsTranslation(), is("latest key words translations"));
        assertThat(translation.getMetaTagsTranslation(), is("latest meta tags translations"));
    }

    @Test
    public void findBySnapshotIdsThatDoNotExistInLanguageReturnsEmptyMap() throws Exception {
        Map<Long, ProductTranslation> translationsBySnapshotIds = productTranslationDao.findBySnapshotIds(
                Lists.newArrayList(999124L), Locale.ENGLISH);

        assertThat(translationsBySnapshotIds, is(notNullValue()));
        assertThat(translationsBySnapshotIds.size(), is(0));
    }

    @Test
    public void findBySnapshotIdsThatDoNotExistInAnyLanguageReturnsEmptyMap() throws Exception {
        Map<Long, ProductTranslation> translationsBySnapshotIds = productTranslationDao.findBySnapshotIds(
                Lists.newArrayList(888123L), Locale.CHINESE);

        assertThat(translationsBySnapshotIds, is(notNullValue()));
        assertThat(translationsBySnapshotIds.size(), is(0));
    }

    @Test
    public void findBySnapshotIdsThatAreEmptyReturnsEmptyMap() throws Exception {
        Map<Long, ProductTranslation> translationsBySnapshotIds = productTranslationDao.findBySnapshotIds(
                Lists.<Long> newArrayList(), Locale.CHINESE);

        assertThat(translationsBySnapshotIds, is(notNullValue()));
        assertThat(translationsBySnapshotIds.size(), is(0));
    }

    @Test
    public void insertInsertsNewRecordInTable() {
        ProductTranslation translation = new ProductTranslation(888L, 888123L, "chinesePayment", "translated name",
                "translated description", "translated key spec.", "translated words", "translated tags");

        productTranslationDao.insert(translation);

        Map<String, Object> translationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_translation WHERE product_audit_id = ?", translation.getSnapshotId());
        assertThat((Long) translationRowMap.get("language_id"), is(Language.CHINESE.getId()));
        assertThat((Long) translationRowMap.get("product_audit_id"), is(translation.getSnapshotId()));
        assertThat(translationRowMap.get("translated_key_specification").toString(),
                is(translation.getKeySpecificationTranslation()));
        assertThat(translationRowMap.get("translated_payment_term").toString(),
                is(translation.getPaymentTermsTranslation()));
        assertThat(translationRowMap.get("translated_product_name").toString(),
                is(translation.getProductNameTranslation()));
        assertThat(translationRowMap.get("translated_product_description").toString(),
                is(translation.getProductDescriptionTranslation()));
        assertThat(translationRowMap.get("translated_keyword_list").toString(),
                is(translation.getKeyWordsTranslation()));
        assertThat(translationRowMap.get("translated_meta_tag_list").toString(),
                is(translation.getMetaTagsTranslation()));
    }

    @Test
    public void updateUpdatesExistingRecordInTable() {
        ProductTranslation translation = new ProductTranslation(9991L, 999124L, "updated payment", "updated name",
                "updated description", "updated key specifications", "updated keywords", "updated meta tags");

        productTranslationDao.update(9999992L, translation);

        Map<String, Object> translationRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM product_audit_translation WHERE product_audit_id = ?", translation.getSnapshotId());
        assertThat((Long) translationRowMap.get("language_id"), is(Language.CHINESE.getId()));
        assertThat((Long) translationRowMap.get("product_audit_id"), is(translation.getSnapshotId()));
        assertThat(translationRowMap.get("translated_key_specification").toString(),
                is(translation.getKeySpecificationTranslation()));
        assertThat(translationRowMap.get("translated_payment_term").toString(),
                is(translation.getPaymentTermsTranslation()));
        assertThat(translationRowMap.get("translated_product_name").toString(),
                is(translation.getProductNameTranslation()));
        assertThat(translationRowMap.get("translated_product_description").toString(),
                is(translation.getProductDescriptionTranslation()));
        assertThat(translationRowMap.get("translated_keyword_list").toString(),
                is(translation.getKeyWordsTranslation()));
        assertThat(translationRowMap.get("translated_meta_tag_list").toString(),
                is(translation.getMetaTagsTranslation()));
    }

}
