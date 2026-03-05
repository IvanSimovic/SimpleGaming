# Preserve source file names and line numbers for crash reports.
# Without this, stack traces in production show only obfuscated class names and no line numbers.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---- Kotlinx Serialization ----
# The serialization plugin transforms @Serializable classes at compile time, but R8 still needs
# to see the annotations and inner serializer classes to keep them from being stripped.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}

# ---- TikXml ----
# TikXml resolves annotated classes and their generated TypeAdapters at runtime via reflection.
# R8 will strip both the annotated model classes and the generated adapter classes without these rules.
-keep @com.tickaroo.tikxml.annotation.Xml class * { *; }
-keepclassmembers class * {
    @com.tickaroo.tikxml.annotation.Attribute *;
    @com.tickaroo.tikxml.annotation.Element *;
    @com.tickaroo.tikxml.annotation.ElementNameMatcher *;
    @com.tickaroo.tikxml.annotation.TextContent *;
    @com.tickaroo.tikxml.annotation.PropertyElement *;
}
# Keep the generated TypeAdapter classes (named <ModelClass>$$TypeAdapter by the processor)
-keep class **$$TypeAdapter { *; }
