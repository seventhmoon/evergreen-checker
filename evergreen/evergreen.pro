# ProGuard configuration for Evergreen

-mergeinterfacesaggressively
-flattenpackagehierarchy
-repackageclasses
-printseeds
-printusage


# Data Model:
# -----------
-keep class app.evergreen.config.** { *; }

# OkHttp3
# -------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# For easier debugging with Crashlytics
# -------------------------------------
-keepattributes *Annotation*
-keepattributes SourceFile, LineNumberTable
-keep public class * extends java.lang.Throwable

# Android defaults:
# -----------------
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepattributes Signature
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
