package com.hamyarehealth.ir.ui.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamyarehealth.ir.platform.core.common.JalaliDate
import com.hamyarehealth.ir.platform.core.common.toPersianDigits
import com.hamyarehealth.ir.ui.AppTypography
import com.hamyarehealth.ir.ui.home.CalendarOccasions
import com.hamyarehealth.ir.ui.home.ProfileClockAvatar
import com.hamyarehealth.ir.ui.home.SubscriptionChip
import com.hamyarehealth.ir.ui.hub.hubTo
import com.hamyarehealth.ir.ui.navigation.Screen
import com.hamyarehealth.ir.ui.profile.StudentProfileState
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime

private val gregMonthFa = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec",
)

/**
 * کارتِ «تاریخ و ساعت» داشبورد — همان بلوکِ داشبوردِ اپِ قبلی («همیار من»):
 * تاریخ شمسی، ساعتِ ۱۲ساعته با «قبل/بعد از ظهر»، تاریخ میلادی، ساعتِ آنالوگِ
 * زنده روی آواتار، برچسبِ اشتراک و خطِ مناسبت‌های امروز.
 *
 * دو تفاوتِ عمدی با اپِ قبلی، در حدِ متن:
 *  - به‌جای «تعطیلی مدرسه» پنجشنبه، فقط «جمعه — تعطیل هفتگی» نوشته می‌شود.
 *  - لمسِ تاریخ، تقویمِ شمسی/قمری/میلادی را باز می‌کند (بدونِ تب‌های مدرسه).
 */
@Composable
fun DashboardTodayCard(nav: NavController, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1_000)
        }
    }
    val time = remember(now) { LocalDateTime.ofInstant(Instant.ofEpochMilli(now), JalaliDate.TEHRAN) }
    val jalali = remember(now) { JalaliDate.toJalali(now) }
    val iso = remember(now) { JalaliDate.todayIso() }
    val weekday = JalaliDate.weekDayFa(iso)
    val row1 = toPersianDigits("$weekday ${jalali.day} ${JalaliDate.monthName(jalali.month)} ${jalali.year}")
    val h24 = time.hour
    val h12 = if (h24 % 12 == 0) 12 else h24 % 12
    val period = if (h24 < 12) "قبل از ظهر" else "بعد از ظهر"
    val row2Time = toPersianDigits("%d:%02d".format(h12, time.minute)) + " " + period
    val row2Greg = "${time.year}/${gregMonthFa[time.monthValue - 1]}/${time.dayOfMonth}"
    val holiday = CalendarOccasions.dashboardLine(ctx, jalali)
    val restNotice = if (time.dayOfWeek == DayOfWeek.FRIDAY) "جمعه — تعطیل هفتگی" else null
    val occasionText = listOfNotNull(restNotice, holiday?.takeIf { it.isNotBlank() })
        .joinToString(" · ")
        .ifBlank { "بدون مناسبت" }
    val openCalendar: () -> Unit = { nav.hubTo(Screen.ClassPlanCalendar.route) }

    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier.weight(1f).clickable(onClick = openCalendar),
                    contentAlignment = Alignment.Center,
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                        ) {
                            Text(
                                row1,
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = AppTypography.d3Date.family,
                                fontWeight = AppTypography.d3Date.weight,
                                fontSize = AppTypography.d3Date.size,
                            )
                            Text(
                                row2Time,
                                style = MaterialTheme.typography.headlineSmall,
                                fontFamily = AppTypography.d4Clock.family,
                                fontWeight = AppTypography.d4Clock.weight,
                                fontSize = AppTypography.d4Clock.size,
                            )
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                Text(
                                    row2Greg,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = AppTypography.d5Gregorian.family,
                                    fontWeight = AppTypography.d5Gregorian.weight,
                                    fontSize = AppTypography.d5Gregorian.size,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileClockAvatar(onClick = { nav.hubTo(Screen.UserProfile.route) })
                    Spacer(Modifier.height(6.dp))
                    SubscriptionChip(StudentProfileState.subscription) {
                        nav.hubTo(Screen.Subscription.route)
                    }
                }
            }
        }

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = HealthPalette.Mint.copy(alpha = 0.14f),
            ),
        ) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text("مناسبت‌های امروز", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    occasionText,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = HealthPalette.MintDeep,
                    modifier = Modifier.clickable(onClick = openCalendar),
                )
            }
        }
    }
}
