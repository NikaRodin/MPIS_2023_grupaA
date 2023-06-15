class SignalsProcessor {

    fun getAllSignals(panel: MyPanel): List<Signal> {
        val signals = ArrayList<Signal>()
        signals.addAll(panel.dalPolje1.getSignals())
        signals.addAll(panel.dalPolje2.getSignals())
        signals.addAll(panel.mjernoPolje.getSignals())
        return signals
    }
}