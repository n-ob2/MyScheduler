package com.example.myscheduler

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class ConfirmDialog(private val message: String,    //丸かっこ内はクラスのプロパティ 1行目はダイアログのタイトル
                    private val okLabel: String,
                    private val okSelected: () -> Unit, //okボタンがタップされた時の関数
                    private val cancelLabel: String,
                    private val cancelSelected: () -> Unit )    //キャンセルボタンがタップされた時の関数
    : DialogFragment(){ //DialogFragmentクラスを継承
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {  //ダイアログの中身
        val builder = AlertDialog.Builder(requireActivity())    //インスタンス生成
        builder.setMessage(message) //定数messageを表示
        builder.setPositiveButton(okLabel) { dialog, wihch ->   //ボタンをokLabelで表示
            okSelected()    //タップされたらokSelectedの処理を実行
        }
        builder.setNegativeButton(cancelLabel){ dialog, which ->    //ボタンをcancelLabelで表示
            cancelSelected()    //タップされたらcancelSelectedの処理を実行
        }
        return builder.create() //onCreateDialogの最後にオブジェクトを返す
    }
}

class DateDialog(private val onSelected: (String) -> Unit)  //日付選択時の処理を受け取るプロパティ
    : DialogFragment(), DatePickerDialog.OnDateSetListener{ //クラス継承とインターフェース実装

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        val c = Calendar.getInstance()  //現在の日付を初期値として設定
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DATE)
        return DatePickerDialog(requireActivity(), this, year, month, date) //インスタンスを戻す
        }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) { //日付設定された時の処理
        onSelected("$year/${month + 1}/$dayOfMonth")

    }
}   // DateDialog↑↑

class TimeDialog(private val onSelected: (String) -> Unit)  //処理を受け取るプロパティ実装
    : DialogFragment(), TimePickerDialog.OnTimeSetListener{ //インターフェース実装

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(context, this, hour, minute, true)  //インスタンスを戻す
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onSelected("%1$02d:%2$02d".format(hourOfDay, minute))   //時間の形式を整えてプロパティに渡す
    }
}   //TimeDialog↑↑