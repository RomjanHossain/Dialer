# ==============================================================================
# 1. CORE SYSTEM DIALER RULES (CRITICAL)
# ==============================================================================

# Keep your custom ConnectionService & InCallService implementations.
# The Android Telecom framework binds to these using system intents;
# if they are renamed or stripped, the OS cannot route calls to your app.
-keep class * extends android.telecom.ConnectionService { *; }
-keep class * extends android.telecom.InCallService { *; }
-keep class * extends android.telecom.Connection { *; }
-keep class * extends android.telecom.Conference { *; }

# Keep all broadcast receivers that listen for outbound calls or phone state changes.
-keep class * extends android.content.BroadcastReceiver { *; }

# ==============================================================================
# 2. DATA MODELS & CONTACT RESOLUTION
# ==============================================================================

# Replace 'com.capx.dialer' with your app's actual root package.
# This prevents your API, Room, or JSON Data Transfer Objects (DTOs)
# from being stripped, which causes null fields when parsing contact info.
-keepclassmembers class com.capx.dialer.data.models.** {
    <fields>;
    <methods>;
}

# Keep SerializedName annotations if you use GSON/Moshi for contact APIs.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ==============================================================================
# 3. KOTLIN & REFLECTION SAFETY (AGRessive Optimization Defaults)
# ==============================================================================

# Keep Kotlin metadata intact to prevent reflection crashes.
-keep class kotlin.Metadata { *; }

# Keep Parcelable implementations (essential for passing call data via Intents).
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Enums intact (prevents crashes with switch statements on call states).
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ==============================================================================
# 4. DEBUGGING HELPER
# ==============================================================================

# Retain original line numbers in crash reports so you can de-obfuscate logcats.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile