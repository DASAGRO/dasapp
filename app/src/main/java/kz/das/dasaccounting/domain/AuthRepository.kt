package kz.das.dasaccounting.domain

import kz.das.dasaccounting.domain.data.Profile

interface AuthRepository {

    suspend fun login(phone: String?, password: String?): Profile

    suspend fun sendPassword(phone: String?): Any

    suspend fun checkPhone(phone: String?): Profile?

}