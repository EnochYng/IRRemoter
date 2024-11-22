package com.enochyoung28.irremoter

import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var irManager: ConsumerIrManager
    private lateinit var sendIrButton: Button

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 获取红外发射器管理对象
        irManager = getSystemService(CONSUMER_IR_SERVICE) as ConsumerIrManager
        sendIrButton = findViewById(R.id.OnOff)

        // 检查设备是否支持红外发射器
        if (irManager.hasIrEmitter()) {
            sendIrButton.setOnClickListener {
                sendIrSignal()
                vibrateButton()
            }
        } else {
            Toast.makeText(this, "设备不支持红外功能", Toast.LENGTH_SHORT).show()
        }
    }

    // 发送红外信号
    private fun sendIrSignal() {
        val frequency = 38000  // 常见的38kHz频率
        val pattern = intArrayOf(
            9000, 4500, 560, 560, 560, 1690, // NEC协议的简单编码
            560, 560, 560, 1690,
            560, 1690, 560, 1690,
            560, 1690, 560, 1690
        )

        try {
            irManager.transmit(frequency, pattern)

            Toast.makeText(this, "信号已发送", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "发送信号失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 震动按钮的方法
    @RequiresApi(Build.VERSION_CODES.S)
    private fun vibrateButton() {
        // 获取Vibrator系统服务
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val vibrationEffect = VibrationEffect.createOneShot(89, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    }

}
