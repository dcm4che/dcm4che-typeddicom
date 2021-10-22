import com.agfa.typeddicom.dataelements.*;
import com.agfa.typeddicom.iods.CRImageIOD;
import com.agfa.typeddicom.iods.GrayscaleSoftcopyPresentationStateIOD;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SimpleTests {
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
    public void accessingValuesTheOldFashionedWayShouldYieldTheSameResults() throws IOException {
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
    public void afterSettingADoubleArrayItShouldContainTheNewValues() throws IOException {
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
}
