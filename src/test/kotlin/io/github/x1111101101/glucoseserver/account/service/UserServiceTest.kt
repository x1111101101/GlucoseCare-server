package io.github.x1111101101.glucoseserver.account.service

import io.github.x1111101101.glucoseserver.Strings
import io.github.x1111101101.glucoseserver.account.dto.UserLoginRequest
import io.github.x1111101101.glucoseserver.account.dto.UserRegister
import io.github.x1111101101.glucoseserver.connectDB
import io.github.x1111101101.glucoseserver.currentMillis
import io.github.x1111101101.glucoseserver.sha256Hash
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals


class UserServiceTest {

    init {
        connectDB()
    }

    val service = UserService

    val invalidLoginIds = listOf(
        "@!ASDF", "ASDF!23", "!ASDF222", "asdf111.", ".asdf1213", "ASDF"
    )

    @Test
    fun deny_invalid_loginId() {
        val hash = sha256Hash("test")
        val invalidRequests = invalidLoginIds.map {
            UserRegister(loginId = it, hash, "", "", 0, "")
        }
        invalidRequests.forEach {
            val respond = service.signup(it)
            println("invalid id: ${it.loginId}, respond: $respond")
            assertEquals(false, respond.succeed)
            assertEquals(Strings.INVALID_LOGINID, respond.message)
        }
    }

    @Test
    fun signup_and_login() {
        val id = "testid${(Random.nextDouble() * 1000000).toInt()}"
        val pwHash = sha256Hash("testpassword")
        val register = UserRegister(
            loginId = id, loginPasswordHash = pwHash, name="name",
            phoneNumber = "01000000000", birthday = currentMillis(), extra = ""
        )
        val respond = UserService.signup(register)
        assertEquals(true, respond.succeed)
        val loginRespond = UserService.signin(UserLoginRequest(id, pwHash))
        assertEquals(true, loginRespond.succeed)
    }

}