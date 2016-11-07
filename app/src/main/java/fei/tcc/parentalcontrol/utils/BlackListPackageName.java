package fei.tcc.parentalcontrol.utils;

/**
 * Created by thiagoretondar on 06/11/16.
 */
public enum BlackListPackageName {

    SMOKETEST("com.android.smoketest"),
    TELEPHONY("com.android.providers.telephony"),
    GOOGLE_QUICK_SEARCH_BOX("com.google.android.googlequicksearchbox"),
    ANDROID_PROVIDERS("com.android.providers"),
    ANDROID_PROTIPS("com.android.protips"),
    ANDROID_GAPPS("com.google.process.gapps"),
    ANDROID_EXTERNAL_STORAGE("com.android.externalstorage"),
    HTML_VIEWER("com.android.htmlviewer"),
    ANDROID_DEFCONTAINER("com.android.defcontainer"),
    ANDROID_LICENSE("com.android.vending"),
    ANDROID_MEDIATEK("com.mediatek.nlpservice"),
    ANDROID_PROCESS_MEDIA("android.process.media"),
    ANDROID_ACORE("android.process.acore"),
    ANDROID_PACPROCESSOR("com.android.pacprocessor"),
    CERT_INSTALLER("com.android.certinstaller"),
    CARRIER_CONFIG("com.android.carrierconfig"),
    BACKUP_CONFIRM("com.android.backupconfirm"),
    ANDROID_LAUNCHER("com.google.android.launcher"),
    STATEMENT_SERVICE("com.android.statementservice"),
    SHARED_STORARAGE_BACKUP("com.android.sharedstoragebackup"),
    PRINT_SPOOLER("com.android.printspooler"),
    DREAMS_BASIC("com.android.dreams.basic"),
    ANDROID_WEBVIEW("com.android.webview"),
    INPUT_DEVICES("com.android.inputdevices"),
    BACKUP_TESTER("com.android.backuptester"),
    SDK_SETUP("com.android.sdksetup"),
    DEVELOPMEN_SETTING("com.android.development_settings"),
    SERVER_TELECOM("com.android.server.telecom"),
    ANDROID_KEYCHAN("com.android.keychain"),
    EMULATOR_SMOKETESTS("com.android.emulator.smoketests"),
    ANDROID_GMS("com.google.android.gms"),
    ANDROID_GSF("com.google.android.gsf"),
    PACKAGE_INSTALLER("com.android.packageinstaller"),
    SVOX_PICO("com.svox.pico"),
    ANDROID_APIS("com.example.android.apis"),
    PROXY_HANDLER("com.android.proxyhandler"),
    FALLBACK("com.android.fallback"),
    INPUTMETHOD_LATIN("com.android.inputmethod.latin"),
    MANAGED_PROVISIONING("com.android.managedprovisioning"),
    ANDORID_GSF_LOGIN("com.google.android.gsf.login"),
    ANDROID_WALLPAPER("com.android.wallpaper.livepicker"),
    ANDROID_NETSPEED("com.android.netspeed"),
    OMRONSOFT_OPENWNN("jp.co.omronsoft.openwnn"),
    ANDROID_SETTINGS("com.android.settings"),
    ANDROID_GESTURE("com.android.gesture.builder"),
    ANDROID_VPNDIALOGS("com.android.vpndialogs"),
    ANDROID_PHONE("com.android.phone"),
    ANDROID_SHELL("com.android.shell"),
    ANDROID_DICTIONARY("com.android.providers.userdictionary"),
    ANDROID_LOCATION_FUSED("com.android.location.fused"),
    ANDROID_DESKLOCK("com.android.deskclock"),
    ANDROID_SYSTEMUI("com.android.systemui"),
    ANDROID_EXCHANGE("com.android.exchange"),
    ANDROID_SMOKETESTS_TESTS("com.android.smoketest.tests"),
    ANDROID_CUSTOMLOCALE2("com.android.customlocale2"),
    ANDROID_SOFTKEYBOARD("com.example.android.softkeyboard"),
    ANDROID_PLAY_GAMES("com.google.android.play.games"),
    ANDROID_DEVELOPMENT("com.android.development"),
    ANDROID_CAPTIVE_PORT_ALL_LOGIN("com.android.captiveportallogin"),
    ANDROID_WIDGET_PREVIEW("com.android.widgetpreview"),
    LIVECUBES("com.example.android.livecubes");

    private String packageName;

    BlackListPackageName(String packageName) {
        this.packageName = packageName;
    }
}
