package io.github.x1111101101.glucoseserver.account.service

import io.github.x1111101101.glucoseserver.Container
import io.github.x1111101101.glucoseserver.Strings
import io.github.x1111101101.glucoseserver.account.dto.*
import io.github.x1111101101.glucoseserver.isValidSHA256
import io.github.x1111101101.glucoseserver.session.SessionManager
import org.jetbrains.exposed.sql.transactions.transaction

object UserService {

    private val userRepository = Container.userRepository

    fun signup(register: UserRegister): SignupRespond {
        fun fail(msg: String) = SignupRespond(false, msg)
        if(!isValidSHA256(register.loginPasswordHash)) {
            return fail(Strings.invalidHash)
        }
        if(!register.loginId.matches(SignupSecurities.loginIdRegex)) {
            return fail(Strings.invalidLoginId)
        }
        return transaction {
            if(userRepository.getUserByLoginId(register.loginId) != null) {
                return@transaction fail(Strings.loginIdDuplicated)
            }
            val id = userRepository.addUser(register)
                ?: return@transaction fail("서버 오류로 인해 회원가입에 실패하였습니다.")
            return@transaction SignupRespond(true, "")
        }
    }

    fun signin(request: UserLoginRequest): UserLoginRespond {
        fun fail(msg: String): UserLoginRespond {
            return UserLoginRespond(false, msg, null, null)
        }

        val user = transaction {
            userRepository.getUserByLoginId(request.loginId)
        }
        if(user == null || user.loginPasswordHash != request.loginPasswordHash) {
            return fail("아이디 또는 비밀번호가 일치하지 않습니다.")
        }
        val session = SessionManager.create(user.loginId)
        val vo = UserDTO(user)
        return UserLoginRespond(true, "", session.uuid.toString(), vo)
    }

}

private object SignupSecurities {
    val loginIdRegex = "^([a-z, 0-9])\\w+".toRegex()
}