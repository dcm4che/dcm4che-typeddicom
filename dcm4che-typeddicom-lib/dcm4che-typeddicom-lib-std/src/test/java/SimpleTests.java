import org.dcm4che.typeddicom.UniversalAttributesWrapper;
import org.dcm4che.typeddicom.dataelements.DisplayedAreaSelectionSequence;
import org.dcm4che.typeddicom.dataelements.PixelAspectRatio;
import org.dcm4che.typeddicom.dataelements.ReferencedImageSequence;
import org.dcm4che.typeddicom.dataelements.ReferencedSeriesSequence;
import org.dcm4che.typeddicom.iods.CRImageIOD;
import org.dcm4che.typeddicom.iods.GrayscaleSoftcopyPresentationStateIOD;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTests {
    public static final String HPStateTag_CREATOR = "AGFA-AG_HPState";
    public static final int HPStateTag_ViewZoom = 0x00710024;
    public static final int HPStateTag_SpatialTransformSequence = 0x00710019;

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

    @SuppressWarnings("deprecation")
    private Attributes readDicomFile(String path) throws IOException {
        Attributes attributes;
        File dicomFile = new File(SimpleTests.class.getClassLoader().getResource(path).getFile());
        try (DicomInputStream dicomIn = new DicomInputStream(new FileInputStream(dicomFile))) {
            attributes = dicomIn.readDataset(-1, -1);
        }
        return attributes;
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
        gsps.getPatientName().setString("Niklas");
        gsps.getPatientName().setString("Roth");
        assertEquals("Roth", gsps.getPatientName().getString());
    }

    @Test
    void synthesizingGSPSWithFluentAPICreatesValidGSPS() throws IOException {
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
        
        assertEquals("Niklas", gsps.getPatientName().getString());
        int[] expectedFrameNumbers = {1, 2, 3, 4, 5};
        assertArrayEquals(expectedFrameNumbers,
                gsps.getReferencedSeriesSequence().get(0)
                        .getReferencedImageSequence().get(0)
                        .getReferencedFrameNumber().getInts()
        );
        
        assertEquals(gspsClassic, gsps.getAttributes());
    }
    
    @Test
    void settingCustomAttributesWithFluidBuilderAPIWorks() {
        final UniversalAttributesWrapper rendererEnvironment =
                UniversalAttributesWrapper.builder().setSequence(HPStateTag_CREATOR, HPStateTag_SpatialTransformSequence,
                        DisplayedAreaSelectionSequence.Holder.builder()
                                .setAttribute(HPStateTag_CREATOR, HPStateTag_ViewZoom, VR.FD).asDoubles(.1, .2)
                                .setDisplayedAreaSelectionSequence(DisplayedAreaSelectionSequence.Item.builder()
                                        .setReferencedImageSequence(ReferencedImageSequence.Item.builder()
                                                .setReferencedSOPClassUID().asString("1.1.1.1")
                                                .setReferencedSOPInstanceUID().asString("658416184.186.81.684.6816")
                                        )
                                        .setPresentationPixelAspectRatio().asInts(1, 1)
                                        .setDisplayedAreaTopLeftHandCorner().asInts(1, 1)
                                        .setDisplayedAreaBottomRightHandCorner().asInts(1920, 1080)
                                        .setPresentationSizeMode().asString("SCALE TO FIT")
                                )
                )
                .build();
        assertEquals(0.1, rendererEnvironment.getAttributes()
                .getSequence(HPStateTag_CREATOR, HPStateTag_SpatialTransformSequence)
                .get(0).getDouble(HPStateTag_CREATOR, HPStateTag_ViewZoom, VR.FD, 0, 0));
    }

    @Test
    void callingBuildOnHolderBuilderReturnsAValidObjectContainingTheAttribute() {
        int[] ratio = {1, 1};
        PixelAspectRatio.Holder.Builder pixelAspectRatioHolderBuilder = PixelAspectRatio.Holder.builder()
                .setPixelAspectRatio().asInts(ratio);
        assertArrayEquals(ratio, pixelAspectRatioHolderBuilder.build().getPixelAspectRatio().getInts());
    }
}
