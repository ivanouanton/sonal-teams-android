package com.waveneuro.data.model.entity

import com.asif.abase.data.model.BaseModel

class User : BaseModel {
    var id: Int? = null
    var email: String? = null
    var username: String? = null
    var name: String? = null
    var givenName: String? = null
    var familyName: String? = null
    var birthdate: String? = null
    var imageThumbnailUrl: String? = null
    var gender: String? = null
    var location: String? = null
    var customGoal: String? = null

    constructor()
    constructor(
        id: Int?, email: String?, username: String?, name: String?, givenName: String?, familyName: String?,
        birthdate: String?, imageThumbnailUrl: String?, gender: String?, location: String?,
        customGoal: String?
    ) {
        this.id = id
        this.email = email
        this.username = username
        this.name = name
        this.givenName = givenName
        this.familyName = familyName
        this.birthdate = birthdate
        this.imageThumbnailUrl = imageThumbnailUrl
        this.gender = gender
        this.location = location
        this.customGoal = customGoal
    }

    val isNameAvailable: Boolean
        get() = name != null && name!!.isNotEmpty()
}