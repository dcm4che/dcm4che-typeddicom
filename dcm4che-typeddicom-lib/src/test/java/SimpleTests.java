import org.dcm4che.typeddicom.dataelements.DisplayedAreaSelectionSequence;
import org.dcm4che.typeddicom.iods.CRImageIOD;
import org.dcm4che.typeddicom.iods.GrayscaleSoftcopyPresentationStateIOD;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
}
