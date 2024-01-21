package uz.sher.currency.util.resource

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Success<T : Any>(val data: T) : Resource<T>()
    class Network<T : Any>(val t: String) : Resource<T>()
    class Failure<T : Any>(val t: String) : Resource<T>()
}
