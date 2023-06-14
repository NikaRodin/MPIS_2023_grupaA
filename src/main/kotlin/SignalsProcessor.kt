import java.lang.StringBuilder

class SignalsProcessor {

    var sviSignali: List<Signal> = listOf(
        Signal("mock", "aa", "aa", "aa", "aa", "bb", true) // TODO: remove mock
    )

    fun all(panel: MyPanel): String {
        return "Svi:\n" + toString(sviSignali)
    }

    fun toString(signals: List<Signal>): String {
        val sb = StringBuilder()
        for (s in signals) {
            sb.append(s.toString())
            sb.append("\n")
        }
        return sb.toString()
    }
}