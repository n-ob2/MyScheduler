package com.example.myscheduler

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Schedule: RealmObject() {
    @PrimaryKey //データを識別するための項目、同じidが存在しないように管理する
    var id: Long = 0    //スケジュールを連番で管理するためのid
    var date: Date = Date()
    var title: String = ""
    var detail: String = ""
}