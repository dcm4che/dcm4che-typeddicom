import org.dcm4che.typeddicom.dataelements.DisplayedAreaSelectionSequence;
import org.dcm4che.typeddicom.dataelements.ReferencedImageSequence;
import org.dcm4che.typeddicom.dataelements.ReferencedSeriesSequence;
import org.dcm4che.typeddicom.iods.CRImageIOD;
import org.dcm4che.typeddicom.iods.GrayscaleSoftcopyPresentationStateIOD;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.dcm4che3.data.Tag.ReferencedSeriesSequence;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTests {
    @SuppressWarnings("deprecation")
    private Attributes readDicomFile(String path) throws IOException {
        Attributes attributes;
        File dicomFile = new File(SimpleTests.class.getClassLoader().getResource(path).getFile());
        try (DicomInputStream dicomIn = new DicomInputStream( new FileInputStream( dicomFile ) ))
        {
            attributes = dicomIn.readDataset( -1, -1 );
        }
        return attributes;
    }

    @Test
    void accessingValuesTheOldFashionedWayShouldYieldTheSameResults() throws IOException {
        Attributes attributes = readDicomFile("GSPS.dcm");

        GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD(attributes);
        int[] typedDicomPixelAspectRatio = gsps.getDisplayedAreaSelectionSequence()
                .get(0)
                .getPresentationPixelAspectRatio()
                .getInts();

        int[] untypedPixelAspectRatio = attributes.getSequence(Tag.DisplayedAreaSelectionSequence)
                .get(0)
                .getInts(Tag.PresentationPixelAspectRatio);

        assertArrayEquals(untypedPixelAspectRatio, typedDicomPixelAspectRatio, "Ints must be the same if retrieved by the typed methods");
    }

    @Test
    void afterSettingADoubleArrayItShouldContainTheNewValues() throws IOException {
        GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD(readDicomFile("GSPS.dcm"));
        CRImageIOD crImage = new CRImageIOD(readDicomFile("CRImage_PS.dcm"));
        double[] pixelSpacing = crImage.getPixelSpacing().getDoubles();
        for (DisplayedAreaSelectionSequence.Item item : gsps.getDisplayedAreaSelectionSequence()) {
            item.getPresentationPixelSpacing().setDoubles(pixelSpacing);
        }
        for (DisplayedAreaSelectionSequence.Item item : gsps.getDisplayedAreaSelectionSequence()) {
            assertArrayEquals(item.getPresentationPixelSpacing().getDoubles(), pixelSpacing);
        }
    }

    @Test
    void afterSettingAPatientNameTwiceItShouldContainTheLastValue() throws IOException {
        GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD(readDicomFile("GSPS.dcm"));
        gsps.getPatientNameSetter().setString("Niklas")
                .getPatientNameSetter().setString("Roth");
        assertEquals("Roth", gsps.getPatientName().getString());
    }
    
    @Test
    void synthesizingGSPSWithFluentAPICreatesValidGSPS() throws IOException {
        GrayscaleSoftcopyPresentationStateIOD gsps = new GrayscaleSoftcopyPresentationStateIOD()
                .getPatientNameSetter().setString("Niklas")
                .appendReferencedSeriesSequence(
                        new ReferencedSeriesSequence.Item()
                                .getSeriesInstanceUIDSetter().setString("1234567890.345678.3456789")
                                .appendReferencedImageSequence(
                                        new ReferencedImageSequence.Item()
                                                .getReferencedSOPInstanceUIDSetter().setString("3483648368436.483.864369.43648.368")
                                                .getReferencedSOPClassUIDSetter().setString("8646.36.54186.86408684371")
                                                .getReferencedFrameNumberSetter().setInts(1,2,3,4,5),
                                        new ReferencedImageSequence.Item()
                                                .getReferencedSOPInstanceUIDSetter().setString("782583648368436.478754369.436487827")
                                                .getReferencedSOPClassUIDSetter().setString("7827287.5634836.8463841.3684.3")
                                                .getReferencedFrameNumberSetter().setInts(0,1,2,3,4)
                                )
                )
                .appendDisplayedAreaSelectionSequence(
                        new DisplayedAreaSelectionSequence.Item()
                                .getDisplayedAreaTopLeftHandCornerSetter().setInts(1, 1)
                                .getDisplayedAreaBottomRightHandCornerSetter().setInts(1920, 1080)
                                .getPresentationPixelAspectRatioSetter().setInts(1, 1)
                );
        assertEquals("Niklas", gsps.getPatientName().getString());
        int[] expectedFrameNumbers = {1, 2, 3, 4, 5};
        assertArrayEquals(expectedFrameNumbers,
                gsps.getReferencedSeriesSequence().get(0)
                        .getReferencedImageSequence().get(0)
                        .getReferencedFrameNumber().getInts()
        );
    }
}
