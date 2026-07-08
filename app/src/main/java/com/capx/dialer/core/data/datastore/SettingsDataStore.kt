package com.capx.dialer.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Top-level DataStore delegate — creates a single instance scoped to
 * the application [Context]. The file is stored at
 * `data/data/com.capx.dialer/files/datastore/dialer_settings.preferences_pb`.
 */
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "dialer_settings"
)

/**
 * Typed wrapper around [DataStore] for user-facing settings.
 *
 * Every preference is exposed as a [Flow] so the UI layer can
 * reactively observe changes. Write operations are suspend functions
 * that atomically update the backing store.
 *
 * Default values:
 * - **Dark mode** — `false` (follows system by default)
 * - **Haptic feedback** — `true`
 * - **Auto-record calls** — `false`
 */
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // ── Preference keys ──────────────────────────────────────────

    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val AUTO_RECORD_ENABLED = booleanPreferencesKey("auto_record_enabled")
    }

    // ── Readers (Flow) ───────────────────────────────────────────

    /** Observes whether dark mode is enabled. Defaults to `false`. */
    val isDarkMode: Flow<Boolean> = context.settingsDataStore.data
        .map { prefs -> prefs[Keys.DARK_MODE] ?: false }

    /** Observes whether haptic feedback is enabled. Defaults to `true`. */
    val isHapticEnabled: Flow<Boolean> = context.settingsDataStore.data
        .map { prefs -> prefs[Keys.HAPTIC_ENABLED] ?: true }

    /** Observes whether automatic call recording is enabled. Defaults to `false`. */
    val isAutoRecordEnabled: Flow<Boolean> = context.settingsDataStore.data
        .map { prefs -> prefs[Keys.AUTO_RECORD_ENABLED] ?: false }

    // ── Writers (suspend) ────────────────────────────────────────

    /** Persists the dark-mode preference. */
    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.DARK_MODE] = enabled
        }
    }

    /** Persists the haptic-feedback preference. */
    suspend fun setHapticEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.HAPTIC_ENABLED] = enabled
        }
    }

    /** Persists the auto-record preference. */
    suspend fun setAutoRecordEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.AUTO_RECORD_ENABLED] = enabled
        }
    }
}
