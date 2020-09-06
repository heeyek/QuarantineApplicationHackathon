package com.example.quarantinelogin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quarantinelogin.LoginActivity
import com.example.quarantinelogin.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONException
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeFragment : Fragment() {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    var client = OkHttpClient()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val phoneNumber = LoginActivity.Companion.user?.getJSONObject("Item")?.getJSONObject("phoneNumber")?.getString("S")
        getQuarantineEndTime(phoneNumber, object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Request Failure.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                if (body != null) {
                    val QuarantineEndTimeInMilliseconds = body.toLong()

                    activity?.runOnUiThread {
                        try {
                            // TODO Get Time Left = QuarantineEndTimeInMilliseconds - CURRENT TIME IN MILLISECONDS

                            // TODO DISPLAY TIME LEFT IN TEXT FORMAT ON SCREEN

                            println("Request Successful to Get Quarantine Time!")
                            println(QuarantineEndTimeInMilliseconds)
                            Toast.makeText(
                                activity,
                                "Created Geofence Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity,
                                "An Error occurred while creating the Geofence. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
        return inflater.inflate(R.layout.fragment_time, container, false)
    }


    @Throws(IOException::class)
    fun getQuarantineEndTime(phoneNumber: String?, callback: Callback): Unit {
        val request = Request.Builder()
            .url("https://e2d600v4b3.execute-api.us-east-1.amazonaws.com/dev/user/$phoneNumber/quarantineTimer")
            .get()
            .build()

        return client.newCall(request).enqueue(callback)
    }
}