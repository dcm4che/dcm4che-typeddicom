# dcm4che - Typed DICOM
> :warning: **This project is under active development and not ready for production yet. Use at own risk.**

This library creates typed wrappers around dcm4che attributes to make DICOM objects easier and more intuitive to use in Java.
We tried our best to mimic a lot of the DICOM standard in this library but due to the complexity and openness of the standard we cannot possibly cover everything.
If you encounter unsupported fields you can always use the `getAttributes()` method and use dcm4che means to get whatever you want from the data.

## Dependencies
This library is based on the 
* [DICOM Standard Version 2022d](https://dicom.nema.org/medical/dicom/2022d/output/chtml/part01/ps3.1.html).
* [dcm4che version 5.29.0](https://github.com/dcm4che/dcm4che/releases/tag/5.29.0)

## How to add it to your project
### Standard Library
#### Gradle
##### Groovy
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
    implementation 'org.dcm4che:dcm4che-typeddicom-lib-std:0.5.5-SNAPSHOT'
}
```

##### Kotlin
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
    implementation("org.dcm4che:dcm4che-typeddicom-lib-std:0.5.5-SNAPSHOT")
}
```

#### Maven

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
    <version>0.5.5-SNAPSHOT</version>
</dependency>
```
### Use private tags with the gradle plugin

In the ```build.gradle``` put:
```groovy
buildscript {
    repositories {
        // Add dicom4che repository
        maven {
            url "https://www.dcm4che.org/maven2/"
        }
    }
}

plugins {
    id 'java-library'
    id 'org.dcm4che.typeddicom-java-generator' version '0.5.5-SNAPSHOT'
}

generateTypeddicomJavaSources {
    privateDicomMetamodelYamlDirectory = layout.projectDirectory.dir("src/main/resources") // default - so this is optional when using this directory
    generatedJavaOutputDirectory = layout.buildDirectory.dir("typeddicom") // default - so this is optional when using this directory
}
```
or put this into ```build.gradle.kts```:
```kotlin
buildscript {
    repositories {
        // Add dicom4che repository
        maven {
            url = uri("https://www.dcm4che.org/maven2/")
        }
    }
}

plugins {
    id("java-library")
    id("org.dcm4che.typeddicom-java-generator") version "0.5.5-SNAPSHOT"
}

generateTypeddicomJavaSources {
    privateDicomMetamodelYamlDirectory.set(layout.projectDirectory.dir("src/main/resources")) // default - so this is optional when using this directory
    generatedJavaOutputDirectory.set(layout.buildDirectory.dir("typeddicom")) // default - so this is optional when using this directory
}
```
Put your custom yaml file into the specified ```src/main/resources``` folder and populate it with your custom tags. Examples for this are plenty in ```dcm4che-typeddicom-java-generator-gradleplugin-<version>.jar/std.dicom-meta-model.yaml```.

* ```gradle generateJavaSourceFiles``` will generate the source files in the specified ```build/typeddicom``` folder.
* You can do anything as with the usual `java-library` projects (see https://docs.gradle.org/current/userguide/java_library_plugin.html). The ```generateJavaSourceFiles``` task is properly integrated with them and will run when needed. 

## How to build it yourself
Run ```.\gradlew build```. The JAR-files are then located under: 
* ```dcm4che-typeddicom-skeleton\build\libs``` for the skeleton java library containing interfaces and abstract classes which are implemented and extended by the generated classes.
* ```dcm4che-typeddicom-lib/dcm4che-typeddicom-lib-std\build\libs``` contains an easily usable library for all standard dicom IODs, Modules and Attributes.
* ```dcm4che-typeddicom-parser/dcm4che-typeddicom-parser-dtos\build\libs``` contains the DTOs which are needed to parse the metadata representation of the dicom standard.
* ```dcm4che-typeddicom-generator/dcm4che-typeddicom-java-generator-lib\build\libs``` contains a library to generate a typeddicom implementation with custom adaptations using additional yaml files.
* ```dcm4che-typeddicom-generator/dcm4che-typeddicom-java-generator-gradleplugin\build\libs``` contains the plugin which generates a typeddicom java implementation from a provided private tags file.
You can also run ```.\gradlew publishToMavenLocal``` to install all packages to your local Maven repository.
## How to use
### Reading fields
Instead of
```java
Attributes attributes = readDicomFile("GSPS.dcm");

int[] untypedPixelAspectRatio = attributes.getSequence(Tag.DisplayedAreaSelectionSequence)
.get(0)
.getInts(Tag.PresentationPixelAspectRatio);

```
you can now use
```java
Attributes attributes = readDicomFile("GSPS.dcm");

GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD(attributes);
int[] typedDicomPixelAspectRatio = gsps.getDisplayedAreaSelectionSequence()
        .get(0)
        .getPresentationPixelAspectRatio()
        .getInts();
```
Where everything is checked for type-safety.

### Synthesizing Dicom Attributes
A fluent API was implemented to make DICOM attributes synthesizing easily read and writable.
Instead of
```java
Attributes gspsClassic = new Attributes();
gspsClassic.setString(Tag.PatientName, VR.PN, "Niklas");
Sequence referencedSeriesSequence = gspsClassic.newSequence(Tag.ReferencedSeriesSequence, 1);
Attributes referencedSeriesSequenceItem = new Attributes();
referencedSeriesSequenceItem.setString(Tag.SeriesInstanceUID, VR.UI, "1234567890.345678.3456789");
Sequence referencedInstanceSequence = referencedSeriesSequenceItem.newSequence(Tag.ReferencedImageSequence, 2);
Attributes referencedInstanceSequenceItem1 = new Attributes();
referencedInstanceSequenceItem1.setString(Tag.ReferencedSOPInstanceUID, VR.UI, "3483648368436.483.864369.43648.368");
referencedInstanceSequenceItem1.setString(Tag.ReferencedSOPClassUID, VR.UI, "8646.36.54186.86408684371");
referencedInstanceSequenceItem1.setInt(Tag.ReferencedFrameNumber, VR.IS, 1, 2, 3, 4, 5);
referencedInstanceSequence.add(referencedInstanceSequenceItem1);
Attributes referencedInstanceSequenceItem2 = new Attributes();
referencedInstanceSequenceItem2.setString(Tag.ReferencedSOPInstanceUID, VR.UI, "782583648368436.478754369.436487827");
referencedInstanceSequenceItem2.setString(Tag.ReferencedSOPClassUID, VR.UI, "7827287.5634836.8463841.3684.3");
referencedInstanceSequenceItem2.setInt(Tag.ReferencedFrameNumber, VR.IS, 0, 1, 2, 3, 4);
referencedInstanceSequence.add(referencedInstanceSequenceItem2);
referencedSeriesSequence.add(referencedSeriesSequenceItem);
Sequence displayedAreaSelectionSequence = gspsClassic.newSequence(Tag.DisplayedAreaSelectionSequence, 1);
Attributes displayedAreaSelectionSequenceItem = new Attributes();
displayedAreaSelectionSequenceItem.setInt(Tag.DisplayedAreaTopLeftHandCorner, VR.SL, 1, 1);
displayedAreaSelectionSequenceItem.setInt(Tag.DisplayedAreaBottomRightHandCorner, VR.SL, 1920, 1080);
displayedAreaSelectionSequenceItem.setInt(Tag.PresentationPixelAspectRatio, VR.IS, 1, 1);
displayedAreaSelectionSequence.add(displayedAreaSelectionSequenceItem);
```
you can now use
```java
GrayscaleSoftcopyPresentationStateIOD gsps = GrayscaleSoftcopyPresentationStateIOD.builder()
    .setPatientName().asString("Niklas")
    .setReferencedSeriesSequence(
        ReferencedSeriesSequence.Item.builder()
            .setSeriesInstanceUID().asString("1234567890.345678.3456789")
            .setReferencedImageSequence(
                ReferencedImageSequence.Item.builder()
                    .setReferencedSOPInstanceUID().asString("3483648368436.483.864369.43648.368")
                    .setReferencedSOPClassUID().asString("8646.36.54186.86408684371")
                    .setReferencedFrameNumber().asInts(1, 2, 3, 4, 5),
                ReferencedImageSequence.Item.builder()
                    .setReferencedSOPInstanceUID().asString("782583648368436.478754369.436487827")
                    .setReferencedSOPClassUID().asString("7827287.5634836.8463841.3684.3")
                    .setReferencedFrameNumber().asInts(0, 1, 2, 3, 4)
            )
    )
    .setDisplayedAreaSelectionSequence(
        DisplayedAreaSelectionSequence.Item.builder()
            .setDisplayedAreaTopLeftHandCorner().asInts(1, 1)
            .setDisplayedAreaBottomRightHandCorner().asInts(1920, 1080)
            .setPresentationPixelAspectRatio().asInts(1, 1)
    ).build();
```
### JavaDocs
Check out the javadocs. They should represent the stuff written in the DICOM Standard.
