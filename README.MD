# dcm4che - Typed DICOM
> :warning: **This project is under active development and not ready for production yet. Use at own risk.**

This library creates typed wrappers around dcm4che attributes to make DICOM objects easier and more intuitive to use in Java.
We tried our best to mimic a lot of the DICOM standard in this library but due to the complexity and openness of the standard we cannot possibly cover everything.
If you encounter unsupported fields you can always use the `getAttributes()` method and use dcm4che means to get whatever you want from the data.

## How to add it to your project
### Gradle
#### Groovy
```groovy
repositories {
    // Add dicom4che repository
    maven {
        url "https://www.dcm4che.org/maven2/"
    }
}
```

```groovy
dependencies {
    implementation 'org.dcm4che:dcm4che-typeddicom:0.1.1-SNAPSHOT'
}
```

#### Kotlin
```kotlin
repositories {
    // Add dicom4che repository
    maven {
        url = uri("https://www.dcm4che.org/maven2/")
    }
}
```

```kotlin
dependencies {
    implementation("org.dcm4che:dcm4che-typeddicom:0.1.1-SNAPSHOT")
}
```

### Maven

```xml
<!-- Add dicom4che repository -->
<repository>
  <id>Dcm4Che</id>
  <name>Dcm4Che</name>
  <url>https://www.dcm4che.org/maven2/</url>
</repository>
```

```xml
<dependency>
    <groupId>org.dcm4che</groupId>
    <artifactId>dcm4che-typeddicom</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```

## How to build it yourself
Run ```.\gradlew jar```. The JAR-file is then located under ```typeddicom-lib/build/libs/typeddicom-lib.jar```.

You can also run ```.\gradlew publishToMavenLocal``` to install it to your local Maven repository.
## How to use
Instead of
```
Attributes attributes = readDicomFile("GSPS.dcm");

int[] untypedPixelAspectRatio = attributes.getSequence(Tag.DisplayedAreaSelectionSequence)
.get(0)
.getInts(Tag.PresentationPixelAspectRatio);

```
you can now use
```
Attributes attributes = readDicomFile("GSPS.dcm");

GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD(attributes);
int[] typedDicomPixelAspectRatio = gsps.getDisplayedAreaSelectionSequence()
        .get(0)
        .getPresentationPixelAspectRatio()
        .getInts();
```
Where everything is checked for type-safety.

Also try out the javadocs. They should represent the stuff written in the DICOM Standard.

