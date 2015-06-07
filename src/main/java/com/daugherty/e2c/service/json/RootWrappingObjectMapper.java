package com.daugherty.e2c.service.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Ensures Json transfer objects get serialized with a root node that the domain objects don't have.
 */
@SuppressWarnings("serial")
public class RootWrappingObjectMapper extends ObjectMapper {

    public RootWrappingObjectMapper() {
        super();
        /*
         * final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
         */

        this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        this.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

        /*
         * this.setDeserializationConfig(this.getDeserializationConfig().withAnnotationIntrospector(introspector));
         * this.setSerializationConfig(this.getSerializationConfig().withAnnotationIntrospector(introspector));
         */

    }
}
