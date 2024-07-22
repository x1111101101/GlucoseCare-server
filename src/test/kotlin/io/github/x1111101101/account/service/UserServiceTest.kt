package io.github.x1111101101.account.service

import io.github.x1111101101.Strings
import io.github.x1111101101.account.vo.UserLoginRequest
import io.github.x1111101101.account.vo.UserRegister
import io.github.x1111101101.connectDB
import io.github.x1111101101.sha256Hash
import org.junit.jupiter.api.Test
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
            assertEquals(Strings.invalidLoginId, respond.message)
        }
    }

}