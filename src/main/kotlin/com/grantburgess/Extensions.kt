package com.grantburgess

inline fun <T> Iterable<T>.takeAndAccumulateUntil(value: Long, selector: (T) -> Float): Pair<Boolean, List<T>> {
    val list = mutableListOf<T>()
    var sum = 0f
    var conditionMet = false

    for (element in this) {
        list.add(element)
        sum += selector(element)

        if (sum >= value) {
            conditionMet = true
            break
        }
    }

    return Pair<Boolean, List<T>>(conditionMet, list)
}