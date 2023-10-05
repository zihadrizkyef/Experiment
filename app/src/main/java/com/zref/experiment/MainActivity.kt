package com.zref.experiment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.style.LineBackgroundSpan
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.zref.experiment.databinding.ActivityMainBinding
import com.zref.experiment.databinding.DialogCalendarBinding
import java.util.Calendar
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val calendar = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()
            calendar2.add(Calendar.MONTH, 4)

            val dialog = DialogCalendarBinding.inflate(layoutInflater)
            dialog.calendarView.state()
                .edit()
                .setMinimumDate(
                    CalendarDay.from(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                )
                .setMaximumDate(
                    CalendarDay.from(
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH) + 1,
                        calendar2.get(Calendar.DAY_OF_MONTH)
                    )
                )
                .commit()
            dialog.calendarView.setOnDateChangedListener { _, date, selected ->
                Toast.makeText(this, "$date $selected", Toast.LENGTH_SHORT).show()
            }
            dialog.calendarView.setOnMonthChangedListener { _, date ->
                Toast.makeText(this, "$date", Toast.LENGTH_SHORT).show()
            }
            dialog.calendarView.selectedDate = CalendarDay.today()

            val listCalendar = arrayListOf<CalendarDay>()
            repeat(5) {
                calendar.add(Calendar.DAY_OF_MONTH, Random.nextInt(10))
                listCalendar.add(
                    CalendarDay.from(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                )
            }
            dialog.calendarView.addDecorator(ReservedDecorator(listCalendar))

            val location = IntArray(2)
            it.getLocationInWindow(location)

            val popup = PopupWindow(dialog.root, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            popup.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popup.elevation = 20F
            popup.isOutsideTouchable = true
            popup.isFocusable = true
            popup.showAsDropDown(it)

            dialog.root.post {
                popup.update(
                    binding.button,
                    (-dialog.root.measuredWidth / 2) + (binding.button.measuredWidth / 2),
                    0,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                )
            }
        }
    }

    class ReservedDecorator(private val dates: Collection<CalendarDay>?) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean = dates?.contains(day) == true
        override fun decorate(view: DayViewFacade) {
            view.addSpan(ReservedSpan())
        }
    }

    class ReservedSpan : LineBackgroundSpan {
        private val radius = 5F
        private val color = Color.RED

        override fun drawBackground(
            canvas: Canvas, paint: Paint,
            left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
            charSequence: CharSequence,
            start: Int, end: Int, lineNum: Int
        ) {
            val oldColor = paint.color
            paint.color = color
            canvas.drawCircle(right - radius - 15, top + radius, radius, paint)
            paint.color = oldColor
        }
    }
}