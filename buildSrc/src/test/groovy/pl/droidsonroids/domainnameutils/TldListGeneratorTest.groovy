package pl.droidsonroids.domainnameutils

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertTrue

public class TldListGeneratorTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder()

    @Test
    public void testSavedSource() {
        def savedSourceTxt = TldListGenerator.class.getResource('tlds-alpha-by-domain.txt').text
        assertTrue(savedSourceTxt.startsWith('#'))
        assertTrue(savedSourceTxt.contains('COM'))
    }

    @Test
    public void testGenerator() {
        def outputDir = tempFolder.newFolder()
        TldListGenerator.generateTldListClass(outputDir.getPath(), true)
        def generatedFile = new File(outputDir, 'pl/droidsonroids/domainnameutils/TldList.java')
        assertTrue(generatedFile.isFile())
    }
}