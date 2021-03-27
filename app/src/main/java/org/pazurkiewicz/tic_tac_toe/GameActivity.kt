package org.pazurkiewicz.tic_tac_toe

import android.app.Activity
import android.content.Context
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
    private lateinit var tiles: Array<Array<MyTile>>



    private var size by Delegates.notNull<Int>()
    private var win by Delegates.notNull<Int>()
    private var counter by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        size = intent.getIntExtra("size",3)
        win = intent.getIntExtra("win",3)
        counter = size*size
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
                MyTile(this)
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
                if (tiles[x][y].tileState == TileState.EMPTY) {
                    if (turn == 0) {
                        b.setImageResource(R.drawable.cross)
                        tiles[x][y].tileState = TileState.CROSS
                    } else {
                        b.setImageResource(R.drawable.circle)
                        tiles[x][y].tileState = TileState.CIRCLE
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
        val selectedTileState = tiles[x][y].tileState
//        horizontal
        var found = ArrayList<MyTile>()
        found.add(tiles[x][y])

        for (i in x-1 downTo  0){
            if (tiles[i][y].tileState == selectedTileState)
                found.add(tiles[i][y])
            else
                break
        }

        for (i in x+1 until size) {
            if (tiles[i][y].tileState == selectedTileState)
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
            if (tiles[x][i].tileState == selectedTileState)
                found.add(tiles[x][i])
            else
                break
        }

        for (i in y+1 until size) {
            if (tiles[x][i].tileState == selectedTileState)
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
            if (tiles[i][j].tileState == selectedTileState)
                found.add(tiles[i][j])
            else
                break
        }

        i = x
        j = y
        while (i < size -1 && j < size -1) {
            i++
            j++
            if (tiles[i][j].tileState == selectedTileState)
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
            if (tiles[i][j].tileState == selectedTileState)
                found.add(tiles[i][j])
            else
                break
        }

        i = x
        j = y
        while (i < size -1 && j > 0) {
            i++
            j--
            if (tiles[i][j].tileState == selectedTileState)
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

    private fun colorTiles(tiles : ArrayList<MyTile>){
        for (tile in tiles)
            tile.setBackgroundColor(Color.GREEN)
    }

    private class MyTile(context: Context) : androidx.appcompat.widget.AppCompatImageButton(context) {
        var tileState = TileState.EMPTY
    }

}