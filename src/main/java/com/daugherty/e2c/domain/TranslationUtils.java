package com.daugherty.e2c.domain;

import java.lang.Character.UnicodeBlock;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for translating text.
 */
public class TranslationUtils {

    static Set<Character.UnicodeBlock> chineseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {
        private static final long serialVersionUID = 1L;

        {
            add(Character.UnicodeBlock.CJK_COMPATIBILITY);
            add(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
            add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
            add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
            add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
            add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
            add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
            add(UnicodeBlock.KANGXI_RADICALS);
            add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
        }
    };

    private final static Logger LOGGER = LoggerFactory.getLogger(TranslationUtils.class);

    private TranslationUtils() {
    }

    public static String toPinyinIfChinese(String text, Locale locale) {
        if (Locale.CHINESE.equals(locale) && StringUtils.isNotBlank(text)) {
            return toPinyin(text);
        }
        return text;
    }

    private static String toPinyin(String chineseText) {
        StringBuilder pinyin = new StringBuilder();
        for (int i = 0; i < chineseText.length(); i++) {
            char c = chineseText.charAt(i);
            pinyin.append(toPinyin(c));
        }
        return pinyin.toString().trim();
    }

    private static String toPinyin(char c) {
        HanyuPinyinOutputFormat defaultFormat = getHanyuPinyinOutputFormat();
        String[] pinyinArray = null;
        if (isChineseCharacter(c)) {
            try {
                pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                LOGGER.warn("Unable to convert character '" + c + "' to Pinyin");
            }
        }
        if (pinyinArray != null) {
            return pinyinArray[0];
        } else {
            return String.valueOf(c);
        }
    }

    private static HanyuPinyinOutputFormat getHanyuPinyinOutputFormat() {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        return defaultFormat;
    }

    private static boolean isChineseCharacter(char c) {
        return chineseUnicodeBlocks.contains(UnicodeBlock.of(c));
    }

}
