package com.capstone.pawcheck.data.repository

import com.capstone.pawcheck.data.local.entity.UserProfile
import com.capstone.pawcheck.data.local.room.UserProfileDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {

    suspend fun getUserProfile(userId: String): UserProfile? {
        return userProfileDao.getUserProfile(userId)
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfileDao.insert(userProfile)
    }
}
