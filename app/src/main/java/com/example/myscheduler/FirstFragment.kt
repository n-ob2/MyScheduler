package com.example.myscheduler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myscheduler.databinding.FragmentFirstBinding
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var realm: Realm   //realmクラスのプロパティを用意

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()    //realmクラスのインスタンス取得
    }   //onCreate↑↑

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }   //onCreateView↑↑

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.list.layoutManager = LinearLayoutManager(context)   //項目を直列に並べるクラスのインスタンス生成、レイアウトマネージャとして設定
        val schedules = realm.where<Schedule>().findAll()   //データを取得して定数に格納
        val adapter = ScheduleAdapter(schedules)    //クエリで取得したデータを渡してアダプタに適用
        binding.list.adapter = adapter  //作ったアダプタのルール通りに表示

        adapter.setOnItemClickListener { id ->  //RecyclerViewがタップされた時に
            id?.let {
                val action =
                    FirstFragmentDirections.actionToScheduleEditFragment(it)    //自動生成されるFirstFragmentDirectionクラスでスケジュールエディットへの遷移アクション「actionToScheduleEditFragment」にIDを渡す
                findNavController().navigate(action)    //作成したアクションをNavigateメソッドに渡して画面遷移する
            }
        }

    }   //onViewCreated↑↑

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)    //非表示にしていたfabボタンを表示
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){ //activityが破棄されると
        super.onDestroy()
        realm.close()   //realmインスタンスを破棄してリソースを解放
    }
}