package com.daugherty.e2c.service.controller;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daugherty.e2c.security.Role;
import com.daugherty.e2c.service.json.JsonMethodExecutionProfile;
import com.daugherty.e2c.service.json.JsonMethodExecutionProfiles;
import com.daugherty.profile.MethodExecutionProfile;
import com.daugherty.profile.MethodExecutionProfileReporter;

/**
 * REST resource for system diagnostics.
 */
@Controller
public class DiagnosticsService {

    private static final Comparator<MethodExecutionProfile> DESCENDING_AVERAGE_TIME_PROFILE_COMPARATOR = new Comparator<MethodExecutionProfile>() {
        @Override
        public int compare(MethodExecutionProfile profile1, MethodExecutionProfile profile2) {
            return -profile1.getAverageTime().compareTo(profile2.getAverageTime());
        }
    };

    @Inject
    private MethodExecutionProfileReporter profileReporter;

    @RequestMapping(value = "/methodExecutionProfiles", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Role.ADMIN)
    @ResponseBody
    public JsonMethodExecutionProfiles getMethodExecutionProfiles() {
        JsonMethodExecutionProfiles jsonProfiles = new JsonMethodExecutionProfiles();
        List<MethodExecutionProfile> profiles = profileReporter.getProfiles(DESCENDING_AVERAGE_TIME_PROFILE_COMPARATOR);
        for (MethodExecutionProfile profile : profiles) {
            jsonProfiles.add(new JsonMethodExecutionProfile(profile));
        }
        return jsonProfiles;
    }

}
