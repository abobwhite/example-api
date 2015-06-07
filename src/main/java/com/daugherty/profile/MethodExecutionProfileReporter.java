package com.daugherty.profile;

import java.util.Comparator;
import java.util.List;

/**
 * Defines operations for reporting existing method execution profiles.
 */
public interface MethodExecutionProfileReporter {

    List<MethodExecutionProfile> getProfiles(Comparator<MethodExecutionProfile> comparator);

}
