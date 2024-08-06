package gcc.holywater.myapplication


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import gcc.holywater.velha.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    // Vetor bidimensional que representará o tabuleiro de jogo
    val tabuleiro = arrayOf(
        arrayOf("", "", ""),
        arrayOf("", "", ""),
        arrayOf("", "", "")
    )


    // Qual o Jogador está jogando
    var jogadorAtual = "X"


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        // Configurações de botões e eventos
        binding.buttonDificil.setOnClickListener {
            val intent = Intent(this, MDificilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun buttonClick(view: View) {
        val buttonSelecionado = view as Button
        when (buttonSelecionado.id) {
            binding.buttonZero.id -> tabuleiro[0][0] = jogadorAtual
            binding.buttonUm.id -> tabuleiro[0][1] = jogadorAtual
            binding.buttonDois.id -> tabuleiro[0][2] = jogadorAtual
            binding.buttonTres.id -> tabuleiro[1][0] = jogadorAtual
            binding.buttonQuatro.id -> tabuleiro[1][1] = jogadorAtual
            binding.buttonCinco.id -> tabuleiro[1][2] = jogadorAtual
            binding.buttonSeis.id -> tabuleiro[2][0] = jogadorAtual
            binding.buttonSete.id -> tabuleiro[2][1] = jogadorAtual
            binding.buttonOito.id -> tabuleiro[2][2] = jogadorAtual
        }
        buttonSelecionado.setBackgroundColor(Color.BLUE)
        buttonSelecionado.isEnabled = false


        var vencedor = verificaVencedor(tabuleiro)
        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            reiniciarJogo()
            return
        }


        // Turno do bot
        val jogadaBot = jogadaDoBot()
        if (jogadaBot != null) {
            val (rX, rY) = jogadaBot
            tabuleiro[rX][rY] = "O"
            atualizarBotao(rX, rY)
        }


        vencedor = verificaVencedor(tabuleiro)
        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            reiniciarJogo()
        }
    }


    fun jogadaDoBot(): Pair<Int, Int>? {
        val jogadaVencedora = verificaJogadaVencedora(tabuleiro, "O")
        val jogadaBloqueio = verificaJogadaVencedora(tabuleiro, "X")
        return jogadaVencedora ?: jogadaBloqueio ?: jogadaAleatoria()
    }


    fun jogadaAleatoria(): Pair<Int, Int>? {
        var rX: Int
        var rY: Int
        var i = 0
        do {
            rX = Random.nextInt(0, 3)
            rY = Random.nextInt(0, 3)
            i++
        } while (i < 9 && tabuleiro[rX][rY] != "")
        return if (tabuleiro[rX][rY] == "") Pair(rX, rY) else null
    }


    fun atualizarBotao(rX: Int, rY: Int) {
        when (rX * 3 + rY) {
            0 -> {
                binding.buttonZero.setBackgroundColor(Color.RED)
                binding.buttonZero.isEnabled = false
            }
            1 -> {
                binding.buttonUm.setBackgroundColor(Color.RED)
                binding.buttonUm.isEnabled = false
            }
            2 -> {
                binding.buttonDois.setBackgroundColor(Color.RED)
                binding.buttonDois.isEnabled = false
            }
            3 -> {
                binding.buttonTres.setBackgroundColor(Color.RED)
                binding.buttonTres.isEnabled = false
            }
            4 -> {
                binding.buttonQuatro.setBackgroundColor(Color.RED)
                binding.buttonQuatro.isEnabled = false
            }
            5 -> {
                binding.buttonCinco.setBackgroundColor(Color.RED)
                binding.buttonCinco.isEnabled = false
            }
            6 -> {
                binding.buttonSeis.setBackgroundColor(Color.RED)
                binding.buttonSeis.isEnabled = false
            }
            7 -> {
                binding.buttonSete.setBackgroundColor(Color.RED)
                binding.buttonSete.isEnabled = false
            }
            8 -> {
                binding.buttonOito.setBackgroundColor(Color.RED)
                binding.buttonOito.isEnabled = false
            }
        }
    }


    fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        for (i in 0..2) {
            // Verificar linhas
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != "") return tabuleiro[i][0]
            // Verificar colunas
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i] != "") return tabuleiro[0][i]
        }
        // Verificar diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != "") return tabuleiro[0][0]
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != "") return tabuleiro[0][2]


        // Verificar empate
        if (tabuleiro.all { linha -> linha.all { it != "" } }) return "Empate"


        return null
    }


    fun verificaJogadaVencedora(tabuleiro: Array<Array<String>>, jogador: String): Pair<Int, Int>? {
        for (i in 0..2) {
            // Verificar linhas
            if (tabuleiro[i][0] == jogador && tabuleiro[i][1] == jogador && tabuleiro[i][2] == "") return Pair(i, 2)
            if (tabuleiro[i][0] == jogador && tabuleiro[i][2] == jogador && tabuleiro[i][1] == "") return Pair(i, 1)
            if (tabuleiro[i][1] == jogador && tabuleiro[i][2] == jogador && tabuleiro[i][0] == "") return Pair(i, 0)
            // Verificar colunas
            if (tabuleiro[0][i] == jogador && tabuleiro[1][i] == jogador && tabuleiro[2][i] == "") return Pair(2, i)
            if (tabuleiro[0][i] == jogador && tabuleiro[2][i] == jogador && tabuleiro[1][i] == "") return Pair(1, i)
            if (tabuleiro[1][i] == jogador && tabuleiro[2][i] == jogador && tabuleiro[0][i] == "") return Pair(0, i)
        }
        // Verificar diagonais
        if (tabuleiro[0][0] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][2] == "") return Pair(2, 2)
        if (tabuleiro[0][0] == jogador && tabuleiro[2][2] == jogador && tabuleiro[1][1] == "") return Pair(1, 1)
        if (tabuleiro[1][1] == jogador && tabuleiro[2][2] == jogador && tabuleiro[0][0] == "") return Pair(0, 0)
        if (tabuleiro[0][2] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][0] == "") return Pair(2, 0)
        if (tabuleiro[0][2] == jogador && tabuleiro[2][0] == jogador && tabuleiro[1][1] == "") return Pair(1, 1)
        if (tabuleiro[1][1] == jogador && tabuleiro[2][0] == jogador && tabuleiro[0][2] == "") return Pair(0, 2)
        return null
    }


    fun reiniciarJogo() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
