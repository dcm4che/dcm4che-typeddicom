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
    implementation 'org.dcm4che:dcm4che-typeddicom:0.3.0-SNAPSHOT'
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
    implementation("org.dcm4che:dcm4che-typeddicom:0.3.0-SNAPSHOT")
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
    <version>0.3.0-SNAPSHOT</version>
</dependency>
```

## How to build it yourself
Run ```.\gradlew jar```. The JAR-file is then located under ```typeddicom-lib/build/libs/typeddicom-lib.jar```.

You can also run ```.\gradlew publishToMavenLocal``` to install it to your local Maven repository.
## How to use
### Reading fields
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

### Synthesizing Dicom Attributes
A fluent API was implemented to make DICOM attributes synthesizing easily read and writable.
Instead of
```
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
```
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

