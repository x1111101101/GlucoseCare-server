package io.github.x1111101101.account.service

import io.github.x1111101101.account.vo.UserRegister
import io.github.x1111101101.account.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction

object UserService {

    val userRepository = UserRepository()

    fun signup(register: UserRegister): SignupRespond {
        var errorMsg: String? = null
        transaction {
            if(userRepository.getUserByLoginId(register.loginId) != null) {
                errorMsg = "다른 회원과 아이디가 중복됩니다."
                return@transaction
            }
            val id = userRepository.addUser(register)
            if(id == null) {
                errorMsg = "서버 오류로 인해 회원가입에 실패하였습니다."
                return@transaction
            }
        }
        if(errorMsg === null) {
            return SignupRespond(true, "")
        }
        return SignupRespond(false, errorMsg!!)
    }


    data class SignupRespond(val succeed: Boolean, val message: String) {

    }

}

