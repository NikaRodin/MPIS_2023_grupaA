import java.lang.StringBuilder

data class Signal(
    val nazivEep: String,
    val naponskiNivo: String,
    val polje: String,
    val nazivUredaja: String,
    val varijabla: String,
    val stanje: String,
    val isActive: Boolean = true,
) {

    val eachItemCharWidth = listOf(9, 8, 9, 30, 30, 20)
    val eachItemCharWidthPrefixSum = ArrayList<Int>()

    init {
        var sum = 0
        for (element in eachItemCharWidth) {
            sum += element
            eachItemCharWidthPrefixSum.add(sum)
        }
    }

    override fun toString(): String {
        return createString(nazivEep, naponskiNivo, polje, nazivUredaja, varijabla, stanje)
    }

    fun createString(vararg values: String): String {
        val sb = StringBuilder()
        var index = 0
        for (v in values) {
            sb.append(v)
            sb.append(", ")
            sb.append(nSpaces((eachItemCharWidthPrefixSum[index]) - sb.length))
            index++
        }
        return sb.toString()
    }

    fun nSpaces(n: Int): String {
        assert(n >= 0)
        val sb = StringBuilder()
        repeat(n) { sb.append(" ") }
        return sb.toString()
    }
}