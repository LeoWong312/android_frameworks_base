package com.android.internal.util.mahdi;

import android.R;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.hardware.display.WifiDisplayStatus;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.PhoneConstants;

public class QSUtils {
        public static boolean deviceSupportsImeSwitcher(Context ctx) {
            Resources res = ctx.getResources();
            return res.getBoolean(com.android.internal.R.bool.config_show_cmIMESwitcher);
        }

        public static boolean deviceSupportsUsbTether(Context ctx) {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (cm.getTetherableUsbRegexs().length != 0);
        }

        public static boolean deviceSupportsWifiDisplay(Context ctx) {
            DisplayManager dm = (DisplayManager) ctx.getSystemService(Context.DISPLAY_SERVICE);
            return (dm.getWifiDisplayStatus().getFeatureState() != WifiDisplayStatus.FEATURE_STATE_UNAVAILABLE);
        }

        public static boolean deviceSupportsMobileData(Context ctx) {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE);
        }

        public static boolean deviceSupportsBluetooth() {
            return (BluetoothAdapter.getDefaultAdapter() != null);
        }

        public static boolean systemProfilesEnabled(ContentResolver resolver) {
            return (Settings.System.getInt(resolver, Settings.System.SYSTEM_PROFILES_ENABLED, 1) == 1);
        }

        public static boolean globalImmersiveModeEnabled(ContentResolver resolver) {
            return (Settings.System.getIntForUser(resolver, Settings.System.GLOBAL_IMMERSIVE_MODE_STYLE, 0,
                    UserHandle.USER_CURRENT_OR_SELF) != 0);
        }

        public static boolean deviceSupportsNfc(Context ctx) {
            return NfcAdapter.getDefaultAdapter(ctx) != null;
        }

        public static boolean deviceSupportsLte(Context ctx) {
            final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return (tm.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE) || tm.getLteOnGsmMode() != 0;
        }

        public static boolean deviceSupportsDockBattery(Context ctx) {
            Resources res = ctx.getResources();
            return res.getBoolean(com.android.internal.R.bool.config_hasDockBattery);
        }

        public static boolean deviceSupportsCamera() {
            return Camera.getNumberOfCameras() > 0;
        }

        public static boolean deviceSupportsGps(Context context) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        }

        public static boolean deviceSupportsTorch(Context context) {
            return context.getResources().getBoolean(com.android.internal.R.bool.config_enableTorch);
        }

        public static boolean adbEnabled(ContentResolver resolver) {
            return (Settings.Global.getInt(resolver, Settings.Global.ADB_ENABLED, 0)) == 1;
        }

	public static boolean NavBarEnabled(ContentResolver resolver) {
            return (Settings.System.getInt(resolver, Settings.System.NAVIGATION_BAR_SHOW, 1) == 1);
        }

	public static boolean NetworkSpeedEnabled(ContentResolver resolver) {
            return (Settings.System.getInt(resolver, Settings.System.STATUS_BAR_TRAFFIC, 1) == 1);
        }
}
