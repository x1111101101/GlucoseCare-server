package io.github.x1111101101.account.service

import io.github.x1111101101.account.repository.UserRepository
import io.github.x1111101101.account.vo.*
import io.github.x1111101101.isValidSHA256
import io.github.x1111101101.session.SessionManager
import org.jetbrains.exposed.sql.transactions.transaction

object UserService {

    val userRepository = UserRepository()

    fun signup(register: UserRegister): SignupRespond {
        fun fail(msg: String) = SignupRespond(false, msg)
        if(!isValidSHA256(register.loginPasswordHash)) {
            return fail("invalid sha256 hash")
        }
        if(!register.loginId.matches(SignupSecurities.loginIdRegex)) {
            return fail("아이디는 영문 소문자와 숫자로만 이루어져야 합니다.")
        }
        return transaction {
            if(userRepository.getUserByLoginId(register.loginId) != null) {
                return@transaction fail("다른 회원과 아이디가 중복됩니다.")
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
        val vo = UserVO(user)
        return UserLoginRespond(true, "", session.uuid.toString(), vo)
    }


}

private object SignupSecurities {
    val loginIdRegex = "^([a-z, 0-9])\\w+".toRegex()
}