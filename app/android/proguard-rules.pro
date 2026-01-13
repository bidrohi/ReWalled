# Preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable
# Hide the original source file name.
-renamesourcefileattribute Rwl
# Repackage renamed classes under.
-repackageclasses 'rwl'
# Or repackage renamed classes with flatter hierarchy.
#-flattenpackagehierarchy
# Optional: Keep custom exceptions.
-keep public class * extends java.lang.Exception
# Optional: For projects using WebView with JS,
# specify the fully qualified class name to the JavaScript interface class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

## Ktor
-dontwarn org.slf4j.impl.StaticLoggerBinder
