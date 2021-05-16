package kz.das.dasaccounting.domain

interface AuthRepository {

    fun login(phone: String?, password: String?) {

    }

    fun forgetPassword(phone: String?) {

    }

    fun checkUser() {

    }

}