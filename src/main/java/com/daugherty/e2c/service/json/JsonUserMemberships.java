package com.daugherty.e2c.service.json;

/**
 * Created with IntelliJ IDEA.
 * User: SHK0723
 * Date: 1/1/14
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls a list of User domain objects to/from JSON.
 */
@SuppressWarnings("serial")
@JsonRootName("userMemberships")
public class JsonUserMemberships extends ArrayList<JsonUserMembership> {
}
