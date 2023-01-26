package com.example.ch1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickButton = findViewById<Button>(R.id.clickButton);
        val textView = findViewById<TextView>(R.id.textView);

        clickButton.setOnClickListener{
            textView.text="텍스트 버튼을 눌렀습니다"
        }
    }
}