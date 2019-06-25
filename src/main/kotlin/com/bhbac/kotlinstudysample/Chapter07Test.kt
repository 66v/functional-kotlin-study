/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

const val numThreads = 10
const val threadWorkTime = 1000L

suspend fun main() = runBlocking {
    println(">>>>> 7장 테스트 시작 <<<<<")
    val worldCallTime = 100L
    val worldWaitTime = 250L

    var startTime: Long by Delegates.notNull()
    thread {
        Thread.sleep(worldCallTime)
        println("World!")
    }
    print("Hello ")
    startTime = System.currentTimeMillis()
    Thread.sleep(worldWaitTime)
    println("ElapsedTime 1 : ${System.currentTimeMillis() - startTime}")

    val computation = thread {
        Thread.sleep(worldCallTime)
        println("World!")
    }
    print("Hello ")
    startTime = System.currentTimeMillis()
    computation.join()
    println("ElapsedTime 2 : ${System.currentTimeMillis() - startTime}")

    println("CurrentThread :${Thread.currentThread()}")

    startTime = System.currentTimeMillis()
    val threads = List(numThreads) {
        thread {
            println("CurrentThread :${Thread.currentThread()}")
            Thread.sleep(threadWorkTime)
            print('.')
        }
    }
    threads.forEach(Thread::join)
    println("\nElasped Time 3 : ${System.currentTimeMillis() - startTime}")

    val executor = Executors.newFixedThreadPool(numThreads / 3)
    startTime = System.currentTimeMillis()
    repeat(numThreads) {
        executor.submit {
            println("CurrentThread :${Thread.currentThread()}")
            Thread.sleep(threadWorkTime)
            print('.')
        }
    }
    executor.shutdown()
    executor.awaitTermination(20, TimeUnit.SECONDS)
    println("\nElasped Time 4 : ${System.currentTimeMillis() - startTime}")

    startTime = System.currentTimeMillis()
    val jobs = mutableListOf<Job>()
    repeat(numThreads) {
        val job = launch {
            println("${Thread.currentThread()} : ${this@launch}")
            delay(threadWorkTime)
            print('.')
        }
        jobs.add(job)
    }
    println("\nElasped Time 5 : ${System.currentTimeMillis() - startTime}")
    startTime = System.currentTimeMillis()
    jobs.forEach { it.join() }
    println("\nElasped Time 5 - end : ${System.currentTimeMillis() - startTime}")

//        launch {
//            delay(threadWorkTime)
//            println("World")
//        }
//        print("Hello ")
//        delay(threadWorkTime * 2)

    println("\n\n========== SynchronousUserService ==========\n")
    val userServiceStartTimeMs = System.currentTimeMillis()
    fun execute(userService: UserService, id: UserId) {
        val (fact, time) = inTime {
            userService.getFact(id)
        }
        println("진실 = $fact")
        println("시간 = $time ms.")
    }

    // sync. worst
    val sUserClient: UserClient = MockUserClient()
    val sFactClient: FactClient = MockFactClient()
    val sUserRepository: UserRepository = MockUserRepository()
    val sFactRepository: FactRepository = MockFactRepository()
    var userService: UserService = SynchronousUserService(
            sUserClient, sFactClient,
            sUserRepository, sFactRepository
    )

    // callback
    val cUserClient = CallbackUserClient(sUserClient)
    val cFactClient = CallbackFactClient(sFactClient)
    val cUserRepository = CallbackUserRepository(sUserRepository)
    val cFactRepository = CallbackFactRepository(sFactRepository)
    userService = CallbackUserService(
            cUserClient, cFactClient,
            cUserRepository, cFactRepository
    )

    // future
    userService = FutureUserService(
            sUserClient, sFactClient,
            sUserRepository, sFactRepository
    )

    // promise
    userService = PromiseUserService(
            sUserClient, sFactClient,
            sUserRepository, sFactRepository
    )

    // coroutine
    userService = CoroutineUserService(
            sUserClient, sFactClient,
            sUserRepository, sFactRepository
    )

    execute(userService, 1)
    execute(userService, 2)
    execute(userService, 1)
    execute(userService, 2)
    execute(userService, 3)
    execute(userService, 4)
    execute(userService, 5)
    execute(userService, 10)
    execute(userService, 100)

    println("Time Elapsed(Sync)     : 12014 ms")
    println("Time Elapsed(Callback) :  9435 ms")
    println("Time Elapsed(Future)   :  9335 ms")
    println("Time Elapsed(Promise)  :  9282 ms")
    println("Time Elapsed           :  ${System.currentTimeMillis() - userServiceStartTimeMs} ms")

    println("coroutineContext 블록 실행 = $coroutineContext")
    println("coroutineContext[Job] = ${coroutineContext[Job]}")
    println(Thread.currentThread().name)
    println("----")

    val jobs2 = listOf(
            launch {
                println("launch coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            },
            async {
                println("async coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            },
            launch(Dispatchers.Default) {
                println("Dispatchers.Default coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            },
            launch(Dispatchers.IO) {
                println("Dispatchers.IO coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            },
            // only for Android
//            launch (Dispatchers.Main) {
//                println("Dispatchers.Main coroutineContext = $coroutineContext")
//                println("coroutineContext[Job] = ${coroutineContext[Job]}")
//                println(Thread.currentThread().name)
//                println("----")
//            },
            launch(Dispatchers.Unconfined) {
                println("Dispatchers.Unconfined coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            },
            launch(coroutineContext) {
                println("inherit coroutineContext = $coroutineContext")
                println("coroutineContext[Job] = ${coroutineContext[Job]}")
                println(Thread.currentThread().name)
                println("----")
            }
    )

    jobs2.forEach { job ->
        println("job = $job")
        job.join()
    }

    val result = CompletableDeferred<String>()
    val world = launch {
        delay(500)
        result.complete("World (또 다른 코루틴에서)")
    }
    val hello = launch {
        println("Hello ${result.await()}")
    }
    hello.join()
    world.join()

    val channel = Channel<String>()
    val world2 = launch {
        delay(500)
        channel.send("World (채널을 사용한 또 다른 코루틴에서)")
    }
    val hello2 = launch {
        println("Hello ${channel.receive()}")
    }
    hello2.join()
    world2.join()

    val channel2 = Channel<Char>()
    val jobs3 = List(100) {
        launch {
            delay(1000)
            channel2.send('.')
        }
    }
    repeat(100) { print(channel2.receive()) }
    jobs3.forEach { job -> job.join() }

    val channel3 = Channel<Char>()
    val sender = launch {
        repeat(10) {
            delay(100)
            channel3.send(':')
            delay(100)
            channel3.send(',')
        }
        channel3.close()
    }
    for (msg in channel3) {
        print(msg)
    }
    sender.join()

    val channel4 = dotsAndCommas(10)
    for (msg in channel4) {
        print(msg)
    }

    val quoteChannel = Channel<Quote>()
    val accountingChannel = Channel<Bill>()
    val warehouseChannel = Channel<PickingOrder>()
    val transformerChannel = calculatePriceTransformer(coroutineContext, quoteChannel)
    val filteredChannel = cheapBillFilter(coroutineContext, transformerChannel)
    splitter(filteredChannel, accountingChannel, warehouseChannel)
    warehouseEndpoint(warehouseChannel)
    accountingEndpoint(accountingChannel)
    launch(coroutineContext) {
        quoteChannel.send(Quote(20.0, "Foo", "Shoes", 1))
        quoteChannel.send(Quote(20.0, "Bar", "Shoes", 200))
        quoteChannel.send(Quote(2000.0, "Foo", "Motorbike", 1))
    }
    delay(100)
    coroutineContext.cancelChildren()

    val nums = listOf(1, 2, 3, 4, 5, 6)
    val names = listOf("item 01", "item 02", "item 03", "item 04", "item 05", "item 06")
    val a = nums.zip(names)
    println("$a")

    var counter = 0
    var time = measureTimeMillis {
        repeatInParallel(10_000) {
            counter++
        }
    }
    println("counter = $counter")
    println("time = $time (coroutine)")

    counter = 0
    val counterContext = newSingleThreadContext("CounterContext")
    time = measureTimeMillis {
        repeatInParallel(10_000) {
            withContext(counterContext) {
                counter++
            }
        }
    }
    println("counter = $counter")
    println("time = $time (withContext)")

    val counterA = AtomicInteger(0)
    time = measureTimeMillis {
        repeatInParallel(10_000) {
            counterA.incrementAndGet()
        }
    }
    println("counterA = $counterA")
    println("time = $time (AtomicInteger)")

    counter = 0
    val mutex = Mutex()
    time = measureTimeMillis {
        repeatInParallel(10_000) {
            mutex.withLock {
                counter++
            }
        }
    }
    println("counter = $counter")
    println("time = $time (mutex)")

    val counterActor = counterActor(0)
    time = measureTimeMillis {
        repeatInParallel(1000) {
            counterActor.send(IncCounter)
        }
    }
    val counter2 = CompletableDeferred<Int>()
    counterActor.send(GetCounter(counter2))
    println("counter = ${counter2.await()}")
    println("time = $time")

    println(">>>>> 7장 테스트 종료 <<<<<")
}