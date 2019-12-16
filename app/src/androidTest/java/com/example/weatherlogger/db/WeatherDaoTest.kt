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
package com.example.weatherlogger.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.example.weatherlogger.models.Weather
import com.example.weatherlogger.utils.LiveDataTestUtil
import com.example.weatherlogger.utils.MockTestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherDaoTest : DBTest() {
  @Rule
  @JvmField
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var weather: Weather

  @Before
  fun initMock() {
    weather = MockTestUtil.mockWeather()
  }

  @Test
  fun insertTest() {
    MockTestUtil.calendar.set(1900, 0, 0)
    val expectedDate = MockTestUtil.calendar.time

    db.weatherDao().insertWeather(weather)

    val loaded = LiveDataTestUtil.getValue(db.weatherDao().selectWeather())
    assertThat(loaded.at.time, `is`(expectedDate.time))
    assertThat(loaded.temperature, `is`(0))
  }
}
