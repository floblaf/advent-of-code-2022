fun main() {
    abstract class Content {
        abstract val name: String
        abstract val size: Int
    }

    class Folder(
        override val name: String,
        val parent: Folder? = null,
        val content: MutableList<Content> = mutableListOf(),
    ) : Content() {

        override val size: Int
            get() = content.sumOf { it.size }

        override fun toString(): String {
            return "$name=$content"
        }
    }

    class File(override val name: String, override val size: Int) : Content() {
        override fun toString(): String {
            return name
        }
    }

    fun Folder.flattenFolders() : List<Folder> {
        return listOf(this) + this.content.filterIsInstance<Folder>().flatMap { it.flattenFolders() }
    }

    fun part1(input: Folder): Int {
        return input.flattenFolders().filter { it.size < 100_000 }.sumOf { it.size }
    }

    fun part2(input: Folder): Int {
        val neededSpace = 30_000_000 - (70_000_000 - input.size)
        return input.flattenFolders().sortedBy { it.size }
            .first { it.size > neededSpace }
            .size
    }

    val input = readInput("Day07")
    val root = Folder("/")
    var current = root

    fun moveTo(name: String) {
        current = current.content
            .filterIsInstance<Folder>()
            .find { it.name == name }
            ?: throw IllegalStateException("$name not found in ${current.name}")
    }

    fun addFolder(name: String) {
        current.content.add(Folder(name, parent = current))
    }

    fun addFile(name: String, size: Int) {
        current.content.add(File(name, size))
    }

    fun goBack() {
        current = current.parent ?: throw IllegalStateException("Can't go back from ${current.name}")
    }

    input.drop(1).forEach { line ->
        when {
            line == "$ ls" -> { /*nothing*/ }
            line == "$ cd .." -> goBack()
            line.startsWith("$ cd") -> moveTo(line.split(" ").last())
            line.startsWith("dir") -> addFolder(line.split(" ").last())
            else -> line.split(" ").let { addFile(it.last(), it.first().toInt()) }
        }
    }

    println(root)
    println(part1(root))
    println(part2(root))
}

