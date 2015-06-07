package com.daugherty.e2c.business.accessor;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.daugherty.e2c.business.Filter;
import com.daugherty.e2c.business.MessageCollectionHydrator;
import com.daugherty.e2c.domain.Message;
import com.daugherty.e2c.domain.ProductMessage;
import com.daugherty.e2c.persistence.data.ProductMessageReadDao;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Accessor for Product Message domain objects.
 */
@Service("productMessageAccessor")
public class ProductMessageAccessor extends BaseAccessor<ProductMessage> {

    @Inject
    private MessageCollectionHydrator messageCollectionHydrator;

    static final Function<Message, Long> MESSAGE_ID_FUNCTION = new Function<Message, Long>() {
        @Override
        public Long apply(Message message) {
            return message.getId();
        }
    };

    @Inject
    private ProductMessageReadDao messageReadDao;

    @Override
    public List<ProductMessage> find(Filter<ProductMessage> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductMessage load(Long id, Locale locale) {
        ProductMessage message = messageReadDao.load(id);
        addProductIdsToMessages(Lists.newArrayList(message));

        messageCollectionHydrator.hydrate(message);

        return message;
    }

    private void addProductIdsToMessages(List<ProductMessage> messages) {
        List<Long> messageIds = Lists.newArrayList(Iterables.transform(messages, MESSAGE_ID_FUNCTION));
        Multimap<Long, Long> productIdListsByMessageIds = messageReadDao.findProductIdListsByMessageIds(messageIds);
        for (ProductMessage message : messages) {
            message.addProductIds(productIdListsByMessageIds.get(message.getId()));
        }
    }

}
