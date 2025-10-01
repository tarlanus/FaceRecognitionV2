package com.tarlanus.facerecognitionv2

import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.models.UserDetails

fun List<UserFaces>.toListUserDetails(): List<UserDetails> {
    return this.map {
        UserDetails(candidateName = it.candidateName, imagePath = it.imagePath, id = it.id)
    }
}

fun UserFaces.toUserDetail(): UserDetails {

    return UserDetails(candidateName = this.candidateName, imagePath = this.imagePath, id = this.id)
}