/*
 * The MIT License (MIT)
 *
 * Designed and developed by 2018 skydoves (Jaewoong Eum)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.example.weatherlogger.utils

import com.example.weatherlogger.models.*
import java.util.*


class MockTestUtil {
    companion object {

        val calendar = Calendar.getInstance()

        fun mockTime(): Date {
            calendar.set(1900, 0, 0)
            return calendar.time
        }

        fun mockWeather(): Weather {
            return Weather(0, mockTime())
        }

        fun mockWeatherResponse(): WeatherResponse {
            return WeatherResponse(
                coord=Coord(lon=76.85, lat=43.22),
                weather=listOf(WeatherData(id=701, main="Mist", description="mist", icon="50d")),
                base="stations",
                main=Main(temp=-6, pressure=1030, humidity=92, temp_min=-6, temp_max=-6),
                visibility=2600,
                wind=Wind(speed=2, deg=250),
                clouds=Clouds(all=90), dt=1576478203,
                sys=Sys(type=1, id=8818, country="KZ", sunrise=1576462683, sunset=1576495077),
                timezone=21600,
                id=1524193,
                name="Gornyy Gigant",
                cod=200)
        }
    }
}