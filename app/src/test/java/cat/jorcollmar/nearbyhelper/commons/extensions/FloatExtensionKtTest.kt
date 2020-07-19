package cat.jorcollmar.nearbyhelper.commons.extensions

import junit.framework.Assert.assertEquals
import org.junit.Test

class FloatExtensionKtTest {
    @Test
    fun `Given a Float round execution, When parameter is passed, Then Float is rounded with decimals given by parameter`() {
        assertEquals("1,13", 1.12f.round(2))
        assertEquals("1,13", 1.12345f.round(2))
        assertEquals("2,13", 2.12345f.round(2))
        assertEquals("2,13", 2.126f.round(2))
        assertEquals("2,13", 2.1210f.round(2))
        assertEquals("2,12", 2.1200f.round(2))
    }
}