package org.pazurkiewicz.tic_tac_toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import org.pazurkiewicz.tic_tac_toe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBoardSizeChanged()
        onWinSizeChanged()

        val sizeBar = binding.sizeSeekBar
        val winBar = binding.winSeekBar

        sizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onBoardSizeChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                winBar.isEnabled = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                winBar.isEnabled = true
            }
        })

        winBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onWinSizeChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


    }

    private fun onBoardSizeChanged() {
        binding.winSeekBar.max = binding.sizeSeekBar.progress
        val pomSize = binding.sizeSeekBar.progress + 3
        binding.size.text = getString(R.string.board_size, pomSize, pomSize)
    }

    private fun onWinSizeChanged() {
        binding.win.text = getString(R.string.win_size, binding.winSeekBar.progress + 3)
    }

    fun onAccept(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("size",binding.sizeSeekBar.progress + 3)
        intent.putExtra("win", binding.winSeekBar.progress + 3)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (data == null) {
                binding.lastWon.visibility = View.GONE

            } else {
                binding.lastWon.text = getString(
                    R.string.last_won,
                    data.getStringExtra("who"),
                    data.getIntExtra("size", 0),
                    data.getIntExtra("size", 0),
                    data.getIntExtra("win", 0)
                )
                binding.lastWon.visibility = View.VISIBLE
            }
        }
    }

}