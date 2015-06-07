package com.daugherty.e2c.persistence.data;


/**
 * Defines database write operations for application parameters.
 */
public interface ApplicationParameterWriteDao {

    public void updateValueForName(String name, String value);

}
