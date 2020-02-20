package util

interface Callback {
    fun notEmptyString(param: String)
}

fun notEmptyString(param: String?, callback: Callback) {
    if (!param.isNullOrEmpty()) {
        callback.notEmptyString(param!!)
    }
}