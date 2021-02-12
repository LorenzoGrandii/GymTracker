package com.grandi.lorenzo.gymtracker;

public enum KeyLoader {
    /*===================================== PREFERENCE KEYS ======================================*/
    LOGIN_PREFERENCE_FILE ("com.grandi.lorenzo.gymtracker.loginPreference"),
    SETTINGS_PREFERENCE_FILE("com.grandi.lorenzo.gymtracker.settingsPreference"),
    emailKey ("emailKey"),
    nameKey ("nameKey"),
    passwordKey ("passwordKey"),
    loggedKey ("loggedKey"),
    trainingKey ("trainingKey"),
    temperatureKey ("temperatureKey"),
    stepCounterKey ("stepCounterKey"),
    humidityKey ("humidityKey"),

    /*======================================= FILE STRINGS =======================================*/
    REGISTRATION_FILE ("userRegistrations.txt"),

    /*====================================== QRCODE STRINGS ======================================*/
    strQRFlag (""),

    /*======================================= EXTRAS KEYS ========================================*/
    EXTRA_ACCOUNT ("com.grandi.lorenzo.gymtracker.ACCOUNT");

    /*======================================= CONSTRUCTOR ========================================*/
    private final String value;
    KeyLoader(String value) {
        this.value = value;
    }
    public java.lang.String getValue() {
        return value;
    }
}
