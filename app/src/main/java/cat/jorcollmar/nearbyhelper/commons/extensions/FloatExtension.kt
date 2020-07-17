package cat.jorcollmar.nearbyhelper.commons.extensions

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Round the Float (CEILING Mode) to 'decimals' places
 *
 * @param decimals Number of decimals places
 */
fun Float.round(decimals: Int): String {
    val pattern = "#.##"
    for (decimal in 2..decimals) {
        pattern.plus("#")
    }

    return DecimalFormat(pattern).apply { roundingMode = RoundingMode.CEILING }.format(this)
}