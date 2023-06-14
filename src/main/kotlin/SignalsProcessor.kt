import java.lang.StringBuilder

class SignalsProcessor {

    fun all(panel: MyPanel): String {
        return "Svi\n" + toString(getlAllSignals(panel))
    }

    fun toString(signals: List<Signal>): String {
        val sb = StringBuilder()
        for (s in signals) {
            sb.append(s.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun getlAllSignals(panel: MyPanel): List<Signal> {
        val signals = ArrayList<Signal>()
        signals.addAll(panel.dalPolje1.getSignals())
        signals.addAll(panel.dalPolje2.getSignals())
        signals.addAll(panel.mjernoPolje.getSignals())
        return signals
    }
}