package com.example.learnwithgarbancete

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.ColorMatrixColorFilter
import android.os.*
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v7.app.*
import android.view.*
import android.widget.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var texto: TextView
    lateinit var maths: Button;
    lateinit var language : Button;
    lateinit var options : Button;
    lateinit var health: Button;
    lateinit var geometry: Button;
    lateinit var info: Button;
    lateinit var settings : SharedPreferences
    lateinit var conf : ConfigurationActivity
    lateinit var edit : SharedPreferences.Editor
    lateinit var texttospeech: TextToSpeech
    lateinit var idioma: String
    lateinit var image_background: ImageView
    lateinit var bocadillo: ImageView
    lateinit var garbancete: Button
    lateinit var fondo : ImageView
    lateinit var tipodaltonico : String
    val RECOGNISE_SPEECH_ACTIVITY = 102



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settings = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)

       texttospeech = TextToSpeech(this, TextToSpeech.OnInitListener {

            idioma = settings.getString("idioma", "").toString()

            if(idioma.equals("ENGLISH")){
                texttospeech.setLanguage(Locale.ENGLISH)
            }

            if(idioma.equals("ESPAÑOL")){
                val locSpanish = Locale("spa", "ES")
                texttospeech.setLanguage(locSpanish)
            }
        })

        maths = findViewById(R.id.mathsButton)
        language = findViewById(R.id.languageButton)
        options = findViewById(R.id.configuration)
        conf = ConfigurationActivity()
        health = findViewById(R.id.health)
        geometry = findViewById(R.id.geometry)
        info = findViewById(R.id.info)
        image_background = findViewById(R.id.imageView)
        garbancete = findViewById(R.id.garbancete)
        bocadillo = findViewById(R.id.imageView6)
        fondo = findViewById(R.id.fondo)
        garbancete = findViewById(R.id.garbancete)

        tipodaltonico = settings.getString("daltonico", "").toString()
        aplicarTipoDaltonismo(tipodaltonico)

        language.setOnClickListener(){
            val auxText = language.text.toString()
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, LanguageGame::class.java)
            startActivity(intent)
        }

        options.setOnClickListener() {
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getString(R.string.options).toString(), TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        }

        maths.setOnClickListener() {
            val auxText = getString(R.string.maths_name2)
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, MathsGame::class.java)
            startActivity(intent)
        }
        health.setOnClickListener(){
            val auxText = health.text.toString()
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, HealthyUnhealthy::class.java)
            startActivity(intent)
        }
        geometry.setOnClickListener(){
            val auxText = geometry.text.toString()
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, GeometryGame::class.java)
            startActivity(intent)
        }
        info.setOnClickListener(){
            val auxText = getString(R.string.info)
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);

            val intent : Intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        garbancete.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                escucharComando()
                return false
            }
        })

    }

    fun escucharComando(){
        askSpeechInput()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RECOGNISE_SPEECH_ACTIVITY && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            procesarComando(result?.get(0).toString())
        }
    }

    fun procesarComando(result : String){
        if (result.contains(getString(R.string.maths_name), ignoreCase = true) || result.contains(getString(R.string.maths_name2), ignoreCase = true)){
            texttospeech.speak(getString(R.string.mathsVoz), TextToSpeech.QUEUE_ADD, null);
            val intent : Intent = Intent(this, MathsGame::class.java)
            startActivity(intent)
        } else if (result.contains(getString(R.string.idiomaChange), ignoreCase = true)){
            texttospeech.speak(getString(R.string.languageVoz), TextToSpeech.QUEUE_ADD, null);
            if(idioma.equals("ESPAÑOL")){
                chooseLanguage("ENGLISH")
            } else {
                chooseLanguage("ESPAÑOL")
            }
        } else if (result.contains(getString(R.string.health_name), ignoreCase = true)){
            texttospeech.speak(getString(R.string.healthVoz), TextToSpeech.QUEUE_ADD, null);
            val intent : Intent = Intent(this, HealthyUnhealthy::class.java)
            startActivity(intent)
        } else if (result.contains(getString(R.string.geometry_name), ignoreCase = true)){
            texttospeech.speak(getString(R.string.geometryoz), TextToSpeech.QUEUE_ADD, null);
            val intent : Intent = Intent(this, GeometryGame::class.java)
            startActivity(intent)
        } else if (result.contains(getString(R.string.conf_name), ignoreCase = true)){
            texttospeech.speak(getString(R.string.confVoz), TextToSpeech.QUEUE_ADD, null);
            val intent : Intent = Intent(this, ConfigurationActivity::class.java)
            startActivity(intent)
        } else if (result.contains(getString(R.string.language_name), ignoreCase = true)){
            texttospeech.speak(getString(R.string.languageVoz), TextToSpeech.QUEUE_ADD, null);
            val intent : Intent = Intent(this, LanguageGame::class.java)
            startActivity(intent)
        } else if (result.contains(getString(R.string.blindassitantmode), ignoreCase = true)){
            texttospeech.speak(getString(R.string.visualChange), TextToSpeech.QUEUE_ADD, null);
            edit.putString("tts", "SI")
            edit.commit()
        }
    }



    fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Your device does not support voice recognision", Toast.LENGTH_SHORT).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "${getString(R.string.idioma)}")
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "${getString(R.string.saysmth)}")
            startActivityForResult(i, RECOGNISE_SPEECH_ACTIVITY)
        }
    }

    fun cargarpreferencias() {
        var idioma = settings.getString("idioma", "ENGLISH").toString()
        chooseLanguage(idioma)
    }

    fun chooseLanguage(l: String) {
        var lang = ""
        var country = ""

        if (l.equals("ESPAÑOL")) {
            lang = "es"
            country = "ES"

        } else if (l.equals("ENGLISH")) {
            lang = "en"
            country = "EN"
        }


        val localizacion = Locale(lang, country)

        Locale.setDefault(localizacion)
        val config = Configuration()
        config.locale = localizacion
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    fun aplicarTipoDaltonismo(dalt: String) {

        val protanomalia = floatArrayOf(
            0.567f, 0.433f, 0.0f, 0.0f, 0f,
            0.558f, 0.442f, 0.0f, 0.0f, 0f,
            0.0f, 0.242f, 0.758f, 0.0f, 0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0f
        )

        val deuteronomalia = floatArrayOf(
            0.625f, 0.375f, 0.0f, 0.0f, 0f,
            0.7f, 0.3f, 0.0f, 0.0f, 0f,
            0.0f, 0.3f, 0.7f, 0.0f, 0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0f
        )

        val tritanomalia = floatArrayOf(
            0.95f, 0.05f, 0.0f, 0.0f, 0f,
            0.0f, 0.433f, 0.567f, 0.0f, 0f,
            0.0f, 0.475f, 0.525f, 0.0f, 0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0f
        )

        var matrix = floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )

        if (dalt.equals("Protanomalia")){
            matrix = protanomalia
        }else if(dalt.equals("Deuteronomalia")){
            matrix = deuteronomalia
        }else if(dalt.equals("Tritanomalia")){
            matrix = tritanomalia
        }

        fondo.colorFilter = ColorMatrixColorFilter(matrix)
        image_background.colorFilter = ColorMatrixColorFilter(matrix)
        bocadillo.colorFilter = ColorMatrixColorFilter(matrix)
        garbancete.background.colorFilter = ColorMatrixColorFilter(matrix)
        maths.background.colorFilter = ColorMatrixColorFilter(matrix)
        language.background.colorFilter = ColorMatrixColorFilter(matrix)
        options.background.colorFilter = ColorMatrixColorFilter(matrix)
        health.background.colorFilter = ColorMatrixColorFilter(matrix)
        geometry.background.colorFilter = ColorMatrixColorFilter(matrix)
        info.background.colorFilter = ColorMatrixColorFilter(matrix)

    }
}