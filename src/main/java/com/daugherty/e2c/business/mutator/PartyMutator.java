package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.domain.User;
import com.daugherty.e2c.persistence.data.CertificationWriteDao;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;
import com.daugherty.e2c.persistence.data.InteractionWriteDao;
import com.daugherty.e2c.persistence.data.MembershipWriteDao;
import com.daugherty.e2c.persistence.data.PendingUserWriteDao;
import com.daugherty.e2c.persistence.data.ProductWriteDao;
import com.daugherty.e2c.persistence.data.UserReadDao;
import com.daugherty.e2c.persistence.data.UserWriteDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcMessageDao;
import com.daugherty.e2c.persistence.data.jdbc.JdbcPartyDao;

@Service("partyMutator")
@Transactional
public class PartyMutator extends BaseMutator<Party> {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Inject
    private InteractionWriteDao interactionWriteDao;
    @Inject
    private JdbcMessageDao messageDao;
    @Inject
    private MembershipWriteDao membershipWriteDao;
    @Inject
    private InquiryWriteDao inquiryWriteDao;
    @Inject
    private ProductWriteDao productWriteDao;
    @Inject
    private JdbcPartyDao partyDao;
    @Inject
    private UserReadDao userReadDao;
    @Inject
    private UserWriteDao userWriteDao;
    @Inject
    private PendingUserWriteDao pendingUserWriteDao;
    @Inject
    private CertificationWriteDao certificationWriteDao;

    @Override
    public Party create(Party entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Party update(Party entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long partyId) {

        User user = null;

        try {
            user = userReadDao.findByPartyId(partyId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("User must of been a Pending User");
        }

        interactionWriteDao.deleteInteractionsByPartyId(partyId);
        messageDao.deleteMessagesByPartyId(partyId);
        membershipWriteDao.deleteMembershipsByPartyId(partyId);
        inquiryWriteDao.clearBasket(partyId);
        inquiryWriteDao.deletePendingInquiriesByPartyId(partyId);
        certificationWriteDao.deleteByPartyId(partyId);
        productWriteDao.deleteProductsByPartyId(partyId);
        pendingUserWriteDao.deleteByPartyId(partyId);
        partyDao.deletePartyByPartyId(partyId);

        if (user != null) {
            userWriteDao.deleteUser(user.getId());
        }

    }
}
