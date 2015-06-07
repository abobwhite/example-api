package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

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
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.InquiryProductStager;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.InquiryFilter;
import com.daugherty.e2c.business.mapper.InquiryMapper;
import com.daugherty.e2c.domain.Inquiry;
import com.daugherty.e2c.domain.SecurityUtils;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonInquiries;
import com.daugherty.e2c.service.json.JsonInquiry;
import com.daugherty.e2c.service.json.JsonProductIds;

/**
 * REST resource for Inquiries.
 */
@Controller
public class InquiryService {

    @Inject
    private Accessor<Inquiry> inquiryAccessor;
    @Inject
    private InquiryProductStager productStager;
    @Inject
    private Mutator<Inquiry> inquiryMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;
    @Inject
    private InquiryMapper inquiryMapper;

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER })
    @ResponseBody
    public JsonInquiry retrieveInquiryBasket(Locale locale) {
        InquiryFilter filter = new InquiryFilter(SecurityUtils.getAuthenticatedUserPartyId(), true, null, null, null,
                false, 1, 1);
        List<Inquiry> inquiries = inquiryAccessor.find(filter); // Should only be one
        return new JsonInquiry(inquiries.get(0), documentUrlFactory, locale);
    }

    @RequestMapping(value = "/inquiry/products", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public void stageProducts(@RequestBody JsonProductIds jsonProductIds, @RequestParam(
            value = InquiryFilter.SENDER_ID, required = true) String senderId) {
        productStager.stage(senderId, jsonProductIds);
    }

    @RequestMapping(value = "/inquiry/products/{id}", method = RequestMethod.DELETE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public void unstageProduct(@PathVariable Long id,
            @RequestParam(value = InquiryFilter.SENDER_ID, required = true) String senderId) {
        productStager.unstage(senderId, id);
    }

    @RequestMapping(value = "/inquiry/submissions", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.BUYER, Role.SUPPLIER, Role.Spring.IS_AUTHENTICATED_ANONYMOUSLY })
    @ResponseBody
    public JsonInquiry submitInquiryBasket(@RequestBody JsonInquiry jsonInquiry, Locale locale) {
        return new JsonInquiry(inquiryMutator.create(inquiryMapper.toNewDomainObject(jsonInquiry, locale)),
                documentUrlFactory, locale);
    }

    @RequestMapping(value = "/inquiries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonInquiries retrieveInquiriesNeedingApproval(@RequestParam(value = InquiryFilter.SENDER_ID,
            required = false) Long senderId,
            @RequestParam(value = InquiryFilter.DISAPPROVED, required = false) Boolean disapproved, @RequestParam(
                    value = InquiryFilter.ORGINATOR_COMPANY, required = false) String originatorCompnayName,
            @RequestParam(value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(
                    value = BaseFilter.SORT_DESC, defaultValue = "false") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count, Locale locale) {
        InquiryFilter filter = new InquiryFilter(senderId, false, disapproved, originatorCompnayName, sortBy, sortDesc,
                startItem, count);
        List<Inquiry> inquiries = inquiryAccessor.find(filter);

        JsonInquiries jsonInquiries = new JsonInquiries();
        for (Inquiry inquiry : inquiries) {
            jsonInquiries.add(new JsonInquiry(inquiry, documentUrlFactory, locale));
        }
        return jsonInquiries;
    }

    @RequestMapping(value = "/inquiries/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public JsonInquiry retrieveInquiry(@PathVariable Long id) {
        Inquiry inquiry = inquiryAccessor.load(id, Locale.ENGLISH);

        return new JsonInquiry(inquiry, documentUrlFactory, Locale.ENGLISH);
    }

    @RequestMapping(value = "/inquiries/{id}/approvals", method = RequestMethod.POST)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public void approvePendingInquiry(@PathVariable Long id) {
        inquiryMutator.update(inquiryAccessor.load(id, Locale.ENGLISH));
    }

    @RequestMapping(value = "/inquiries/{id}/disapprovals", method = RequestMethod.POST)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public void disapprovePendingInquiry(@PathVariable Long id) {
        inquiryMutator.delete(id);
    }

}
