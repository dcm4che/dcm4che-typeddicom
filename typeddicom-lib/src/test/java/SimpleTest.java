import com.agfa.typeddicom.dataelements.DisplayedAreaSelectionSequence;
import com.agfa.typeddicom.dataelements.PresentationPixelAspectRatio;
import com.agfa.typeddicom.dataelements.PresentationPixelSpacing;
import com.agfa.typeddicom.iods.GrayscaleSoftcopyPresentationStateIOD;
import com.agfa.typeddicom.modules.DisplayedAreaModule;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleTest {
    private Attributes readDicomFile(String path) throws IOException {
        Attributes attributes;
        File dicomFile = new File(SimpleTest.class.getClassLoader().getResource(path).getFile());
        try (DicomInputStream dicomIn = new DicomInputStream( new FileInputStream( dicomFile ) ))
        {
            attributes = dicomIn.readDataset( -1, -1 );
        }
        return attributes;
    }

    @Test
    public void testSimpleStuff() throws IOException {
        Attributes attributes = readDicomFile("GSPS.dcm");
        GrayscaleSoftcopyPresentationStateIOD grayscaleSoftcopyPresentationState = new GrayscaleSoftcopyPresentationStateIOD(attributes);
        System.out.println(grayscaleSoftcopyPresentationState.getSOPClassUID().getString());
        DisplayedAreaSelectionSequence displayedAreaSelectionSequence = grayscaleSoftcopyPresentationState.getDisplayedAreaSelectionSequence();
        DisplayedAreaSelectionSequence.Item displayedAreaSelectionSequenceItem = displayedAreaSelectionSequence.get(0);
        PresentationPixelAspectRatio presentationPixelAspectRatio = displayedAreaSelectionSequenceItem.getPresentationPixelAspectRatio();
        int[] par = presentationPixelAspectRatio.getInts();
    }
}
