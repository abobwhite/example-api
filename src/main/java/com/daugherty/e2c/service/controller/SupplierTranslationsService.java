package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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
import com.daugherty.e2c.business.ProfilePublisher;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.SupplierTranslationFilter;
import com.daugherty.e2c.business.mapper.ProfileTranslationMapper;
import com.daugherty.e2c.business.mapper.SupplierTranslationMapper;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Membership;
import com.daugherty.e2c.domain.ProfileTranslation;
import com.daugherty.e2c.domain.Supplier;
import com.daugherty.e2c.domain.SupplierTranslation;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonProfileTranslation;
import com.daugherty.e2c.service.json.JsonSupplierTranslations;
import com.google.common.collect.Maps;

/**
 * REST resource for Supplier Translations.
 */
@Controller
public class SupplierTranslationsService {

    @Inject
    private Accessor<SupplierTranslation> supplierTranslationAccessor;
    @Inject
    private Accessor<ProfileTranslation> profileTranslationAccessor;
    @Inject
    private Accessor<Supplier> latestSupplierAccessor;
    @Inject
    private Mutator<SupplierTranslation> supplierTranslationMutator;
    @Inject
    private Mutator<ProfileTranslation> profileTranslationMutator;
    @Inject
    private ApprovalStateTransitionVisitor approveVisitor;
    @Inject
    private ApprovalStateTransitionVisitor moderateVisitor;
    @Inject
    private ProfilePublisher profilePublisher;
    @Inject
    private Accessor<Membership> membershipAccessor;
    @Inject
    private Mutator<Membership> supplierApprovalMembershipMutator;
    @Inject
    private SupplierTranslationMapper supplierTranslationMapper;
    @Inject
    private ProfileTranslationMapper profileTranslationMapper;

    @RequestMapping(value = "/supplierTranslations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public JsonSupplierTranslations retrieveSupplierTranslations(@RequestParam(
            value = SupplierTranslationFilter.TITLE_PREFIX, required = false) String titlePrefix, @RequestParam(
            value = SupplierTranslationFilter.TYPE, required = false) String translationType, @RequestParam(
            value = SupplierTranslationFilter.TRANSLATED, required = false) String translated, @RequestParam(
            value = BaseFilter.SORT_BY, required = false) String sortBy, @RequestParam(value = BaseFilter.SORT_DESC,
            defaultValue = "false") Boolean sortDesc,
            @RequestParam(value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "250") Integer count) {

        SupplierTranslationFilter filter = new SupplierTranslationFilter(titlePrefix, translationType,
                translatedAsBoolean(translated), sortBy, sortDesc, startItem, count);
        List<SupplierTranslation> translations = supplierTranslationAccessor.find(filter);

        JsonSupplierTranslations jsonTranslations = new JsonSupplierTranslations();
        for (SupplierTranslation translation : translations) {
            jsonTranslations.add(supplierTranslationMapper.fromExistingDomainObject(translation));
        }
        return jsonTranslations;
    }

    @RequestMapping(value = "/profileTranslations/{supplierId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public JsonProfileTranslation saveProfileTranslation(@RequestBody JsonProfileTranslation jsonTranslation,
            @PathVariable String supplierId) {
        ProfileTranslation translation = profileTranslationMapper.toExistingDomainObject(latestSupplierAccessor.load(supplierId,
                Locale.CHINESE), jsonTranslation);
        ProfileTranslation updatedTranslation = profileTranslationMutator.update(translation);
        return profileTranslationMapper.fromExistingDomainObject(updatedTranslation, getLinks(supplierId, "supplier"));
    }

    @RequestMapping(value = "/profileTranslations/{supplierId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public JsonProfileTranslation retrieveProfileTranslation(@PathVariable String supplierId) {
        ProfileTranslation profileTranslation = profileTranslationAccessor.load(supplierId, Locale.ENGLISH);
        return profileTranslationMapper.fromExistingDomainObject(profileTranslation, getLinks(supplierId, "supplier"));
    }

    @RequestMapping(value = "/supplierTranslations/{supplierId}/approved", method = RequestMethod.POST)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public void approveTranslation(@PathVariable String supplierId) {
        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(supplierId, Locale.ENGLISH);
        supplierTranslation.visit(approveVisitor);
        supplierTranslationMutator.update(supplierTranslation);

        profilePublisher.publish(supplierId);

        Supplier supplier = latestSupplierAccessor.load(supplierId, Locale.ENGLISH);
        Membership membership = membershipAccessor.load(supplier.getMembershipId(), Locale.ENGLISH);
        supplierApprovalMembershipMutator.update(membership);
    }

    @RequestMapping(value = "/supplierTranslations/{id}/sendToModerator", method = RequestMethod.POST)
    @Secured({ Role.TRANSLATOR })
    @ResponseBody
    public void sendToModerator(@PathVariable String id) {
        SupplierTranslation supplierTranslation = supplierTranslationAccessor.load(id, Locale.ENGLISH);
        supplierTranslation.visit(moderateVisitor);
        supplierTranslationMutator.update(supplierTranslation);
    }

    private Boolean translatedAsBoolean(String translated) {
        return StringUtils.isBlank(translated) ? null : translated.toLowerCase().equals("yes");
    }

    private Map<String, String> getLinks(String id, String service) {
        Map<String, String> links = Maps.newHashMap();
        links.put(service, service + "s/" + id + "?latest=true");
        return links;
    }
}
