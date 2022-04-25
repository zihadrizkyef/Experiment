package com.zref.experiment

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

class Buku : RealmObject {

    constructor()

    constructor(id: String = UUID.randomUUID().toString(), name: String = "") {
        this.id = id
        this.name = name
    }

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var name: String = ""
}