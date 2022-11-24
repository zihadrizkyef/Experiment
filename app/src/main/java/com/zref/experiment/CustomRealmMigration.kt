package com.zref.experiment

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration

class CustomRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        if (oldVersion == 0L && newVersion == 1L) {
            val userSchema = realm.schema.get(User::class.java.simpleName)
            userSchema?.addField("age", Int::class.java, FieldAttribute.REQUIRED)
        }
    }
}