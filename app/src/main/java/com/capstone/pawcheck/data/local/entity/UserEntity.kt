package com.capstone.pawcheck.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity (
    @field:ColumnInfo(name = "email")
    @field:androidx.room.PrimaryKey
    val email: String,

    @field:ColumnInfo(name = "username")
    val userName: String,

    @field:ColumnInfo(name = "password")
    val password: String,

    @field:ColumnInfo(name = "fullName")
    val fullName: String,

    @field:ColumnInfo(name = "profilePict")
    val profilePict: String,

    @field:ColumnInfo(name = "fullName")
    val isLogin: Boolean,

)
