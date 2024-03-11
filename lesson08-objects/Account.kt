class Account {
    companion object {
        /**
         * These are members of which class????
         * Which class is generated from the object use ???
         */
        var countInstances = 0
    }
    init {
        countInstances++
    }
}

fun main() {
    Account()
    Account()
    Account()
    println(Account.countInstances)
}
