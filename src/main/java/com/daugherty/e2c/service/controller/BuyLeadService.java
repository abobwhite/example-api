package com.daugherty.e2c.service.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.BuyLeadFilter;
import com.daugherty.e2c.business.accessor.filter.ProductCategoryFilter;
import com.daugherty.e2c.business.mapper.BuyLeadMapper;
import com.daugherty.e2c.domain.BuyLead;
import com.daugherty.e2c.domain.ProductCategory;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonBuyLead;
import com.daugherty.e2c.service.json.JsonBuyLeads;

/**
 * REST resource for Buy Leads.
 */
@Controller
@RequestMapping("/buyLeads")
public class BuyLeadService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private Accessor<BuyLead> buyLeadAccessor;
    @Inject
    private Accessor<ProductCategory> productCategoryAccessor;
    @Inject
    private Mutator<BuyLead> buyLeadMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private BuyLeadMapper buyLeadMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN, Role.BUYER_MODERATOR, Role.BUYER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonBuyLead createBuyLead(@RequestBody JsonBuyLead jsonBuyLead, Locale locale) {
        BuyLead createdBuyLead = buyLeadMutator.create(buyLeadMapper.toNewDomainObject(jsonBuyLead));

        return new JsonBuyLead(createdBuyLead, documentUrlFactory, locale);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonBuyLeads findBuyLeads(
            @RequestParam(value = BuyLeadFilter.EMAIL_PREFIX, required = false) String emailAddress,
            @RequestParam(value = BuyLeadFilter.PRODUCT_CATEGORY, required = false) Long categoryId,
            @RequestParam(value = BuyLeadFilter.PROVINCE, required = false) String province,
            @RequestParam(value = BuyLeadFilter.EFFECTIVE_SINCE, required = false) Long effectiveSince,
            @RequestParam(value = BuyLeadFilter.INCLUDE_EXPIRED, required = false, defaultValue = "false") Boolean includeExpired,
            @RequestParam(value = BaseFilter.SORT_BY, required = false,
                    defaultValue = BuyLead.LAST_MODIFIED_SERIAL_PROPERTY) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "true") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale) {

        BuyLeadFilter filter = new BuyLeadFilter(emailAddress, categoryId == null ? null
                : findCategoryWithChildren(categoryId), province, effectiveSince == null ? null : new Date(
                effectiveSince), includeExpired, sortBy, sortDesc, startItem, count);

        return new JsonBuyLeads(buyLeadAccessor.find(filter), documentUrlFactory, locale);
    }

    private ProductCategory findCategoryWithChildren(Long categoryId) {
        ProductCategoryFilter filter = new ProductCategoryFilter(null, categoryId, null);
        List<ProductCategory> categories = productCategoryAccessor.find(filter);
        return categories.isEmpty() ? null : categories.get(0);
    }
}
