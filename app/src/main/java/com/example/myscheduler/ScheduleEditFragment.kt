package com.example.myscheduler

//import android.icu.text.DateFormat
//import android.icu.text.SimpleDateFormat
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myscheduler.databinding.FragmentScheduleEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleEditFragment : Fragment() {

    private var _binding: FragmentScheduleEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()  //realmのインスタンスを取得

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleEditBinding.inflate(inflater,container, false)
        return binding.root
    }

    private val args: ScheduleEditFragmentArgs by navArgs() //safeargsで遷移もとから安全に引数を取得

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.scheduleId != -1L){    //スケジュールIDが-1の時は新規登録
            val schedule = realm.where<Schedule>()
                .equalTo("id", args.scheduleId).findFirst() //同じIDのレコードを取得して再格納
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", schedule?.date))   //各項目を表示
            binding.timeEdit.setText(DateFormat.format("HH:mm", schedule?.date))
            binding.titleEdit.setText(schedule?.title)
            binding.detailEdit.setText(schedule?.detail)
            binding.delete.visibility = View.VISIBLE
        }else{
            binding.delete.visibility = View.INVISIBLE
        }
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)  //fabボタンの非表示
        binding.save.setOnClickListener { saveSchedule(it) }    //保存ボタンをタップした時にsaveScheduleメソッド実行
        binding.delete.setOnClickListener { deleteSchedule(it) }
    }

    private fun saveSchedule(view: View){
        when (args.scheduleId) {
            -1L -> {    //新規登録

            realm.executeTransaction { db: Realm -> //トランザクション実行
                val maxId = db.where<Schedule>().max("id")  //DBのid最大値を取得して
                val nextId = (maxId?.toLong() ?: 0L) + 1L   //新規作成するスケジュールidは最大値に+1
                val schedule = db.createObject<Schedule>(nextId)    //データを一行新規追加
                val date = ("${binding.dateEdit.text}" +
                        "${binding.timeEdit.text}").toDate()
                if (date != null) schedule.date = date
                schedule.title = binding.titleEdit.text.toString()
                schedule.detail = binding.detailEdit.text.toString()
            }
            Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)    //スナックバーを作成
                .setAction("戻る"){ findNavController().popBackStack() }  //アクションを設定。テキストと「ひとつ前の画面に戻る」アクション
                .setActionTextColor(Color.YELLOW)   //アクションテキストの色決定
                .show() //スナックバーを表示
        }
            else -> {   //更新
                realm.executeTransaction{ db: Realm ->
                    val schedule =db.where<Schedule>()
                        .equalTo("id", args.scheduleId).findFirst() //同じIDを探す
                    val date = ("${binding.dateEdit.text} " + "${binding.timeEdit.text}").toDate()
                    /*
                    Log.e("debug", "date;$date")
                    val result = date is String
                    Log.e("debug","result;$result")
                    print(date is String)
                    */
                    if (date != null) schedule?.date = date
                    schedule?.title = binding.titleEdit.text.toString()
                    schedule?.detail = binding.detailEdit.text.toString()
                }
                Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack()}
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }
    }

    private fun deleteSchedule(view: View){
        realm.executeTransaction{ db: Realm ->
            db.where<Schedule>().equalTo("id", args.scheduleId)
                ?.findFirst()
                ?.deleteFromRealm() //1
        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {  //データーベースを閉じる
        super.onDestroy()
        realm.close()
    }

    private fun String.toDate( pattern: String = "yyyy/MM/dd HH:mm" ): Date?{   //日付で入力されたString型をDate型で扱える形に整形
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch(e: IllegalArgumentException){
            return null
        } catch(e: ParseException){
            return null
        }
    }
}