package com.example.weatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    val CITY: String = "Krakow"
    val API : String = "API_KEY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        weatherTask().execute()
    }
    inner class weatherTask() : AsyncTask<String, Void ,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<RelativeLayout>(R.id.MainContainer).visibility = View.GONE

        }
        override fun doInBackground(vararg params: String?): String? {
            var response :String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8)
            }
            catch (e : Exception){
                response = null
            }
            return response
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0).getString("main")
                val temp = main.getString("temp").dropLast(1) + "°C"
                val adress = jsonObj.getString("name") +", "+ sys.getString("country")
                val updatedAt : Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at : " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp_min = "Min temp " + main.getString("temp_min").dropLast(1) + "°C"
                val temp_max = "Max temp " + main.getString("temp_max").dropLast(1) + "°C"
                val sunrise : Long = sys.getLong("sunrise")
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.Location).text = adress
                findViewById<TextView>(R.id.maxTemp).text = temp_max
                findViewById<TextView>(R.id.minTemp).text = temp_min
                findViewById<TextView>(R.id.Sunrise).text = SimpleDateFormat("hh:mm a" , Locale.ENGLISH).format(Date(sunrise))
                findViewById<TextView>(R.id.updatedAt).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weather.toString()

                findViewById<RelativeLayout>(R.id.MainContainer).visibility = View.VISIBLE
            }
            catch (e: Exception){
                findViewById<RelativeLayout>(R.id.MainContainer).visibility = View.VISIBLE
            }
        }
    }

}
