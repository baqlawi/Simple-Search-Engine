package search

import java.io.File
import kotlin.system.exitProcess
var fileName = ""

fun main(args: Array<String>) {
    fileName = args[args.indexOf("--data") + 1]
    People
}

object People {



    object Menu {
        private var selectedOption: MenuOption
        init {
            do {
                selectedOption = showMenu()
            } while (selectedOption != MenuOption.Exit)
        }

        enum class MenuOption(val title: String) {
            Exit("Exit"),
            FIND_A_PERSON("Find a person"),
            PRINT_ALL_PEOPLE("Print all people");

            companion object {
                fun byOrdinal(ordinal: Int): MenuOption {
                    return values()[ordinal]
                }
            }
        }

        private fun callForOption() = readln().toInt()

        private fun showMenu(selectIt: Boolean = true): MenuOption {
            val menuUpperBound = MenuOption.values().lastIndex
            var option = printMenu()

            while (option !in 0..menuUpperBound) {
                println("Incorrect option! Try again.")
                option = printMenu()
            }
            val selected = processMenuOption(option)
            if (selectIt) {
                selectedOption = selected
            }
            return selected
        }

        private fun printMenu(): Int {
            println("=== Menu ===")
            val menuOptions = MenuOption.values().drop(1) + MenuOption.Exit
            menuOptions.forEach { option -> println("${option.ordinal}. ${option.title}") }

            return callForOption()
        }

        private fun processMenuOption(option: Int): MenuOption {
            val selectedOption = MenuOption.byOrdinal(option)
            when (selectedOption) {
                MenuOption.Exit -> {
                    println("Bye!")
                    exitProcess(0)
                }
                MenuOption.FIND_A_PERSON -> findAPerson()
                MenuOption.PRINT_ALL_PEOPLE -> printAll()
            }
            return selectedOption
        }

    }

    private val invertedIndexSearch = mutableMapOf<String, MutableList<Int>>()
    private val list:List<String> = File(fileName).readLines()

    init {
//        println("Enter the number of people:")
//        val peopleCount = readln().toInt()
//        println("Enter all people:")
//        list = List(peopleCount) { readln() }
        list.forEachIndexed { idx, line ->
            line.split(" ").forEach {
                invertedIndexSearch.merge(it, mutableListOf(idx)) { oldIndices, newIndices -> (oldIndices + newIndices).toMutableList() }
            }
        }

        showMenu()
    }

    private fun showMenu() {
        Menu
    }

    /*val DEFAULT_PRINT_FOUND_PEOPLE =  { foundList:List<String> ->
        println(
            if (foundList.isEmpty()) {
                "No matching people found."
            } else {
                "People found:\n${foundList.joinToString("\n")}"
            }
        )
    }
    private val DEFAULT_PRINT_FOUND_PEOPLE_Stage5 =  { foundList:List<String> ->
        println(
            if (foundList.isEmpty()) {
                "No matching people found."
            } else {
                "${foundList.size} person found:\n${foundList.forEach(::println)}"
            }
        )
    }*/

    /*fun runQuery() {
        val querySize = promptForQuerySize()
        repeat(querySize) { search() }
    }

    private fun promptForQuerySize(): Int {
        println("Enter the number of search queries:")
        return readln().toInt()
    }*/

    /*private fun linearSearch(query: String): List<String> {
        val regex = Regex("\\b$query\\b", RegexOption.IGNORE_CASE)
//        val regex = Regex("$query", RegexOption.IGNORE_CASE)
        val found = list.filter { line->
            line.contains(regex)
        }
        return found
    }

    private fun invertedIndexSearch(query: String) = invertedIndexSearch[query]*/

    private fun searchByStrategy(query: String, strategy: Strategy = Strategy.ALL): List<String> {
        val words = query.split(" ")
        val result = when (strategy) {
            Strategy.ALL -> list.filter { line -> words.all { word -> line.contains(Regex("\\b$word\\b", RegexOption.IGNORE_CASE)) } }
            Strategy.ANY -> list.filter { line -> words.any { word -> line.contains(Regex("\\b$word\\b", RegexOption.IGNORE_CASE)) } }
            Strategy.NONE -> list.filter { line -> words.none { word -> line.contains(Regex("\\b$word\\b", RegexOption.IGNORE_CASE)) } }
        }
        return result
    }

    private fun search(strategy: Strategy = Strategy.ALL,
                       action: (List<String>) -> Unit ) {
//                       action: (List<Int>?) -> Unit ) {
//                       action: (List<String>) -> Unit = DEFAULT_PRINT_FOUND_PEOPLE ) {

        println("Enter a name or email to search all matching people.")
        val query = readln()
        val result = searchByStrategy(query, strategy)
//        val result = invertedIndexSearch(query)
//        val result = linearSearch(query)
        action(result)
    }

    fun printAll() {
        list.forEach(::println)
    }


    fun findAPerson() {
        println("Select a matching strategy: ALL, ANY, NONE")
        val strategy = Strategy.valueOf(readln())
//        search("Enter a name or email to search all suitable people.") { found -> found.forEach(::println) }
        search(strategy) { found:List<String> ->
            if (found.isEmpty()) {
                println("No matching people found.")
            } else {
                println("${found.size} persons found:")
                found.forEach(::println)
            }
        }
    }

    enum class Strategy {ALL, ANY, NONE}

}


/*fun search() {
    People.promptForInputs()
    People.runQuery()
}*/
