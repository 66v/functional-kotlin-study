package com.bhbac.kotlinstudysample

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.sql.Time
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

fun main() {
    println(">>>>>>>>>> 8장 테스트 시작 <<<<<<<<<<")

    val list: List<Any> = listOf(1, "듈", 3, "넷", "다섯", 5.5f)
    val iterator = list.iterator()
    while (iterator.hasNext()) {
        println(iterator.next())
    }

    val observable = list.toObservable()
    observable.subscribeBy(
            onNext = { println(it) },
            onError = { it.printStackTrace() },
            onComplete = { println("완료!") }
    )

    val observer = object : Observer<Any> {
        override fun onComplete() {
            println("모두 완료됨")
        }

        override fun onSubscribe(d: Disposable) {
            println("$d 구독됨")
        }

        override fun onNext(t: Any) {
            println("다음 $t")
        }

        override fun onError(e: Throwable) {
            println("에러 발생 $e")
        }
    }
    observable.subscribe(observer)
    val observableOnList = Observable.just(list,
            listOf("아이템 1개 짜리 리스트"),
            listOf(1, 2, 3))
    observableOnList.subscribe(observer)

    val observerString = object : Observer<String> {
        override fun onComplete() {
            println("모두 완료됨")
        }

        override fun onSubscribe(d: Disposable) {
            println("새로운 구독")
        }

        override fun onNext(t: String) {
            println("다음 -> $t")
        }

        override fun onError(e: Throwable) {
            println("에러 발생 => ${e.message}")
        }
    }
    val observableString = Observable.create<String> {
        it.onNext("방출됨 1")
        it.onNext("방출됨 2")
        it.onNext("방출됨 3")
        it.onNext("방출됨 4")
        it.onComplete()
    }
    observableString.subscribe(observerString)
    val observableStringError = Observable.create<String> {
        it.onNext("방출됨 1")
        it.onNext("방출됨 2")
        it.onError(Exception("아이구야 에러가 났네요호!"))
    }
    observableStringError.subscribe(observerString)

    val listString = listOf("Str 1", "Str 2", "Str 3", "Str 4")
    val observableFromIterable: Observable<String> = Observable.fromIterable(listString)
    observableFromIterable.subscribe(observerString)

    val callable = object : Callable<String> {
        override fun call(): String {
            return "Callable에서 왔소이다!"
        }
    }
    val observableFromCallable = Observable.fromCallable(callable)
    observableFromCallable.subscribe(observerString)

    val future = object : Future<String> {
        val retStr = "Future에서 왓쏘이다!!"

        override fun isDone(): Boolean = true
        override fun get(): String = retStr
        override fun get(timeout: Long, unit: TimeUnit?): String = retStr
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false
        override fun isCancelled(): Boolean = false
    }
    val observableFromFuture = Observable.fromFuture(future)
    observableFromFuture.subscribe(observerString)

    val observale = Observable.interval(100, TimeUnit.MILLISECONDS)
    val observerLong = object : Observer<Long> {
        lateinit var disposable: Disposable

        override fun onComplete() {
            println("완료")
        }

        override fun onSubscribe(d: Disposable) {
            println("observale 구독 시작됨!")
            disposable = d
        }

        override fun onNext(t: Long) {
            println("받음 $t")
            if (t >= 10 && !disposable.isDisposed) {
                disposable.dispose()
                println("정리됨")
            }
        }

        override fun onError(e: Throwable) {
            println("에러 ${e.message}")
        }
    }
    runBlocking {
        observale.subscribe(observerLong)
        delay(1500)
    }

    println(">>>>>>>>>> 8장 테스트 종료 <<<<<<<<<<")
}