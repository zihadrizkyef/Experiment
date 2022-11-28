package com.zref.experiment

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration

class CustomRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        migrate0to1(realm)
        migrate1to2(realm)
    }

    fun migrate0to1(realm: DynamicRealm) {
        val userSchema = realm.schema.get(User::class.java.simpleName)
        userSchema?.addField("age", Int::class.java, FieldAttribute.REQUIRED)
    }

    fun migrate1to2(realm: DynamicRealm) {
        val userSchema = realm.schema.get(User::class.java.simpleName)
        userSchema?.addField("money", Int::class.java, FieldAttribute.REQUIRED)
    }
}