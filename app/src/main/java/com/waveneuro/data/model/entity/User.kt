package com.waveneuro.data.model.entity

import com.asif.abase.data.model.BaseModel

class User : BaseModel {
    var id: String
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

    constructor(
        id: String
    ) {
        this.id = id
    }

    val isNameAvailable: Boolean
        get() = name != null && name!!.isNotEmpty()
}