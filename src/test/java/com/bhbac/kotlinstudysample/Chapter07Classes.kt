/*
 * © NHN Corp. All rights reserved.
 * NHN Corp. PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author dl_platformsdk_all@nhn.com
 */
package com.bhbac.kotlinstudysample

import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import org.h2.jdbcx.JdbcDataSource
import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import java.util.*

inline fun <T> inTime(body: () -> T): Pair<T, Long> {
    val startTime = System.currentTimeMillis()
    val v = body()
    val endTime = System.currentTimeMillis()
    return v to endTime - startTime
}

enum class Gender {
    MALE, FEMALE;

    companion object {
        fun valueOfIgnoreCase(name: String): Gender = valueOf(name.toUpperCase())
    }
}

typealias  UserId = Int

data class User(val id: UserId, val firstName: String, val lastName: String, val gender: Gender)

data class Fact(val id: UserId, val value: String, val user: User? = null)

interface UserService {
    fun getFact(id: UserId): Fact
}

interface UserClient {
    fun getUser(id: UserId): User
}

interface FactClient {
    fun getFact(user: User): Fact
}

abstract class WebClient {
    protected val apacheClient = ApacheClient()
    protected val gson = GsonBuilder()
        .registerTypeAdapter<User> {
            deserialize { des ->
                val json = des.json
                User(
                    json["info"]["seed"].int,
                    json["result"][0]["name"]["first"].string.capitalize(),
                    json["result"][0]["name"]["last"].string.capitalize(),
                    Gender.valueOfIgnoreCase(json["result"][0]["gender"].string)
                )
            }
        }
        .registerTypeAdapter<Fact> {
            deserialize { des ->
                val json = des.json
                Fact(
                    json["value"]["id"].int,
                    json["value"]["joke"].string
                )
            }
        }.create()!!
}

class Http4kUserClient : WebClient(), UserClient {
    override fun getUser(id: UserId): User {
        return gson.fromJson(
            apacheClient(
                Request(
                    Method.GET,
                    "https://randomuser.me/api"
                )
                    .query("seed", id.toString())
            )
                .bodyString()
        )
    }
}

class Http4kFactClient : WebClient(), FactClient {
    override fun getFact(user: User): Fact {
        return gson.fromJson<Fact>(
            apacheClient(
                Request(
                    Method.GET,
                    "http://api.icndb.com/jokes/random"
                )
                    .query("firstName", user.firstName)
                    .query("lastName", user.lastName)
            )
                .bodyString()
        )
            .copy(user = user)
    }
}

class MockUserClient : UserClient {
    override fun getUser(id: UserId): User {
        println("MockUserClient.getUser")
        Thread.sleep(500)
        return User(id, "Foo", "Bar", Gender.FEMALE)
    }
}

class MockFactClient : FactClient {
    override fun getFact(user: User): Fact {
        println("MockFactClient.getFact")
        Thread.sleep(500)
        return Fact(Random().nextInt(), "FACT ${user.firstName}, ${user.lastName}", user)
    }
}

interface UserRepository {
    fun getUserById(id: UserId): User?
    fun insertUser(user: User)
}

interface FactRepository {
    fun getFactByUserId(id: UserId): Fact?
    fun insertFact(fact: Fact)
}

abstract class JdbcRepository(protected val template: JdbcTemplate) {
    protected fun <T> toNullable(block: () -> T): T? {
        return try {
            block()
        } catch (_: EmptyResultDataAccessException) {
            null
        }
    }
}

class JdbcUserRepository(template: JdbcTemplate) : JdbcRepository(template), UserRepository {
    override fun getUserById(id: UserId): User? {
        return toNullable {
            template.queryForObject("select * from USERS where id = ?", id) { resultSet, _ ->
                with(resultSet) {
                    User(
                        getInt("ID"),
                        getString("FIRST+NAME"),
                        getString("LAST)NAME"),
                        Gender.valueOfIgnoreCase(getString("GENDER"))
                    )
                }
            }
        }
    }

    override fun insertUser(user: User) {
        template.update(
            "INSERT INFO USERS VALUES (?,?,?,?)",
            user.id, user.firstName, user.lastName, user.gender.name
        )
    }
}

class JdbcFactRepository(template: JdbcTemplate) : JdbcRepository(template), FactRepository {
    override fun getFactByUserId(id: UserId): Fact? {
        return toNullable {
            template.queryForObject(
                "select * from USERS as U inner join FACTS as F on U.ID = F.USER where U.ID = ?",
                id
            ) { resultSet, _ ->
                with(resultSet) {
                    Fact(
                        getInt(5),
                        getString(6),
                        User(
                            getInt(1),
                            getString(2),
                            getString(3),
                            Gender.valueOfIgnoreCase(getString(4))
                        )
                    )
                }
            }
        }
    }

