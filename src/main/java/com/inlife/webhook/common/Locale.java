/*
 *
 */
package com.inlife.webhook.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Locale {
    public static final String en_US = "en_US";
    public static final String fi_FI = "fi_FI";
    public static final String sv_SE = "sv_SE";
    public static final String de_DE = "de_DE";
    public static final String fr_FR = "fr_FR";
    public static final String tr_TR = "tr_TR";
    public static final String el_GR = "el_GR";
    public static final String bg_BG = "bg_BG";
    public static final String ro_RO = "ro_RO";
    public static final String hu_HU = "hu_HU";
    public static final String pl_PL = "pl_PL";
    public static final String cs_CZ = "cs_CZ";
    public static final String nb_NO = "nb_NO";
    public static final String es_ES = "es_ES";
    public static final String it_IT = "it_IT";
    public static final String et_EE = "et_EE";
    public static final String da_DK = "da_DK";
    public static final String nl_NL = "nl_NL";

    public static final List<String> locales = ImmutableList.of(en_US, fi_FI, sv_SE, de_DE, fr_FR, tr_TR,
            el_GR, bg_BG, ro_RO, hu_HU, pl_PL, cs_CZ, nb_NO, es_ES, it_IT, et_EE, da_DK, nl_NL);

    public static final List<String> supportedLocales = ImmutableList.of(en_US, fi_FI);
    public static final Set<String> languages;
    public static final Set<String> countries;

    public static final Map<String, String> languageToLocale;

    static {
        ImmutableMap.Builder<String, String> languageToLocaleBuilder = ImmutableMap.builder();
        locales.forEach(l -> languageToLocaleBuilder.put(l.substring(0, 2), l));
        languageToLocale = languageToLocaleBuilder.build();

        ImmutableSet.Builder<String> languagesBuilder = ImmutableSet.builder();
        locales.forEach(l -> languagesBuilder.add(l.substring(0, 2)));
        languages = languagesBuilder.build();

        ImmutableSet.Builder<String> countriesBuilder = ImmutableSet.builder();
        locales.forEach(l -> countriesBuilder.add(l.substring(3).toLowerCase()));
        countries = countriesBuilder.build();
    }

    public static java.util.Locale getCurrent() {
        return LocaleContextHolder.getLocale();
    }

    public static void validate(String locale) {
        Preconditions.checkArgument(locales.contains(locale));
    }

    public static java.util.Locale toJavaLocale(String locale) {
        String[] splitted = locale.split("_");
        return new java.util.Locale(splitted[0], splitted[1]);
    }

    private Locale() {
    }

}
