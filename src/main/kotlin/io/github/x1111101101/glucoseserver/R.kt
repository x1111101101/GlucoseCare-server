package io.github.x1111101101.glucoseserver

object R {

    val strings = Strings

}

object Strings {
    val loginRequired = "로그인이 필요한 서비스입니다."
    val sessionExpired = "세션이 만료되었습니다. 다시 로그인해주세요."
    val invalidHash = "invalid sha256 hash"
    val invalidLoginId = "아이디는 영문 소문자와 숫자로 이루어져야 합니다."
    val loginIdDuplicated = "다른 회원과 아이디가 중복됩니다."
    val unknownUser = "unknown user"
}