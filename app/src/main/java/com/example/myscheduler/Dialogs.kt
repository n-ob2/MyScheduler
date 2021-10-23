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
        return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())    //インスタンス生成
        builder.setMessage(message)
        builder.setPositiveButton(okLabel) { dialog, wihch ->
            okSelected()
        }
        builder.setNegativeButton(cancelLabel){ dialog, which ->
            cancelSelected()
        }
        return builder.create()
    }
}