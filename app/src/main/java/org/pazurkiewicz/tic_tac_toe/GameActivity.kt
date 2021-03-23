package org.pazurkiewicz.tic_tac_toe

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.pazurkiewicz.tic_tac_toe.databinding.ActivityGameBinding
import kotlin.properties.Delegates

class GameActivity : AppCompatActivity() {
    private var isWon = false
    private lateinit var binding: ActivityGameBinding
    private var turn = 0
    private lateinit var board: Array<Array<TileState>>
    private lateinit var tiles: Array<Array<ImageButton>>



    private var size by Delegates.notNull<Int>()
    private var win by Delegates.notNull<Int>()
    private var counter by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        size = intent.getIntExtra("size",3)
        win = intent.getIntExtra("win",3)
        binding.player1.text = win.toString()
        counter = size*size
        board = Array(size) {
            Array(size) {
                TileState.EMPTY
            }
        }
        createBoard()
    }


    fun nextTurn() {
        turn = (turn + 1) % 2
        if (turn == 0) {
            binding.player1.text = getString(R.string.play)
            binding.player2.text = getString(R.string.wait)
        } else {
            binding.player1.text = getString(R.string.wait)
            binding.player2.text = getString(R.string.play)
        }
    }

    private fun createBoard() {
        val board = binding.board
        val rowParams = TableLayout.LayoutParams(0, 0)
        rowParams.weight = 1f

        tiles = Array(size) {
            Array(size){
                ImageButton(this)
            }
        }
        for (y in 0 until size) {
            val newRow = TableRow(this)
            for (x in 0 until size) {

                newRow.addView(createTile(x, y))
            }
            newRow.layoutParams = rowParams
            board.addView(newRow)

        }
    }

    private fun createTile(x: Int, y: Int): View {
        val b = tiles[x][y]
        val layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT)

        layoutParams.weight = 1f
        b.layoutParams = layoutParams
        b.setImageResource(R.drawable.empty)
        b.scaleType = ImageView.ScaleType.CENTER_CROP

        b.setOnClickListener {
            if (isWon)
                finish()
            else {
                if (board[x][y] == TileState.EMPTY) {
                    if (turn == 0) {
                        b.setImageResource(R.drawable.cross)
                        board[x][y] = TileState.CROSS
                    } else {
                        b.setImageResource(R.drawable.circle)
                        board[x][y] = TileState.CIRCLE
                    }
                    checkIfWon(x, y)
                    nextTurn()
                    counter--
                    if (counter == 0)
                        someoneWon(-1)
                }
            }
        }



        return b
    }

    private fun checkIfWon(x: Int, y: Int) {
        val selectedTile = board[x][y]
//        horizontal
        var found = ArrayList<ImageButton>()
        found.add(tiles[x][y])

        for (i in x-1 downTo  0){
            if (board[i][y] == selectedTile)
                found.add(tiles[i][y])
            else
                break
        }

        for (i in x+1 until size) {
            if (board[i][y] == selectedTile)
                found.add(tiles[i][y])
            else
                break
        }
        if (found.size >= win){
            colorTiles(found)
            someoneWon(turn)
        }
//      vertical
        found = ArrayList()
        found.add(tiles[x][y])
        for (i in y-1 downTo  0){
            if (board[x][i] == selectedTile)
                found.add(tiles[x][i])
            else
                break
        }

        for (i in y+1 until size) {
            if (board[x][i] == selectedTile)
                found.add(tiles[x][i])
            else
                break
        }
        if (found.size >= win){
            colorTiles(found)
            someoneWon(turn)
        }

//        diagonally
        found = ArrayList()
        found.add(tiles[x][y])
        var i = x
        var j = y
        while (i > 0 && j > 0) {
            i--
            j--
            if (board[i][j] == selectedTile)
                found.add(tiles[i][j])
            else
                break
        }

        i = x
        j = y
        while (i < size -1 && j < size -1) {
            i++
            j++
            if (board[i][j] == selectedTile)
                found.add(tiles[i][j])
            else
                break
        }

        if (found.size >= win){
            colorTiles(found)
            someoneWon(turn)
        }

        found = ArrayList()
        found.add(tiles[x][y])
        i = x
        j = y
        while (i > 0 && j < size -1) {
            i--
            j++
            if (board[i][j] == selectedTile)
                found.add(tiles[i][j])
            else
                break
        }

        i = x
        j = y
        while (i < size -1 && j > 0) {
            i++
            j--
            if (board[i][j] == selectedTile)
                found.add(tiles[i][j])
            else
                break
        }

        if (found.size >= win){
            colorTiles(found)
            someoneWon(turn)
        }

    }

    private fun someoneWon(who : Int){
        val pom : String = when (who) {
            0 -> "X"
            1 -> "O"
            else -> "nobody"
        }
        val intent = Intent()
        intent.putExtra("who", pom)
        intent.putExtra("size", size)
        intent.putExtra("win", win)

        setResult(Activity.RESULT_OK, intent)
        if (who == -1)
            finish()
        else
            isWon = true
    }


    enum class TileState {
        EMPTY, CROSS, CIRCLE
    }

    private fun colorTiles(tiles : ArrayList<ImageButton>){
        for (tile in tiles)
            tile.setBackgroundColor(Color.GREEN)
    }

}