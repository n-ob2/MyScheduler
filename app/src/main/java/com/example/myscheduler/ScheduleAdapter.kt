package com.example.myscheduler

//import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter


class ScheduleAdapter(data: OrderedRealmCollection<Schedule>):
    RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(data,true) {

    private var listener: ((Long?) -> Unit)? = null     //引数がLong?型(スケジュールのIDを受け取るため)で戻り値がない関数型の変数

    fun setOnItemClickListener(listener:(Long?) -> Unit) {  //変数listenerに登録をする
        this.listener = listener    //受け取った関数を、セルがタップされた時に備えて変数listenerに格納
    }

    init {
        setHasStableIds(true)   //描画の高速化処理 （定型文）
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell){   //セルに使うビューを保持
        val date: TextView = cell.findViewById(android.R.id.text1)  //セルの中身id.text1
        val title: TextView = cell.findViewById(android.R.id.text2) //セルの中身id.text2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):    //ViewHolderが必要な時に呼ばれる
            ScheduleAdapter.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)  //インフレ―ターのfromメソッドでインフレ―ターのインスタンス取得
        val view = inflater.inflate(android.R.layout.simple_list_item_2,    //simple_list_item_2.xmlからビューを生成
                                    parent,false)
        return ViewHolder(view) //ViewHolderのインスタンスを戻す
    }

    override fun onBindViewHolder ( holder: ScheduleAdapter.ViewHolder, position: Int){ //指定された位置にデータを表示する必要がある時
        val schedule: Schedule? = getItem(position) //データベースから値を取得
        holder.date.text = DateFormat.format("yyyy/MM/dd HH:mm", schedule?.date)    //取得した値をテキストビューにセット
        holder.title.text = schedule?.title //取得した値をテキストビューにセット
        holder.itemView.setOnClickListener {    //セルのビューがタップされたら
            listener?.invoke(schedule?.id)  //スケジュールのIDを渡す
        }
    }

    override fun getItemId(position: Int): Long{    //描画の高速化処理（定型文）
        return getItem(position)?.id ?: 0
    }

}   //ScheduleAdapter↑↑

