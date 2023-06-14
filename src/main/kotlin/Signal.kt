data class Signal(
    val nazivEep: String,
    val naponskiNivo: String,
    val polje: String,
    val nazivUredaja: String,
    val varijabla: String,
    val stanje: String,
    val isActive: Boolean,
) {
    override fun toString(): String {
        val separator = ", "
        return "$nazivEep$separator$naponskiNivo$separator$polje$separator$nazivUredaja$separator$varijabla$separator$stanje"
    }
}