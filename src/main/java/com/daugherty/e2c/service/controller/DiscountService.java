package com.daugherty.e2c.service.controller;

import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.DiscountFilter;
import com.daugherty.e2c.domain.Discount;
import com.daugherty.e2c.domain.SubscriptionType;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonDiscount;
import com.daugherty.e2c.service.json.JsonDiscounts;

/**
 * REST resource for Discounts.
 */
@Controller
@RequestMapping("/discounts")
public class DiscountService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    Accessor<Discount> discountAccessor;
    @Inject
    Mutator<Discount> discountMutator;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonDiscount findDiscount(@PathVariable Long id) {
        return new JsonDiscount(discountAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonDiscounts findDiscounts(
            @RequestParam(value = DiscountFilter.DISCOUNT_CODE, required = false) String discountCode,
            @RequestParam(value = DiscountFilter.DISCOUNT_TYPE, required = false) String discountType,
            @RequestParam(value = DiscountFilter.MEMBERSHIP_LEVEL, required = false) Integer membershipLevel,
            @RequestParam(value = DiscountFilter.SUBSCRIPTION_TYPE, required = false) String subscriptionType,
            @RequestParam(value = DiscountFilter.INCLUDE_EXPIRED, required = false, defaultValue = "false") Boolean includeExpired,
            @RequestParam(value = BaseFilter.SORT_BY, required = false, defaultValue = "code") String sortBy,
            @RequestParam(value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale) {

        SubscriptionType subscriptionTypeValue = getSubscriptionTypeValue(subscriptionType);

        DiscountFilter filter = new DiscountFilter(discountCode, discountType, membershipLevel, subscriptionTypeValue,
                includeExpired, sortBy, sortDesc, startItem, count, locale);

        return new JsonDiscounts(discountAccessor.find(filter));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonDiscount createDiscount(@RequestBody JsonDiscount jsonDiscount) {
        return new JsonDiscount(discountMutator.create(jsonDiscount.toDomainObject(null)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonDiscount updateDiscount(@PathVariable Long id, @RequestBody JsonDiscount jsonDiscount) {
        return new JsonDiscount(discountMutator.update(jsonDiscount.toDomainObject(id)));
    }

    private SubscriptionType getSubscriptionTypeValue(String subscriptionType) {
        SubscriptionType subscriptionTypeValue = null;
        try {
            subscriptionTypeValue = SubscriptionType.findByName(subscriptionType);
        } catch (IllegalArgumentException iae) {
            subscriptionTypeValue = null;
        }
        return subscriptionTypeValue;
    }

}