    override fun insertFact(fact: Fact) {
        template.update(
            "INSERT INTO FACTS VALUES (?,?,?)",
            fact.id, fact.value, fact.user?.id
        )
    }
}

fun initJdbcTemplate(): JdbcTemplate {
    return JdbcTemplate(JdbcDataSource()
        .apply {
            setUrl("jdbc:h2:mem:facts_app;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
        })
        .apply {
            execute(
                "CREATE TABLE USERS (ID INT AUTO_INCREMENT PRIMARY KEY, " +
                        "FIRST_NAME VARCHAR(64) NOT NULL, LAST_NAME VARCHAR(64) NOT NULL, " +
                        "GENDER VARCHAR(8) NOT NULL);"
            )
            execute(
                "CREATE TABLE FACTS (ID INT AUTO_INCREMENT PRIMARY KEY," +
                        "VALUE_ TEXT NOT NULL, USER INT NOT NULL, FOREIGN KEY (USER) REFERENCES " +
                        "USERS(ID) ON DELETE RESTRICT)"
            )
        }
}

class MockUserRepository : UserRepository {
    private val users = hashMapOf<UserId, User>()
    override fun getUserById(id: UserId): User? {
        println("MockUsrRepository.getUserById")
        Thread.sleep(200)
        return users[id]
    }

    override fun insertUser(user: User) {
        println("MockUsreRepository.insertUser")
        Thread.sleep(200)
        users[user.id] = user
    }
}

class MockFactRepository : FactRepository {
    private val facts = hashMapOf<UserId, Fact>()
    override fun getFactByUserId(id: UserId): Fact? {
        println("MockFactRepository.getFactByUserId")
        Thread.sleep(200)
        return facts[id]
    }

    override fun insertFact(fact: Fact) {
        println("MockFactRepository.insertFact")
        Thread.sleep(200)
        facts[fact.user?.id ?: 0] = fact
    }
}

class SynchronousUserService(
    private val userClient: UserClient,
    private val factClient: FactClient,
    private val userRepository: UserRepository,
    private val factRepository: FactRepository
) : UserService {
    override fun getFact(id: UserId): Fact {
        val user = userRepository.getUserById(id)
        return if (user == null) {
            val userFromService = userClient.getUser(id)
            userRepository.insertUser(userFromService)
            getFact(userFromService)
        } else {
            factRepository.getFactByUserId(id) ?: getFact(user)
        }
    }

    private fun getFact(user: User): Fact {
        val fact = factClient.getFact(user)
        factRepository.insertFact(fact)
        return fact
    }
}

class CallbackUserClient(private val client: UserClient) {
    fun getUser(id: Int, callback: (User) -> Unit) {
        thread {
            callback(client.getUser(id))
        }
    }
}

class CallbackFactClient(private val client: FactClient) {
    fun get(user: User, callback: (Fact) -> Unit) {
        thread {
            callback(client.getFact(user))
        }
    }
}

class CallbackUserRepository(private val userRepository: UserRepository) {
    fun getUserById(id: UserId, callback: (User?) -> Unit) {
        thread {
            callback(userRepository.getUserById(id))
        }
    }

    fun insertUser(user: User, callback: () -> Unit) {
        thread {
            userRepository.insertUser(user)
            callback()
        }
    }
}

class CallbackFactRepository(private val factRepository: FactRepository) {
    fun getFactByUserId(id: Int, callback: (Fact?) -> Unit) {
        thread {
            callback(factRepository.getFactByUserId(id))
        }
    }

    fun insertFact(fact: Fact, callback: () -> Unit) {
        thread {
            factRepository.insertFact(fact)
            callback()
        }
    }
}

class CallbackUserService(
    private val userClient: CallbackUsrClient,
    private val factClient: CallbackFactClient,
    private val userRepository: CallbackUserRepository,
    private val factRepository: CallbackFactRepository
) : UserService {
    override fun getFact(id: UserId): Fact {
        var aux: Fact? = null
        userRepository.getUserById(id) { user ->
            if (user == null) {
                userClient.getUser(id) { userFromClient ->
                    userRepository.insertUser(userFromClient) {}
                    factClient.get(userFromClient) { fact ->
                        factRepository.insertFact(fact) {}
                        aux = fact
                    }
                }
            } else {
                factRepository.getFactByUserId(id) { fact ->
                    if (fact == null) {
                        factClient.get(user) { factFromClient ->
                            factRepository.insertFact(factFromClient) {}
                            aux = factFromClient
                        }
                    } else {
                        aux = fact
                    }
                }
            }
        }

        while (aux == null) {
            Thread.sleep(2)
        }

        return aux!!
    }
}