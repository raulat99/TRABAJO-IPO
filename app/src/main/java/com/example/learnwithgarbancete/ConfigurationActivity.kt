package com.example.learnwithgarbancete

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.*
import android.content.pm.*
import android.content.res.Configuration
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.*
import java.io.*
import java.util.*
import kotlin.collections.HashMap


class ConfigurationActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var leftLanguage: Button
    lateinit var rightLanguage: Button
    lateinit var leftDalt: Button
    lateinit var rightDalt: Button
    lateinit var language: TextView
    lateinit var settings: SharedPreferences
    lateinit var idioma: String
    lateinit var colorblind: TextView
    lateinit var daltonico: String
    lateinit var vpd: Button
    lateinit var edit: SharedPreferences.Editor


    lateinit var garbancete: ImageView
    lateinit var switchVisual: Switch
    lateinit var textGarbancete: TextView
    lateinit var texttospeech: TextToSpeech
    lateinit var auxText: String
    lateinit var fondo: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration2)

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

        button = findViewById(R.id.buttonBack)
        vpd = findViewById(R.id.RestoreDefaultSettingsBtn)
        leftLanguage = findViewById(R.id.leftLanguageMode)
        rightLanguage = findViewById(R.id.rightLanguageMode)
        leftDalt = findViewById(R.id.leftDaltMode)
        rightDalt = findViewById(R.id.rightDaltMode)
        language = findViewById(R.id.languageSelect)
        colorblind = findViewById(R.id.colorBlindnessMode)
        garbancete = findViewById(R.id.garbanConf)
        switchVisual = findViewById(R.id.switchVisual)
        textGarbancete =  findViewById(R.id.textView)
        fondo = findViewById(R.id.fondo)

        cargarpreferencias()
        edit = settings.edit()

        button.setOnClickListener() {
            val intent: Intent = Intent(this, MainActivity::class.java)
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.back).toString(), TextToSpeech.QUEUE_ADD, null);

            startActivity(intent)
        }


        leftLanguage.setOnClickListener() {
            setLanguage()
        }

        rightLanguage.setOnClickListener() {
            setLanguage()
        }

        rightDalt.setOnClickListener() {
            swapDalt()

        }

        leftDalt.setOnClickListener() {
            swapDalt()
        }

        vpd.setOnClickListener() {
            establecerValoresPorDefecto()
        }

        garbancete.setOnClickListener() {
            activarTutorial()
        }

        switchVisual.setOnCheckedChangeListener(){_, isCheked ->
            if(isCheked){
                textGarbancete.setText(getText(R.string.activaAsist))
                auxText = textGarbancete.text.toString()
                texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);
                edit.putString("tts", "SI")
                edit.commit()
            }else{
                textGarbancete.setText(getText(R.string.desactivaAsist))
                auxText = textGarbancete.text.toString()
                texttospeech.speak(auxText, TextToSpeech.QUEUE_ADD, null);
                edit.putString("tts", "NO")
                edit.commit()
            }
        }


        checkTutorial()
    }

    fun checkTutorial() {
        var tuto = settings.getString("tutorial", "si").toString()
        println(tuto)
        if (tuto.equals("si")) {
            activarTutorial()
        }
    }

    fun activarTutorial() {
        edit.putString("tutorial", "no")
        edit.commit()

        val intent: Intent = Intent(this, Tutorial::class.java)
        startActivity(intent)
    }

    fun swapDalt() {
        if (colorblind.getText().equals("Normal")) {
            cargarDaltonico("Protanomalia")
            guardarpreferenciasDalt("Protanomalia")
        } else if (colorblind.getText().equals("Protanomalia")) {
            cargarDaltonico("Deuteronomalia")
            guardarpreferenciasDalt("Deuteronomalia")
        }else if (colorblind.getText().equals("Deuteronomalia")) {
            cargarDaltonico("Tritanomalia")
            guardarpreferenciasDalt("Tritanomalia")
        }else if (colorblind.getText().equals("Tritanomalia")) {
            cargarDaltonico("Normal")
            guardarpreferenciasDalt("Normal")
        }
    }

    fun cargarpreferencias() {
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        idioma = settings.getString("idioma", "ENGLISH").toString()
        chooseLanguage(idioma)

        daltonico = settings.getString("daltonico", "Normal").toString()
        cargarDaltonico2(daltonico)

        switchVisual.isChecked = (settings.getString("tts","NO").equals("SI"))
    }

    fun cargarDaltonico(daltonico: String) {


        if (daltonico.equals("Normal")) {
            colorblind.setText("Normal")
            aplicarTipoDaltonismo("Normal")
            textGarbancete.setText(getText(R.string.desactivaColor).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.desactivaColor).toString(), TextToSpeech.QUEUE_ADD, null);
        } else if (daltonico.equals("Protanomalia")){
            colorblind.setText("Protanomalia")
            aplicarTipoDaltonismo("Protanomalia")
            textGarbancete.setText(getText(R.string.activaColorProtanomalia).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.activaColorProtanomalia).toString(), TextToSpeech.QUEUE_ADD, null);
        }else if (daltonico.equals("Deuteronomalia")){
            colorblind.setText("Deuteronomalia")
            aplicarTipoDaltonismo("Deuteronomalia")
            textGarbancete.setText(getText(R.string.activaColorDeuteronomalia).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.activaColorDeuteronomalia).toString(), TextToSpeech.QUEUE_ADD, null);
        } else if (daltonico.equals("Tritanomalia")){
            colorblind.setText("Tritanomalia")
            aplicarTipoDaltonismo("Tritanomalia")
            textGarbancete.setText(getText(R.string.activaColorTritanomalia).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.activaColorTritanomalia).toString(), TextToSpeech.QUEUE_ADD, null);
        }
    }

    fun cargarDaltonico2(daltonico: String) {


        if (daltonico.equals("Normal")) {
            colorblind.setText("Normal")
            aplicarTipoDaltonismo("Normal")
        } else if (daltonico.equals("Protanomalia")){
            colorblind.setText("Protanomalia")
            aplicarTipoDaltonismo("Protanomalia")
        }else if (daltonico.equals("Deuteronomalia")){
            colorblind.setText("Deuteronomalia")
            aplicarTipoDaltonismo("Deuteronomalia")
        } else if (daltonico.equals("Tritanomalia")){
            colorblind.setText("Tritanomalia")
            aplicarTipoDaltonismo("Tritanomalia")
        }
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


        garbancete.colorFilter = ColorMatrixColorFilter(matrix)
        button.background.colorFilter = ColorMatrixColorFilter(matrix)
        fondo.colorFilter = ColorMatrixColorFilter(matrix)
        vpd.background.colorFilter = ColorMatrixColorFilter(matrix)
        leftLanguage.background.colorFilter = ColorMatrixColorFilter(matrix)
        rightLanguage.background.colorFilter = ColorMatrixColorFilter(matrix)
        leftDalt.background.colorFilter = ColorMatrixColorFilter(matrix)
        rightDalt.background.colorFilter = ColorMatrixColorFilter(matrix)
    }

    fun cargarpreferencias2(): String {

        idioma = settings.getString("idioma", "").toString()
        return idioma
    }

    fun establecerValoresPorDefecto() {
        chooseLanguage("ENGLISH")
        cargarDaltonico("Normal")
        edit.putString("daltonico", "Normal")
        edit.commit()

        switchVisual.isChecked = false;
        edit.putString("tts", "NO")
        edit.commit()
        println(settings.getString("tts","NOF"))
    }

    fun guardarpreferenciasIdioma(l: String) {
        var edit = settings.edit()
        edit.putString("idioma", l)
        edit.commit()
        edit.apply()

    }

    fun guardarpreferenciasDalt(l: String) {
        var edit = settings.edit()
        edit.putString("daltonico", l)
        edit.commit()
        edit.apply()

    }


    fun setLanguage() {
        val l = language.getText()

        if (l.equals("ESPAÑOL")) {
            chooseLanguage("ENGLISH")
            texttospeech.setLanguage(Locale.ENGLISH)
            textGarbancete.setText(getText(R.string.ChangeEnglish).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.ChangeEnglish).toString(), TextToSpeech.QUEUE_ADD, null)


        } else if (l.equals("ENGLISH")) {
            chooseLanguage("ESPAÑOL")
            val locSpanish = Locale("spa", "ES")

            texttospeech.setLanguage(locSpanish)
            textGarbancete.setText(getText(R.string.ChangeSpanish).toString())
            if(settings.getString("tts","NO").equals("SI")) texttospeech.speak(getText(R.string.ChangeSpanish).toString(), TextToSpeech.QUEUE_ADD, null);

        } else {
            language.setText(cargarpreferencias2())
            cargarpreferencias()
        }

        val intent: Intent = Intent(this, Auxiliar::class.java)
        startActivity(intent)
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

        guardarpreferenciasIdioma(l)
        language.setText(l)
    }
}



