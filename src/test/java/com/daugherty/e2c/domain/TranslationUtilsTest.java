package com.daugherty.e2c.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class TranslationUtilsTest {

    @Test
    public void toPinyinIfChinese() {
        assertThat(TranslationUtils.toPinyinIfChinese("some 你好 ram", Locale.CHINESE), is("some nĭhăo ram"));
        assertThat(TranslationUtils.toPinyinIfChinese("查詢促進民間參與公共建設法（210ＢＯＴ法）", Locale.CHINESE),
                is("cháxúncùjìnmínjiāncānyŭgōnggòngjiànshèfă（210ＢＯＴfă）"));
        assertThat(TranslationUtils.toPinyinIfChinese("你好", Locale.CHINESE), is("nĭhăo"));
        assertThat(TranslationUtils.toPinyinIfChinese("CHINESE", Locale.CHINESE), is("CHINESE"));
    }

}
