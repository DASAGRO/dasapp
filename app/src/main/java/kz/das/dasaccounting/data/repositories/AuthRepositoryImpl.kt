package kz.das.dasaccounting.data.repositories

import kz.das.dasaccounting.core.extensions.unwrap
import kz.das.dasaccounting.data.entities.toDomain
import kz.das.dasaccounting.data.source.network.AuthApi
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthRepositoryImpl: AuthRepository, KoinComponent {

    private val authApi: AuthApi by inject()

    override suspend fun login(phone: String?, password: String?): Profile {
        val hashMap: HashMap<String, String?> = HashMap()
        hashMap["phone"] = phone
        hashMap["password"] = password
        return authApi.login(hashMap).unwrap { it.toDomain() }
    }

    override suspend fun sendPassword(phone: String?): Any {
        return authApi.sendPassword(phone).unwrap()
    }

    override suspend fun checkPhone(phone: String?): Profile? {
        return authApi.checkPhone(phone).unwrap { it.toDomain() }
    }

}