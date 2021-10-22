# Typed Dicom
## How to build
Run ```.\gradlew jar``` the file is then located under ```typeddicom-lib/build/libs/typeddicom-lib.jar```
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

