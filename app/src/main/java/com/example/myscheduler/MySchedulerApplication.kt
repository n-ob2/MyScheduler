package com.example.myscheduler

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MySchedulerApplication: Application() { //Applicationクラスを継承
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)    //Realm初期化
        val config = RealmConfiguration.Builder()   //Realm初期設定
            .allowWritesOnUiThread(true).build()    //UIスレッドのデータベース書込みを許可
        Realm.setDefaultConfiguration(config)
    }
}