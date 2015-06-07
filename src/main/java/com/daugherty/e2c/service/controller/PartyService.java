package com.daugherty.e2c.service.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.business.Accessor;
import com.daugherty.e2c.business.DocumentUrlFactory;
import com.daugherty.e2c.business.Mutator;
import com.daugherty.e2c.business.accessor.filter.BaseFilter;
import com.daugherty.e2c.business.accessor.filter.PartyFilter;
import com.daugherty.e2c.domain.Party;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonParties;

/**
 * REST resource for Party. This should be a Read Only Service. Party Creation should use specific party type services
 */
@Controller
@RequestMapping("/parties")
public class PartyService {

    @Inject
    private Accessor<Party> partyAccessor;
    @Inject
    private Mutator<Party> partyMutator;
    @Inject
    private DocumentUrlFactory documentUrlFactory;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN, Role.BUYER_MODERATOR })
    @ResponseBody
    public JsonParties retrieveParties(
            @RequestParam(value = PartyFilter.USERNAME_PREFIX, required = false) String username, @RequestParam(
                    value = PartyFilter.PARTY_TYPE, required = false) String partyType, @RequestParam(
                    value = BaseFilter.SORT_BY, required = false, defaultValue = "partyType") String sortBy,
            @RequestParam(value = BaseFilter.SORT_DESC, defaultValue = "true") Boolean sortDesc, @RequestParam(
                    value = BaseFilter.START_ITEM, defaultValue = "1") Integer startItem, @RequestParam(
                    value = BaseFilter.COUNT, defaultValue = "1000") Integer count, Locale locale) {

        PartyFilter filter = new PartyFilter(username, partyType, sortBy, sortDesc, startItem, count, locale);
        List<Party> parties = partyAccessor.find(filter);

        return new JsonParties(parties, documentUrlFactory, locale);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    public void deleteParty(@PathVariable String id) {
        partyMutator.delete(id);
    }
}
