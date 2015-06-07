package com.daugherty.e2c.domain;

/**
 * Represents a Gender.
 */
public enum Gender {

    MALE("Male"), //
    FEMALE("Female"); //

    private String readableName;

    private Gender(String readableName) {
        this.readableName = readableName;
    }

    public String getReadableName() {
        return readableName;
    }

    public static Gender findByReadableName(String readableName) {
        for (Gender gender : Gender.values()) {
            if (gender.getReadableName().equals(readableName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException(readableName + " is not a valid Gender ReadableName");
    }

    @Override
    public String toString() {
        return readableName;
    }

}
