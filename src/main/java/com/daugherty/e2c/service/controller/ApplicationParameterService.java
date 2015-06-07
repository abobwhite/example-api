package com.daugherty.e2c.service.controller;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.persistence.data.ApplicationParameterReadDao;
import com.daugherty.e2c.persistence.data.ApplicationParameterWriteDao;
import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonSearchParameters;

/**
 * REST resource for Application Parameters.
 */
@Controller
@RequestMapping("/parameters")
public class ApplicationParameterService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Inject
    private ApplicationParameterReadDao applicationParameterReadDao;
    @Inject
    private ApplicationParameterWriteDao applicationParameterWriteDao;

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    @Transactional(readOnly = true)
    public JsonSearchParameters retrieveSearchParameters(@PathVariable Long id) {
        return new JsonSearchParameters(applicationParameterReadDao.loadAllValues());
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ Role.ADMIN })
    @ResponseBody
    @Transactional
    public JsonSearchParameters updateSearchParameters(@PathVariable Long id,
            @RequestBody JsonSearchParameters jsonSearchParameters) {
        Map<String, String> applicationParameterMap = jsonSearchParameters.toMap();

        for (String key : applicationParameterMap.keySet()) {
            applicationParameterWriteDao.updateValueForName(key, applicationParameterMap.get(key));
        }

        return jsonSearchParameters;
    }
}
