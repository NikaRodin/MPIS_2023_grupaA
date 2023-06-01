abstract class SklopniUredaj(
    val id: String,
    var stanje: StanjeSklopnogUredaja,
) : ClickableElement {

    fun toggleStanje() {
        stanje = when (stanje) {
            StanjeSklopnogUredaja.ON -> StanjeSklopnogUredaja.OFF
            StanjeSklopnogUredaja.OFF -> StanjeSklopnogUredaja.ON
        }
    }

}

enum class StanjeSklopnogUredaja {
    ON, OFF
}