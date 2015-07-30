package pl.droidsonroids.domainnameutils

import org.junit.Test

import static org.junit.Assert.assertTrue;

public class TldListGeneratorTest {

    @Test
    public void test() {
        def savedSourceTxt = TldListGenerator.class.getResource('tlds-alpha-by-domain.txt').text
        assertTrue(savedSourceTxt.startsWith('#'))
        assertTrue(savedSourceTxt.contains('COM'))
    }
}