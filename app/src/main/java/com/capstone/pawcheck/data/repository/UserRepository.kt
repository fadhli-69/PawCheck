package com.capstone.pawcheck.data.repository

import com.capstone.pawcheck.data.local.entity.UserEntity
import com.capstone.pawcheck.data.local.room.UserProfileDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {

    suspend fun getUserProfile(userId: String): UserEntity? {
        return userProfileDao.getUserProfile(userId)
    }

    suspend fun saveUserProfile(userProfile: UserEntity) {
        userProfileDao.insert(userProfile)
    }
}
