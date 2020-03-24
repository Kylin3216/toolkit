package tech.dreamfund.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LunarKit {
    private lateinit var result: IntArray
    val calendar: Calendar

    constructor() {
        calendar = Calendar.getInstance(
            Locale.SIMPLIFIED_CHINESE
        )
        convert()
    }

    constructor(year: Int, month: Int, day: Int) {
        calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        convert()
    }

    constructor(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int) {
        calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hourOfDay, minute)
        convert()
    }

    constructor(date: Date) {
        calendar = Calendar.getInstance()
        calendar.time = date
        convert()
    }

    constructor(calendar: Calendar) {
        this.calendar = calendar
        convert()

    }


    /**
     * 转换的结果集.year .month .day .isLeap .yearCyl .dayCyl .monCyl
     */


    companion object {
        private val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        private val lunarInfo = longArrayOf(
            0x04bd8, 0x04ae0, 0x0a570, 0x054d5,
            0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0,
            0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2,
            0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40,
            0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
            0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7,
            0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0,
            0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355,
            0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,
            0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0,
            0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0,
            0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46,
            0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,
            0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954,
            0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,
            0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,
            0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50,
            0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
            0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6,
            0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0,
            0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
        )
        private val sTermInfo = longArrayOf(
            0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149,
            195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350,
            375494, 397447, 419210, 440795, 462224, 483532, 504758
        )
        private val solarTerm = arrayOf(
            "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
            "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
            "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
            "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
        )
        private val TG = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
        private val DZ = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
        private val SX = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
        private val DAY1 = arrayOf("日", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十")
        private val DAY2 = arrayOf("初", "十", "廿", "卅", "　")
        private val MONTH = arrayOf("正", "正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")
        private val YEAR = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        /**
         * 键值：0-7位表示日期，8-11位表示月份，5-7位与12-15位恒为0
         */
        private val lunarHolidayMap = mapOf(
            0x0101 to "春节", 0x010f to "元宵", 0x0505 to "端午", 0x0707 to "七夕",
            0x070f to "中元", 0x080f to "中秋", 0x0909 to "重阳", 0x0c08 to "腊八",
            0x0c18 to "小年", 0x0c1e to "除夕"
        )
        /**
         * 键值：0-7位表示日期，8-11位表示月份，5-7位与12-15位恒为0
         */
        private val holidayMap = mapOf(
            0x0101 to "元旦", 0x020e to "情人节", 0x0308 to "妇女节", 0x030c to "植树节",
            0x030f to "消费者权益日", 0x0401 to "愚人节", 0x0501 to "劳动节", 0x0504 to "青年节",
            0x050c to "护士节", 0x0601 to "儿童节", 0x0701 to "建党节", 0x0801 to "建军节",
            0x0808 to "爸爸节", 0x090a to "教师节", 0x091c to "孔子诞辰", 0x0a01 to "国庆节",
            0x0a06 to "老人节", 0x0a18 to "联合国日", 0x0b0c to "孙中山诞辰纪念",
            0x0c14 to "澳门回归纪念", 0x0c19 to "圣诞"
        )

        /**
         * 农历年份的总天数
         * @param year  农历年份
         * @return
         */
        private fun totalDaysOfYear(year: Int): Int {
            var sum = 348 // 29*12
            var i = 0x8000L
            while (i > 0x8) {
                sum += if (lunarInfo[year - 1900] and i == 0L) 0 else 1
                i = i shr 1
            }
            return sum + leapDays(year)
        }

        /**
         * 农历 year年闰月的天数
         * @param year
         * @return
         */
        private fun leapDays(year: Int): Int {
            var result = 0
            if (leapMonth(year) != 0) {
                result = if (lunarInfo[year - 1900] and 0x10000 == 0L) 29 else 30
            }
            return result
        }

        /**
         * 传农历 year年闰哪个月 1-12 , 没闰传回 0
         * @param year 农历年份
         * @return
         */
        private fun leapMonth(year: Int): Int {
            return lunarInfo[year - 1900].toInt() and 0xf
        }


        /**
         * 农历 y年m月的总天数
         * @param y
         * @param m
         * @return
         */
        private fun monthDays(y: Int, m: Int): Int {
            return if (lunarInfo[y - 1900] and (0x10000 shr m).toLong() == 0L) 29 else 30
        }

        /**
         * 获取偏移量对应的干支, 0=甲子
         * @param num 偏移量（年or月or日）
         * @return
         */
        private fun cyclical(num: Int) = TG[num % 10] + DZ[num % 12]

        /**
         * 中文日期
         * @param day
         * @return
         */
        private fun chineseDay(day: Int): String {
            return when (day) {
                10 -> "初十"
                20 -> "二十"
                30 -> "三十"
                else -> DAY2[day / 10] + DAY1[day % 10]
            }
        }

        /**
         * 大写年份
         * @param year
         * @return
         */
        private fun chineseYear(year: Int): String {
            var s = " "
            var d: Int
            var y = year
            while (y > 0) {
                d = y % 10
                y = (y - d) / 10
                s = YEAR[d] + s
            }
            return s
        }

        /**
         * y年的第n个节气为几日(从0小寒起算)
         */
        private fun sTerm(y: Int, n: Int): Int {
            val cal = Calendar.getInstance()
            cal.set(1900, 0, 6, 2, 5, 0)
            val temp = cal.time.time
            cal.timeInMillis = ((31556925974.7 * (y - 1900) + sTermInfo[n] * 60000L) + temp).toLong()
            return cal.get(Calendar.DAY_OF_MONTH)
        }

    }

    private fun convert() {
        // 基准时间 1900-01-31是农历1900年正月初一
        val baseCalendar = Calendar.getInstance()
        baseCalendar.set(1900, 0, 31, 0, 0, 0)// 1900-01-31是农历1900年正月初一
        val baseDate = baseCalendar.time
        // 偏移量（天）
        var offset = ((calendar.timeInMillis - baseDate.time) / 86400000).toInt()// 天数(86400000=24*60*60*1000)
        // 基准时间在天干地支纪年法中的位置
        var monCyl = 14 // 1898-10-01是农历甲子月
        val dayCyl = offset + 40 // 1899-12-21是农历1899年腊月甲子日
        // 得到年数
        var i = 1900
        var temp = 0
        while (i < 2050 && offset > 0) {
            temp = totalDaysOfYear(i) // 农历每年天数
            offset -= temp
            monCyl += 12
            i++
        }
        if (offset < 0) {
            offset += temp
            i--
            monCyl -= 12
        }
        val year = i // 农历年份
        val yearCyl = i - 1864 // 1864年是甲子年
        val leap = leapMonth(i) // 闰哪个月
        var isLeap = false
        var j = 1
        while (j < 13 && offset > 0) {
            // 闰月
            if (leap > 0 && j == (leap + 1) && !isLeap) {
                --j
                isLeap = true
                temp = leapDays(year)
            } else {
                temp = monthDays(year, j)
            }
            // 解除闰月
            if (isLeap && j == (leap + 1))
                isLeap = false
            offset -= temp
            if (!isLeap)
                monCyl++
            j++
        }
        if (offset == 0 && leap > 0 && j == leap + 1)
            if (isLeap) {
                isLeap = false
            } else {
                isLeap = true
                --j
                --monCyl
            }
        if (offset < 0) {
            offset += temp
            --j
            --monCyl
        }
        val month = j // 农历月份
        val day = offset + 1 // 农历天
        result = intArrayOf(year, month, day, if (isLeap) 1 else 0, yearCyl, monCyl, dayCyl)
    }

    /**
     * 输出格式：2015.07.04 周六 乙未{羊}年 壬午月 辛巳日
     * @return
     */
    fun getLunarDate(): String {
        var s = sdf.format(calendar.time) + " "
        s += cyclical(result[4]) + "[" + SX[(result[0] - 4) % 12] + "]年 "
        s += cyclical(result[5]) + "月 "
        s += cyclical(result[6]) + "日"
        return s
    }

    /**
     * 输出格式：五月十九
     * @return
     */
    fun getLunarDay() = if (result[3] == 1) "闰" else "" + MONTH[result[1]] + "月" + chineseDay(result[2])

    fun getLunarMonth() = if (result[3] == 1) "闰" else "" + MONTH[result[1]] + "月"

    fun getLunarDayOfMonth() = chineseDay(result[2])

    /**
     * 获取时辰，输出格式：戊子时
     *
     * @return
     */
    fun getLunarTime(): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val timeOffset = (result[6] % 10) * 24 + hour
        return TG[((timeOffset + 1) / 2) % 10] + DZ[((hour + 1) / 2) % 12] + "时"
    }

    /**
     * 核心方法 根据日期(y年m月d日)得到节气
     */
    fun getSolarTerm(): String? {
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH)
        return when (calendar.get(Calendar.DAY_OF_MONTH)) {
            sTerm(y, m * 2) -> solarTerm[m * 2]
            sTerm(y, m * 2 + 1) -> solarTerm[m * 2 + 1]
            else -> null
        }
    }


    /**
     * 农历节日
     *
     * @return
     */
    fun getLunarHoliday(): String? {
        val temp = if (result[2] == 29 && !isBigMonth(result[0], result[1])) {
            (result[1] shl 8) + 30
        } else {
            (result[1] shl 8) + result[2]
        }
        return lunarHolidayMap[temp]
    }

    /**
     * 公历节日
     * @return
     */
    fun getHoliday(): String? {
        val m = calendar.get(Calendar.MONTH) + 1
        val d = calendar.get(Calendar.DAY_OF_MONTH)
        val temp = (m shl 8) + d
        return holidayMap[temp]
    }

    /**
     * 判断m年y月是大月还是小月
     * @param y
     * @param m
     * @return true:大月，false:小月
     */
    private fun isBigMonth(y: Int, m: Int): Boolean {
        return (lunarInfo[y - 1900] and (0x10000 shr m).toLong()) != 0L
    }

    fun dayOfWeek() = "星期${DAY1[calendar.get(Calendar.DAY_OF_WEEK) - 1]}"
    /**
     * 后一天
     */
    fun nextDay() {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        convert()
    }

    /**
     * 前一天
     */
    fun preDay() {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        convert()
    }
}