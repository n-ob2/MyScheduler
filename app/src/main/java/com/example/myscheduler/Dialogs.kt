package com.example.myscheduler

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

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