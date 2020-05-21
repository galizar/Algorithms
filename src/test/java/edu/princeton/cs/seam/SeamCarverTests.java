import edu.princeton.cs.algs4.Picture;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeamCarverTests {

    Picture twelveByTenPic = new Picture("seam/12x10.png");
    Picture threeByFourPic = new Picture("seam/3x4.png");
    Picture sixByFivePic = new Picture("seam/6x5.png");
    Picture fiveBySixPic = new Picture("seam/5x6.png");
    SeamCarver seam12By10 = new SeamCarver(twelveByTenPic);
    SeamCarver seam3By4 = new SeamCarver(threeByFourPic);

    @Test
    public void testIllegalNullArguments() {

        assertThrows(IllegalArgumentException.class,
                     () -> new SeamCarver(null));

        assertThrows(IllegalArgumentException.class,
                     () -> seam12By10.removeHorizontalSeam(null));

        assertThrows(IllegalArgumentException.class,
                     () -> seam12By10.removeVerticalSeam(null));
    }

    @Test
    public void testXOrYOutOfRange() {

        assertAll("energy method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.energy(-1, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.energy(12, 5)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.energy(5, -1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.energy(5, 10)));
    }

    @Test
    public void testIllegalSeamWithWrongLength() {

        // length of seam array must be the height
        assertAll("removeVerticalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(new int[11])),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(new int[5])),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(new int[0])));

        // length of seam array must be the width
        assertAll("removeHorizontalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(new int[13])),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(new int[5])),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(new int[0])));
    }

    @Test
    public void testIllegalSeamEntryOutsideRange() {

        int[] verticalSeam1 = new int[] {12, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] verticalSeam2 = new int[] {0, 1, 2, 3, 4, 12, 5, 6, 7, 8};
        int[] verticalSeam3 = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 12};

        int[] horizontalSeam1 = new int[] {10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
        int[] horizontalSeam2 = new int[] {0, 1, 2, 3, 4, 10, 5, 6, 7, 8, 9, 9};
        int[] horizontalSeam3 = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 10};

        assertAll("removeVerticalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam2)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam3)));

        assertAll("removeHorizontalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam2)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam3)));
    }

    @Test
    public void testIllegalSeamGapBetweenEntries() {

        int[] verticalSeam1 = new int[] {5, 7, 6, 6, 7, 8, 9, 10, 11, 10};
        int[] verticalSeam2 = new int[] {4, 5, 5, 6, 7, 5, 4, 3, 2, 3};
        int[] verticalSeam3 = new int[] {3, 2, 3, 2, 3, 4, 5, 6, 7, 9};

        int[] horizontalSeam1 = new int[] {9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 9};
        int[] horizontalSeam2 = new int[] {0, 1, 2, 3, 4, 9, 5, 6, 7, 8, 9, 9};
        int[] horizontalSeam3 = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 7, 9};

        assertAll("removeVerticalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam2)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeVerticalSeam(verticalSeam3)));

        assertAll("removeHorizontalSeam method calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam1)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam2)),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> seam12By10.removeHorizontalSeam(horizontalSeam3)));
    }

    @Test
    public void testIllegalSeamMethodsCallsSmallImage() {

        Picture narrowPicture = new Picture("seam/1x8.png");
        Picture shortPicture = new Picture("seam/8x1.png");

        SeamCarver narrowPicSeam = new SeamCarver(narrowPicture);
        SeamCarver shortPicSeam = new SeamCarver(shortPicture);

        assertAll("remove seam methods calls",
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> narrowPicSeam.removeVerticalSeam(new int[8])),
                  () -> assertThrows(IllegalArgumentException.class,
                                     () -> shortPicSeam.removeHorizontalSeam(new int[8])));
    }

    @Test
    public void testEnergyMethod() {

        assertEquals(1000.0, seam3By4.energy(0, 0), 0.001);
        assertEquals(1000.0, seam3By4.energy(0, 1), 0.001);
        assertEquals(1000.0, seam3By4.energy(0, 2), 0.001);
        assertEquals(1000.0, seam3By4.energy(0, 3), 0.001);

        assertEquals(1000.0, seam3By4.energy(2, 0), 0.001);
        assertEquals(1000.0, seam3By4.energy(2, 1), 0.001);
        assertEquals(1000.0, seam3By4.energy(2, 2), 0.001);
        assertEquals(1000.0, seam3By4.energy(2, 3), 0.001);

        assertEquals(1000.0, seam3By4.energy(1, 0), 0.001);
        assertEquals(1000.0, seam3By4.energy(1, 3), 0.001);

        assertEquals(228.0877, seam3By4.energy(1, 2), 0.001);
        assertEquals(228.5279, seam3By4.energy(1, 1), 0.001);
    }

    @Test
    public void testRemoveHorizontalSeamEvenWidthOddHeight() {

        int[] seam = new int[] {2, 2, 1, 2, 1, 1};
        SeamCarver carver = new SeamCarver(sixByFivePic);

        assertEquals(5, carver.height(), "before removal picture should have height of 5");
        carver.removeHorizontalSeam(seam);
        assertEquals(4, carver.height(), "after removal picture should have height of 4");

        Picture afterPicture = new Picture(6, 4);

        // 1st row
        afterPicture.setRGB(0, 0, sixByFivePic.getRGB(0,0));
        afterPicture.setRGB(1, 0, sixByFivePic.getRGB(1,0));
        afterPicture.setRGB(2, 0, sixByFivePic.getRGB(2,0));
        afterPicture.setRGB(3, 0, sixByFivePic.getRGB(3,0));
        afterPicture.setRGB(4, 0, sixByFivePic.getRGB(4,0));
        afterPicture.setRGB(5, 0, sixByFivePic.getRGB(5,0));

        // 2nd
        afterPicture.setRGB(0, 1, sixByFivePic.getRGB(0,1));
        afterPicture.setRGB(1, 1, sixByFivePic.getRGB(1,1));
        afterPicture.setRGB(2, 1, sixByFivePic.getRGB(2,2));
        afterPicture.setRGB(3, 1, sixByFivePic.getRGB(3,1));
        afterPicture.setRGB(4, 1, sixByFivePic.getRGB(4,2));
        afterPicture.setRGB(5, 1, sixByFivePic.getRGB(5,2));

        // 3rd
        afterPicture.setRGB(0, 2, sixByFivePic.getRGB(0,3));
        afterPicture.setRGB(1, 2, sixByFivePic.getRGB(1,3));
        afterPicture.setRGB(2, 2, sixByFivePic.getRGB(2,3));
        afterPicture.setRGB(3, 2, sixByFivePic.getRGB(3,3));
        afterPicture.setRGB(4, 2, sixByFivePic.getRGB(4,3));
        afterPicture.setRGB(5, 2, sixByFivePic.getRGB(5,3));

        // 4th
        afterPicture.setRGB(0, 3, sixByFivePic.getRGB(0,4));
        afterPicture.setRGB(1, 3, sixByFivePic.getRGB(1,4));
        afterPicture.setRGB(2, 3, sixByFivePic.getRGB(2,4));
        afterPicture.setRGB(3, 3, sixByFivePic.getRGB(3,4));
        afterPicture.setRGB(4, 3, sixByFivePic.getRGB(4,4));
        afterPicture.setRGB(5, 3, sixByFivePic.getRGB(5,4));

        assertEquals(afterPicture, carver.picture());
    }

    @Test
    public void testRemoveHorizontalSeamOddWidthEvenHeight() {

        int[] seam = new int[] {0, 1, 2, 3, 4};
        SeamCarver carver = new SeamCarver(fiveBySixPic);

        assertEquals(6, carver.height(), "before removal picture should have height of 6");
        carver.removeHorizontalSeam(seam);
        assertEquals(5, carver.height(), "after removal picture should have height of 5");

        Picture afterPicture = new Picture(5, 5);

        // 1st row
        afterPicture.setRGB(0, 0, fiveBySixPic.getRGB(0,1));
        afterPicture.setRGB(1, 0, fiveBySixPic.getRGB(1,0));
        afterPicture.setRGB(2, 0, fiveBySixPic.getRGB(2,0));
        afterPicture.setRGB(3, 0, fiveBySixPic.getRGB(3,0));
        afterPicture.setRGB(4, 0, fiveBySixPic.getRGB(4,0));

        // 2nd
        afterPicture.setRGB(0, 1, fiveBySixPic.getRGB(0,2));
        afterPicture.setRGB(1, 1, fiveBySixPic.getRGB(1,2));
        afterPicture.setRGB(2, 1, fiveBySixPic.getRGB(2,1));
        afterPicture.setRGB(3, 1, fiveBySixPic.getRGB(3,1));
        afterPicture.setRGB(4, 1, fiveBySixPic.getRGB(4,1));

        // 3rd
        afterPicture.setRGB(0, 2, fiveBySixPic.getRGB(0,3));
        afterPicture.setRGB(1, 2, fiveBySixPic.getRGB(1, 3));
        afterPicture.setRGB(2, 2, fiveBySixPic.getRGB(2,3));
        afterPicture.setRGB(3, 2, fiveBySixPic.getRGB(3,2));
        afterPicture.setRGB(4, 2, fiveBySixPic.getRGB(4,2));

        // 4th
        afterPicture.setRGB(0, 3, fiveBySixPic.getRGB(0,4));
        afterPicture.setRGB(1, 3, fiveBySixPic.getRGB(1, 4));
        afterPicture.setRGB(2, 3, fiveBySixPic.getRGB(2,4));
        afterPicture.setRGB(3, 3, fiveBySixPic.getRGB(3,4));
        afterPicture.setRGB(4, 3, fiveBySixPic.getRGB(4,3));

        // 5th
        afterPicture.setRGB(0, 4, fiveBySixPic.getRGB(0,5));
        afterPicture.setRGB(1, 4, fiveBySixPic.getRGB(1, 5));
        afterPicture.setRGB(2, 4, fiveBySixPic.getRGB(2,5));
        afterPicture.setRGB(3, 4, fiveBySixPic.getRGB(3,5));
        afterPicture.setRGB(4, 4, fiveBySixPic.getRGB(4,5));

        assertEquals(afterPicture, carver.picture());
    }

    @Test
    public void testCorrectEnergyAfterHorizontalSeamRemoval() {
        int[] seam = new int[] {2, 2, 1, 2, 1, 1};
        SeamCarver carver = new SeamCarver(sixByFivePic);
        carver.removeHorizontalSeam(seam);

        assertEquals(1000.0, carver.energy(0, 0), 0);
        assertEquals(1000.0, carver.energy(1, 0), 0);
        assertEquals(1000.0, carver.energy(2, 0), 0);
        assertEquals(1000.0, carver.energy(3, 0), 0);
        assertEquals(1000.0, carver.energy(4, 0), 0);
        assertEquals(1000.0, carver.energy(5, 0), 0);

        assertEquals(1000.0, carver.energy(0, 1), 0);
        assertEquals(161.31, carver.energy(1, 1), 0.01);
        assertEquals(125.23, carver.energy(2, 1), 0.01);
        assertEquals(167.81, carver.energy(3, 1), 0.01);
        assertEquals(135.5, carver.energy(4, 1), 0.01);
        assertEquals(1000.0, carver.energy(5, 1), 0);

        assertEquals(1000.0, carver.energy(0, 2), 0);
        assertEquals(253.42, carver.energy(1, 2), 0.01);
        assertEquals(174.00, carver.energy(2, 2), 0.01);
        assertEquals(227.48, carver.energy(3, 2), 0.01);
        assertEquals(194.5, carver.energy(4, 2), 0.01);
        assertEquals(1000.0, carver.energy(5, 2), 0);

        assertEquals(1000.0, carver.energy(0, 3), 0);
        assertEquals(1000.0, carver.energy(1, 3), 0);
        assertEquals(1000.0, carver.energy(2, 3), 0);
        assertEquals(1000.0, carver.energy(3, 3), 0);
        assertEquals(1000.0, carver.energy(4, 3), 0);
        assertEquals(1000.0, carver.energy(5, 3), 0);
    }

    @Test
    public void testRemoveVerticalSeamMethodEvenWidthOddHeight() {

        int[] seam = new int[] {3, 2, 1, 2, 3};
        SeamCarver carver = new SeamCarver(sixByFivePic);

        assertEquals(6, carver.width(), "picture width must be 6 before removal");
        carver.removeVerticalSeam(seam);
        assertEquals(5, carver.width(), "picture width must be 5 after removal");

        Picture afterPicture = new Picture(5, 5);

        // 1st row
        afterPicture.setRGB(0, 0, sixByFivePic.getRGB(0,0));
        afterPicture.setRGB(1, 0, sixByFivePic.getRGB(1,0));
        afterPicture.setRGB(2, 0, sixByFivePic.getRGB(2,0));
        afterPicture.setRGB(3, 0, sixByFivePic.getRGB(4,0));
        afterPicture.setRGB(4, 0, sixByFivePic.getRGB(5,0));

        // 2nd
        afterPicture.setRGB(0, 1, sixByFivePic.getRGB(0,1));
        afterPicture.setRGB(1, 1, sixByFivePic.getRGB(1,1));
        afterPicture.setRGB(2, 1, sixByFivePic.getRGB(3,1));
        afterPicture.setRGB(3, 1, sixByFivePic.getRGB(4,1));
        afterPicture.setRGB(4, 1, sixByFivePic.getRGB(5,1));

        // 3rd
        afterPicture.setRGB(0, 2, sixByFivePic.getRGB(0,2));
        afterPicture.setRGB(1, 2, sixByFivePic.getRGB(2, 2));
        afterPicture.setRGB(2, 2, sixByFivePic.getRGB(3,2));
        afterPicture.setRGB(3, 2, sixByFivePic.getRGB(4,2));
        afterPicture.setRGB(4, 2, sixByFivePic.getRGB(5,2));

        // 4th
        afterPicture.setRGB(0, 3, sixByFivePic.getRGB(0,3));
        afterPicture.setRGB(1, 3, sixByFivePic.getRGB(1, 3));
        afterPicture.setRGB(2, 3, sixByFivePic.getRGB(3,3));
        afterPicture.setRGB(3, 3, sixByFivePic.getRGB(4,3));
        afterPicture.setRGB(4, 3, sixByFivePic.getRGB(5,3));

        // 5th
        afterPicture.setRGB(0, 4, sixByFivePic.getRGB(0,4));
        afterPicture.setRGB(1, 4, sixByFivePic.getRGB(1, 4));
        afterPicture.setRGB(2, 4, sixByFivePic.getRGB(2,4));
        afterPicture.setRGB(3, 4, sixByFivePic.getRGB(4,4));
        afterPicture.setRGB(4, 4, sixByFivePic.getRGB(5,4));

        assertEquals(afterPicture, carver.picture());
    }

    @Test
    public void testRemoveVerticalSeamOddWidthEvenHeight() {

        int[] seam = new int[] {3, 2, 1, 2, 3, 3};
        SeamCarver carver = new SeamCarver(fiveBySixPic);

        assertEquals(5, carver.width(), "picture width must be 5 before removal");
        carver.removeVerticalSeam(seam);
        assertEquals(4, carver.width(), "picture width must be 4 after removal");

        Picture afterPicture = new Picture(4, 6);

        // 1st row
        afterPicture.setRGB(0, 0, fiveBySixPic.getRGB(0,0));
        afterPicture.setRGB(1, 0, fiveBySixPic.getRGB(1,0));
        afterPicture.setRGB(2, 0, fiveBySixPic.getRGB(2,0));
        afterPicture.setRGB(3, 0, fiveBySixPic.getRGB(4,0));

        // 2nd
        afterPicture.setRGB(0, 1, fiveBySixPic.getRGB(0,1));
        afterPicture.setRGB(1, 1, fiveBySixPic.getRGB(1,1));
        afterPicture.setRGB(2, 1, fiveBySixPic.getRGB(3,1));
        afterPicture.setRGB(3, 1, fiveBySixPic.getRGB(4,1));

        // 3rd
        afterPicture.setRGB(0, 2, fiveBySixPic.getRGB(0,2));
        afterPicture.setRGB(1, 2, fiveBySixPic.getRGB(2,2));
        afterPicture.setRGB(2, 2, fiveBySixPic.getRGB(3,2));
        afterPicture.setRGB(3, 2, fiveBySixPic.getRGB(4,2));

        // 4th
        afterPicture.setRGB(0, 3, fiveBySixPic.getRGB(0,3));
        afterPicture.setRGB(1, 3, fiveBySixPic.getRGB(1,3));
        afterPicture.setRGB(2, 3, fiveBySixPic.getRGB(3,3));
        afterPicture.setRGB(3, 3, fiveBySixPic.getRGB(4,3));

        // 5th
        afterPicture.setRGB(0, 4, fiveBySixPic.getRGB(0,4));
        afterPicture.setRGB(1, 4, fiveBySixPic.getRGB(1,4));
        afterPicture.setRGB(2, 4, fiveBySixPic.getRGB(2,4));
        afterPicture.setRGB(3, 4, fiveBySixPic.getRGB(4,4));

        // 6th
        afterPicture.setRGB(0, 5, fiveBySixPic.getRGB(0,5));
        afterPicture.setRGB(1, 5, fiveBySixPic.getRGB(1,5));
        afterPicture.setRGB(2, 5, fiveBySixPic.getRGB(2,5));
        afterPicture.setRGB(3, 5, fiveBySixPic.getRGB(4,5));
        
        assertEquals(afterPicture, carver.picture());
    }

    @Test
    public void testFindHorizontalSeam() {

        SeamCarver carver = new SeamCarver(sixByFivePic);
        int[] seam = carver.findHorizontalSeam();

        int[] expectedSeam = new int[] {2, 2, 1, 2, 1, 0};

        assertTrue(Arrays.equals(expectedSeam, seam));
    }

    @Test
    public void testFindVerticalSeam() {

        SeamCarver carver = new SeamCarver(sixByFivePic);
        int[] seam = carver.findVerticalSeam();

        int[] expectedSeam = new int[] {5, 4, 3, 2, 3};

        assertTrue(Arrays.equals(expectedSeam, seam));
    }
}
