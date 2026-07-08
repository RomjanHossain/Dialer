package com.capx.dialer.core.telecom

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager

/**
 * Helpers for checking and requesting default-phone-app (dialer) status.
 *
 * The app's custom [DialerInCallService] / in-call UI only takes over from the
 * system calling screen when this app is the device's default dialer, so the
 * setup flow asks the user to grant it.
 */
object DefaultDialer {

    /** True if this app is currently the device's default dialer. */
    fun isDefault(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(RoleManager::class.java)
            roleManager != null &&
                roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) &&
                roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
        } else {
            val telecom = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            telecom.defaultDialerPackage == context.packageName
        }
    }

    /**
     * Builds the platform intent that prompts the user to make this app the
     * default dialer. Returns null if it cannot be constructed.
     */
    fun createRequestIntent(context: Context): Intent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getSystemService(RoleManager::class.java)
                ?.createRequestRoleIntent(RoleManager.ROLE_DIALER)
        } else {
            @Suppress("DEPRECATION")
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(
                    TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
                    context.packageName
                )
        }
    }
}
