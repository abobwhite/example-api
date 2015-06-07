package com.daugherty.e2c.persistence.data.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.daugherty.e2c.E2CMatchers;
import com.daugherty.e2c.domain.ApprovalStatus;
import com.daugherty.e2c.domain.Contact;
import com.daugherty.e2c.domain.Employee;
import com.daugherty.e2c.domain.PartyType;
import com.daugherty.persistence.QueryCriteria;
import com.google.common.collect.Lists;

public class JdbcEmployeeDaoTest extends BaseJdbcDaoTest {

    @Inject
    private JdbcEmployeeDao employeeDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected List<String> getSqlScriptFiles() {
        return Lists.newArrayList();
    }

    @Test
    public void createInsertsIntoBaseSnapshotAndEventTables() {
        Contact contact = new Contact(null, null, "Tiger", "Woods", null, null, "tiger.woods@test.com", null, null,
                null, "1234567", "123.123.123.123", null, new Date());
        Employee employee = new Employee("username", "password", Lists.newArrayList("role"), contact);

        Employee createdEmployee = employeeDao.insert(employee);
        assertThat(createdEmployee.getId(), notNullValue());
        assertThat(createdEmployee.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit_status WHERE party_id = ?", createdEmployee.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(createdEmployee.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(createdEmployee.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(1));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.APPROVED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyRowMap = jdbcTemplate.queryForMap("SELECT * FROM party WHERE party_id = ?",
                createdEmployee.getId());
        assertThat((Date) partyRowMap.get("registration_date"), is(today()));
        assertThat(partyRowMap.get("ip_address").toString(), is("123.123.123.123"));

        Map<String, Object> partyAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit WHERE party_audit_id = ?", createdEmployee.getSnapshotId());
        assertThat(partyAuditRowMap.get("first_name").toString(), is(employee.getContact().getFirstName()));
        assertThat(partyAuditRowMap.get("last_name").toString(), is(employee.getContact().getLastName()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(employee.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("party_type").toString(), is(PartyType.INTERNAL.toString()));
    }

    @Test
    public void updateInsertsIntoBaseSnapshotAndEventTables() {
        Contact contact = new Contact(null, null, "Zaphod", "Beeblebox Jr.", null, null, "zaphod.beeblebox@test.com",
                null, null, null, "1234567", "127.0.0.1", null, new Date());
        Employee employee = new Employee(77L, "94Pa430l", contact, ApprovalStatus.APPROVED, "zaphodb", false, true,
                null, Lists.newArrayList("role"), 2, null);

        Employee createdEmployee = employeeDao.update(employee);
        assertThat(createdEmployee.getId(), notNullValue());
        assertThat(createdEmployee.getSnapshotId(), notNullValue());

        Map<String, Object> partyAuditStatusRowMap = jdbcTemplate
                .queryForMap(
                        "SELECT * FROM party_audit pa, party_audit_status pas WHERE pa.party_audit_id = pas.party_audit_id and pas.party_id = ? and pas.event_datetime = (select max(event_datetime) from party_audit_status where party_id = ?)",
                        createdEmployee.getId(), createdEmployee.getId());
        assertThat((Long) partyAuditStatusRowMap.get("party_id"), is(createdEmployee.getId()));
        assertThat((Long) partyAuditStatusRowMap.get("party_audit_id"), is(createdEmployee.getSnapshotId()));
        assertThat((Integer) partyAuditStatusRowMap.get("version_number"), is(2));
        assertThat(partyAuditStatusRowMap.get("event_type").toString(), is(ApprovalStatus.APPROVED.getName()));
        assertThat((Date) partyAuditStatusRowMap.get("event_datetime"),
                E2CMatchers.equalToWithinTolerance(new Date(), 1000L));

        Map<String, Object> partyAuditRowMap = jdbcTemplate.queryForMap(
                "SELECT * FROM party_audit WHERE party_audit_id = ?", createdEmployee.getSnapshotId());
        assertThat(partyAuditRowMap.get("first_name").toString(), is(employee.getContact().getFirstName()));
        assertThat(partyAuditRowMap.get("last_name").toString(), is(employee.getContact().getLastName()));
        assertThat(partyAuditRowMap.get("email_address").toString(), is(employee.getContact().getEmailAddress()));
        assertThat(partyAuditRowMap.get("party_type").toString(), is(PartyType.INTERNAL.toString()));
    }

    @Test
    public void getWithEmailAddressCriteria() {

        QueryCriteria queryCriteria = employeeDao.createQueryCriteria(null, "export2chinatester+4@gmail.com",
                "username", false, 1, 250, Locale.ENGLISH);

        List<Employee> employees = employeeDao.find(queryCriteria);

        assertThat(employees.size(), is(1));

        Employee employee = employees.get(0);
        assertSbfast(employee);
    }

    @Test
    public void getWithUsernameCriteria() {

        QueryCriteria queryCriteria = employeeDao.createQueryCriteria("sbfast", null, "username", false, 1, 250,
                Locale.ENGLISH);

        List<Employee> employees = employeeDao.find(queryCriteria);

        assertThat(employees.size(), is(1));

        Employee employee = employees.get(0);
        assertSbfast(employee);
    }

    @Test
    public void loadEmployeeById() {
        Employee employee = employeeDao.load(80L);

        assertSbfast(employee);
    }

    private void assertSbfast(Employee employee) {
        assertThat(employee.getId(), is(80L));
        assertThat(employee.getPublicId(), is("KyVkyZ0r"));
        assertThat(employee.getContact().getFirstName(), is("Slartibart"));
        assertThat(employee.getContact().getLastName(), is("Fast"));
        assertThat(employee.getContact().getEmailAddress(), is("export2chinatester+4@gmail.com"));
        assertTrue(employee.getRoles().contains("ROLE_ADMIN"));
        assertTrue(employee.getRoles().contains("ROLE_TRANSLATOR"));
        assertTrue(employee.getRoles().contains("ROLE_BUYER_MODERATOR"));
        assertTrue(employee.getRoles().contains("ROLE_SUPPLIER_MODERATOR"));
    }

}
