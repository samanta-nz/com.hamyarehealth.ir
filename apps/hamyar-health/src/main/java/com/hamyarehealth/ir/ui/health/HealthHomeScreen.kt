package com.hamyarehealth.ir.ui.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamyarehealth.ir.LocalAppContainer
import com.hamyarehealth.ir.platform.core.common.JalaliDate
import com.hamyarehealth.ir.ui.hub.HubCoverGrid
import com.hamyarehealth.ir.ui.hub.hubTo
import com.hamyarehealth.ir.ui.hub.layerTo
import com.hamyarehealth.ir.ui.navigation.Screen
import com.hamyarehealth.ir.ui.profile.StudentProfileState
import com.hamyarehealth.ir.ui.water.WaterRepository
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * داشبورد «همیار سلامتی من» — صفحهٔ اولِ همان اپی که کاربر باز می‌کند.
 *
 * چیدمان از بالا به پایین:
 * ۱) سربرگِ گرادیانی با تاریخ شمسی و ساعتِ تهران
 * ۲) تاریخ شمسی/میلادی، ساعتِ ۱۲ساعته، ساعتِ آنالوگِ روی آواتار و خطِ مناسبت‌ها
 * ۳) «حال امروز» (آب/ورزش/روتین) با دکمه‌های سریع
 * ۴) ردیاب سریعِ آب
 * ۵) اقدام‌های سریعِ آرامش (تنفس، ذهن‌آگاهی، کسب آرامش، بین دروس)
 * ۶) کارت‌های سلامتی (همان کارت‌های همیار سلامتی من)
 * ۷) جملهٔ امروز
 *
 * هیچ بخشی از منطقِ محتوا تغییر نکرده: همان باکت ParsPack و همان گزارش‌های روزانه.
 */
@Composable
fun HealthHomeScreen(nav: NavController) {
    val ctx = LocalContext.current
    val container = LocalAppContainer.current
    val daily = container.dailyHealth
    val water = remember { WaterRepository(container.store, container.sync) }
    var snapshot by remember { mutableStateOf(daily.snapshot()) }

    LaunchedEffect(Unit) {
        snapshot = daily.pullToday()
    }

    val girl = StudentProfileState.gender != "boy"
    val name = StudentProfileState.firstName.ifBlank { "دوست من" }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // ۱) سربرگِ اختصاصی این اپ
        HealthHero(
            title = "سلام $name 🌿",
            subtitle = "همیار سلامتی من — آرامش، تنفس، حرکت و خواب، همین‌جا",
            badge = TehranClock(),
        )

        // ۲) تاریخ و ساعتِ زنده + ساعتِ آنالوگِ آواتار (کپی از داشبورد اپ قبلی)
        DashboardTodayCard(nav)

        // ۳) حال امروز
        TodayHealthCard(
            snapshot = snapshot,
            onWaterDelta = { delta ->
                if (delta > 0) water.addGlass() else water.undoGlass()
                val state = water.state()
                snapshot = daily.recordWater(state.goal, state.consumed, delta)
            },
            onOpen = { key ->
                when (key) {
                    "water" -> nav.hubTo(Screen.Water.route)
                    "routine" -> nav.hubTo(Screen.Routine.route)
                    "breath" -> nav.hubTo(Screen.ContentCategory.of("breath"))
                    "yoga" -> nav.hubTo(Screen.ContentCategory.of("yoga"))
                    "sleep" -> nav.layerTo(Screen.PracticeGroup.of("hl-sleep"))
                    else -> nav.hubTo(Screen.HealthProgress.route)
                }
            },
        )

        // ۴) ردیاب سریعِ آب — همان کارتِ داشبوردِ قبلی (لمس = یک لیوان)
        com.hamyarehealth.ir.ui.home.WaterQuickCard(onOpen = { nav.hubTo(Screen.Water.route) })

        // ۵) اقدام‌های سریعِ آرامش و ذهن
        HealthSectionTitle("اقدام سریع", "⚡")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthQuickAction("🌬️", "تمرین تنفسی", Modifier.weight(1f)) {
                nav.hubTo(Screen.ContentCategory.of("breath"))
            }
            HealthQuickAction("🌙", "کسب آرامش", Modifier.weight(1f)) {
                nav.hubTo(Screen.CalmHub.route)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthQuickAction("🪷", "ذهن‌آگاهی", Modifier.weight(1f)) {
                nav.hubTo(Screen.AwarenessHub.route)
            }
            HealthQuickAction("🎧", "صداهای آرام", Modifier.weight(1f), HealthPalette.Sand) {
                nav.hubTo(Screen.BackgroundMusic.route)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthQuickAction("✈️", "سفر ذهنی", Modifier.weight(1f), HealthPalette.Sand) {
                nav.layerTo(Screen.PracticeGroup.of("cl-journey"))
            }
            HealthQuickAction("⚡", "بین دروس", Modifier.weight(1f)) {
                nav.hubTo(Screen.BetweenLessons.route)
            }
        }

        // ۶) کارت‌های سلامتی
        HealthSectionTitle("سلامتی من", "💚")
        HubCoverGrid(healthTiles(nav, includePeriod = girl))

        // ۷) جملهٔ امروز
        WisdomOfToday()

        Spacer(Modifier.height(6.dp))
    }
}

/** ساعتِ تهران با رقم فارسی — همان قاعده‌ی سراسریِ اپ. */
@Composable
private fun TehranClock(): String {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1_000)
        }
    }
    val local = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), JalaliDate.TEHRAN)
    return JalaliDate.toPersianDigits(local.format(DateTimeFormatter.ofPattern("HH:mm")))
}

@Composable
private fun WisdomOfToday() {
    val ctx = LocalContext.current
    var all by remember { mutableStateOf(com.hamyarehealth.ir.ui.home.WisdomQuotes.load(ctx)) }
    var line by remember { mutableStateOf(com.hamyarehealth.ir.ui.home.WisdomQuotes.current(ctx, all)) }
    LaunchedEffect(Unit) {
        val ok = runCatching { com.hamyarehealth.ir.ui.home.WisdomQuotes.refresh(ctx) }.getOrDefault(false)
        if (ok) {
            all = com.hamyarehealth.ir.ui.home.WisdomQuotes.load(ctx)
            line = com.hamyarehealth.ir.ui.home.WisdomQuotes.current(ctx, all)
        }
    }
    val shape = RoundedCornerShape(22.dp)
    Card(
        Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("جملهٔ امروز", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(line.oneLine(), style = MaterialTheme.typography.bodyMedium)
            Text(
                "${JalaliDate.weekDayFa(JalaliDate.todayIso())}، ${JalaliDate.formatFaLong(System.currentTimeMillis())}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
    }
}
