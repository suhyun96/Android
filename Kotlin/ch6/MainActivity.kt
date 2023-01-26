package com.example.ch6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ch6.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    private var time = 0
    private var timerTask: Timer? = null
    private var isRunning = false
    private var lap = 1

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun reset() {
        // 워커 스레드의 타이머 취소
        timerTask?.cancel()
        // 변수 초기화
        time = 0
        isRunning = false
        binding.fab.setImageResource(R.drawable.baseline_play_arrow_24)

        binding.setTextView.text = "0"
        binding.milliTextView.text = "00"

        // 해당 스크롤뷰에 있는 뷰들을 다 삭제
        binding.lapLayout.removeAllViews()
        lap = 1
    }

    private fun recordLapTime() {
        // 해당 클래스의 시간을 가져옴
        val lapTime = this.time
        // 해당 함수가 실행할 때마다 텍스트뷰가 동적 생성
        val textView = TextView(this)
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"
        // 해당 텍스트 뷰를 레이아웃에 추가되도록  맨위에 추가
        // 리니어 스크린은 차례차례 쌓여짐 즉 쌓이는 거는 역순임
        binding.lapLayout.addView(textView, 0)
        lap++

    }

    // 일시정지하는 경우 워커스레드 없애서 시간 안 올라가도록 조절
    private fun pause() {
        binding.fab.setImageResource(R.drawable.baseline_play_arrow_24)
        timerTask?.cancel()
    }

    private fun start() {
        // 시작 버튼  누를 때 일시정지 버튼으로 변경
        binding.fab.setImageResource(R.drawable.baseline_pause_24)
        // 0.01 초마다 실행
        // timer 는 워커스레드에서 동작함
        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100
            // 0.01초 마다 워커 스레드에서 ui 변경을 위해 runOnUiThread함수가 사용됨
            // 사용자각 보기엔 스탑워치처럼 시간이 실시간으로 변경됨
            runOnUiThread() {
                binding.setTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 시작 버튼을 누르면 isRunning 값을 변경 해당 값을 통해서 일시정지 및 기록시작 변경
        binding.fab.setOnClickListener() {
            isRunning = !isRunning

            if (isRunning) {
                start()
            } else {
                pause()
            }
        }

        binding.lapButon.setOnClickListener() {
            recordLapTime()
        }

        binding.resetFab.setOnClickListener() {
            reset()
        }
    }
}
