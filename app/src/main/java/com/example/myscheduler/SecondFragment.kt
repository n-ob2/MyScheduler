package com.example.myscheduler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myscheduler.databinding.FragmentSecondBinding
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm   //Realmインスタンス取得

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
        binding.list.layoutManager = LinearLayoutManager(context)
        var dateTime = Calendar.getInstance().apply {
            timeInMillis = binding.calendarView.date //選択中の日付を取得
        }
        findSchedule(   //選択されている日付のスケジュール詳細を取得
            dateTime.get(Calendar.YEAR),
            dateTime.get(Calendar.MONTH),
            dateTime.get(Calendar.DAY_OF_MONTH)
        )
        binding.calendarView
            .setOnDateChangeListener { view, year,month, dayOfMonth ->
            findSchedule(year, month, dayOfMonth)   //カレンダーで選択中の日付が変わったときの処理（ラムダ式）
        }
    }   //onViewCreated↑↑

    private fun findSchedule(
        year: Int,
        month: Int,
        dayOfMonth: Int
    ){
        var selectDate = Calendar.getInstance().apply{   //選択された日付より時刻を切り捨てたデータを作成
            clear()
            set(year, month, dayOfMonth)
        }
        val schedules = realm.where<Schedule>()
            .between(   //指定日の0:00:00-23:59:59.999までを選択
                "date",
                selectDate.time,
                selectDate.apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.MILLISECOND, -1)
                }.time
        ).findAll().sort("date")    //日付の順に並べる
        val adapter = ScheduleAdapter(schedules)    //アダプタにクエリ結果を渡す
        binding.list.adapter = adapter

        adapter.setOnItemClickListener{ id ->   //アイテムがクリックされた時に
            id?.let {
                val action = SecondFragmentDirections
                    .actionToScheduleEditFragment(it)   //送信側のクラスが自動作成されて、ScheduleEditFragmentへ遷移
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()   //データベースを閉じる

    }
}