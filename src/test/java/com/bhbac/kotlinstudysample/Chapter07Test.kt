/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.properties.Delegates

val numThreads = 10
val threadWorkTime = 1000L

@Test
fun main() = runBlocking {
    println(">>>>> 7장 테스트 시작 <<<<<")

    val skipCodes = true

    val worldCallTime = 100L
    val worldWaitTime = 250L

    var startTime: Long by Delegates.notNull()
    if (!skipCodes) {
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
    }

    println("\n\n========== SynchronousUserService ==========\n")
    fun execute(userService: UserService, id: UserId) {
        val (fact, time) = inTime {
            userService.getFact(id)
        }
        println("진실 = $fact")
        println("시간 = $time ms.")
    }

    var userClient = MockUserClient()
    var factClient = MockFactClient()
    var userRepository = MockUserRepository()
    var factRepository = MockFactRepository()
    var userService = SynchronousUserService(
        userClient, factClient,
        userRepository, factRepository
    )
    userClient = CallbackUserClient()
    factClient = CallbackFactClient()
    userRepository = CallbackUserRepository()
    factRepository = CallbackFactRepository()
    userService = CallbackUserService(
        userClient, factClient,
        userRepository, factRepository
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

    println(">>>>> 7장 테스트 종료 <<<<<")
}