package com.daugherty.e2c.persistence.data.jdbc;

import com.daugherty.e2c.domain.Party;

public class PartyAuditStatusSqlParameterSource extends AuditSqlParameterSource {

    public PartyAuditStatusSqlParameterSource(Party party) {
        super();

        addValue("party_type", party.getPartyType());

        if (party.getContact() != null) {
            addValue("title", party.getContact().getTitle());
            addValue("gender", party.getContact().getGender());
            addValue("first_name", party.getContact().getFirstName());
            addValue("last_name", party.getContact().getLastName());
            addValue("country", party.getContact().getCountry());
            addValue("province", party.getContact().getProvince());
            addValue("email_address", party.getContact().getEmailAddress());
            addValue("skype_id", party.getContact().getSkypeRefId());
            addValue("msn_id", party.getContact().getMsnRefId());
            addValue("icq_id", party.getContact().getIcqRefId());
            addValue("business_phone", party.getContact().getBusinessTelephoneNumber());
        }

        if (party.getCompany() != null) {
            addValue("company_name_english", party.getCompany().getEnglishName());
            addValue("company_name_chinese", party.getCompany().getChineseName());
            addValue("company_description", party.getCompany().getDescription());
            addValue("number_of_employees", party.getCompany().getNumberOfEmployees());
            addValue("company_website", party.getCompany().getWebsite());
            addValue("year_established", party.getCompany().getYearEstablished());
            addValue("total_annual_sales", party.getCompany().getTotalAnnualSales());
            addValue("total_imports_us_dollars", party.getCompany().getTotalImportAmount());
            addValue("company_logo_link", party.getCompany().getLogoRefId());
            addValue("video_link", party.getCompany().getVideoRefId());
        }
    }
}
