package com.daugherty.e2c.domain;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a language into which text can be translated.
 */
public enum Language {

    ENGLISH(1L, "en", "English"), // Should never be used
    CHINESE(2L, "zh", "Chinese"), // Mandarin, actually
    SPANISH(3L, "es", "Spanish");

    private Long id;
    private String abbreviation;
    private String displayName;

    private Language(Long id, String abbreviation, String displayName) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean matches(Locale locale) {
        if (locale != null) {
            return abbreviation.equals(locale.getLanguage());
        }
        return false;
    }

    public static Language findById(Long id) {
        if (id != null && id != 0L) {
            for (Language language : Language.values()) {
                if (language.getId().equals(id)) {
                    return language;
                }
            }
            throw new EnumConstantNotPresentException(Language.class, id.toString());
        }
        return null;
    }

    public static Language findByAbbreviation(String abbreviation) {
        if (StringUtils.isNotBlank(abbreviation)) {
            for (Language language : Language.values()) {
                if (language.getAbbreviation().equals(abbreviation)) {
                    return language;
                }
            }
            throw new EnumConstantNotPresentException(Language.class, abbreviation);
        }
        return null;
    }

    public static Language findByDisplayName(String displayName) {
        if (StringUtils.isNotBlank(displayName)) {
            for (Language language : Language.values()) {
                if (language.getDisplayName().equals(displayName)) {
                    return language;
                }
            }
            throw new EnumConstantNotPresentException(Language.class, displayName);
        }
        return null;
    }
}
