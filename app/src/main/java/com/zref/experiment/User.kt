package com.zref.experiment

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class User : RealmObject() {
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var name = ""
    var age = 0
}