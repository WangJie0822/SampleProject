package cn.wj.android.base.liveevent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

object LiveDataBus {

    private val bus = mutableMapOf<String, BusLiveEvent<Any>>()

    @Synchronized
    fun <T> with(key: String): Observable<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusLiveEvent()
        }
        @Suppress("UNCHECKED_CAST")
        return bus[key] as Observable<T>
    }

//    fun with(key: String): Observable<Any> {
//        return with(key, Any::class.java)
//    }

    interface Observable<T> {
        fun setValue(value: T)

        fun postValue(value: T)

        fun observe(owner: LifecycleOwner, observer: Observer<T>)

        fun observeSticky(owner: LifecycleOwner, observer: Observer<T>)

        fun observeForever(observer: Observer<T>)

        fun observeStickyForever(observer: Observer<T>)

        fun removeObserver(observer: Observer<T>)
    }

    private class BusLiveEvent<T> : LiveEvent<T>(), Observable<T> {
//        override fun observerActiveLevel(): Lifecycle.State {
//            return super.observerActiveLevel()
//                        return Lifecycle.State.STARTED;
//        }
    }
}