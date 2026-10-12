package com.hamyarehealth.ir.ui.health

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamyarehealth.ir.platform.core.common.JalaliDate
import com.hamyarehealth.ir.platform.core.common.toPersianDigits
import com.hamyarehealth.ir.ui.hub.DailyHealthSnapshot
import com.hamyarehealth.ir.ui.hub.HubCoverTile
import com.hamyarehealth.ir.ui.hub.hubTo
import com.hamyarehealth.ir.ui.hub.layerTo
import com.hamyarehealth.ir.ui.navigation.Screen

/**
 * زبانِ تصویریِ اختصاصی «همیار سلامتی من»:
 *
 * - سربرگِ گرادیانیِ نعنایی→فیروزه‌ای (هم‌رنگِ آیکونِ اپ) به‌جای سربرگِ سادهٔ قبلی.
 * - کاشی‌های ردیف‌بندی‌شدهٔ «اقدام سریع» برای تنفس/آرامش/ذهن‌آگاهی.
 * - کارتِ «حال امروز» با نوارِ پیشرفتِ آب و روتین.
 *
 * این لایه روی همان موتور (Compose + تم + کاتالوگِ باکت) ساخته شده و فقط ظاهرِ
 * همین اپ را می‌سازد؛ هیچ بخشی از منطقِ تمرین‌ها/محتوا تغییر نمی‌کند.
 */

/** رنگ‌های امضای اپ — مستقل از تمِ انتخابیِ کاربر تا هویت اپ همیشه دیده شود. */
object HealthPalette {
    val Mint = Color(0xFF3FBFA6)
    val MintDeep = Color(0xFF1F8F7C)
    val Lime = Color(0xFF9BD96F)
    val Coral = Color(0xFFFF7A7A)
    val Sand = Color(0xFFFFE9C7)
    val Deep = Color(0xFF0E3B36)
}

/** سربرگِ گرادیانی — با فلشِ بازگشتِ اختیاری و برچسبِ اختیاریِ کنارِ عنوان. */
@Composable
fun HealthHero(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null,
    badge: String = "",
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(26.dp)
    Box(
        modifier
            .fillMaxWidth()
            .shadow(8.dp, shape)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(HealthPalette.Mint, HealthPalette.MintDeep),
                ),
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت", tint = Color.White)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.88f))
            }
            if (badge.isNotBlank()) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text(badge, style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        }
    }
}

/** عنوانِ بخش با نوارِ رنگیِ کنارش — ریتمِ بصریِ یکدست در همه‌ی صفحه‌های اپ. */
@Composable
fun HealthSectionTitle(text: String, emoji: String = "", modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(width = 4.dp, height = 18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(HealthPalette.Mint),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            if (emoji.isBlank()) text else "$emoji  $text",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

/** دکمهٔ «اقدام سریع» — قرصِ نرم با ایموجی و برچسب. */
@Composable
fun HealthQuickAction(
    emoji: String,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier
            .shadow(4.dp, shape)
            .clip(shape)
            .background(tint)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .35f), shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(emoji, style = MaterialTheme.typography.headlineSmall)
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
        )
    }
}

/** کارتِ «حال امروز» — آب، ورزش، روتین و میان‌برهای همان روز. */
@Composable
fun TodayHealthCard(
    snapshot: DailyHealthSnapshot,
    onWaterDelta: (Int) -> Unit,
    onOpen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val waterGoal = snapshot.waterGoal.coerceAtLeast(1)
    val water = (snapshot.waterConsumed.toFloat() / waterGoal).coerceIn(0f, 1f)
    val routine = if (snapshot.routineTotal == 0) 0f else snapshot.routineDone.toFloat() / snapshot.routineTotal
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier
            .fillMaxWidth()
            .shadow(6.dp, shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = .35f), shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("حال امروز", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    if (snapshot.lightDay) "امروز روزِ سبک است — مهربان‌تر با خودت" else "هر قدمِ کوچک امروز ثبت می‌شود",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(if (snapshot.lightDay) HealthPalette.Sand else HealthPalette.Lime.copy(alpha = .35f))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
            ) {
                Text(
                    if (snapshot.lightDay) "روز سبک" else "روز معمولی",
                    style = MaterialTheme.typography.labelSmall,
                    color = HealthPalette.Deep,
                )
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HealthMetric("💧", "${toPersianDigits(snapshot.waterConsumed.toString())}/${toPersianDigits(snapshot.waterGoal.toString())}", "لیوان آب", Modifier.weight(1f))
            HealthMetric("🏃", toPersianDigits(snapshot.sportsMinutes.toString()), "دقیقه ورزش", Modifier.weight(1f))
            HealthMetric("✅", "${toPersianDigits(snapshot.routineDone.toString())}/${toPersianDigits(snapshot.routineTotal.toString())}", "روتین", Modifier.weight(1f))
        }

        Text("آب 💧", style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(progress = { water }, modifier = Modifier.fillMaxWidth())
        Text("روتین 📋", style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(progress = { routine }, modifier = Modifier.fillMaxWidth())

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickPill("+ یک لیوان", Modifier.weight(1f)) { onWaterDelta(1) }
            QuickPill("− یک لیوان", Modifier.weight(1f), HealthPalette.Sand) { onWaterDelta(-1) }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextButton(onClick = { onOpen("water") }, modifier = Modifier.weight(1f)) { Text("آب") }
            TextButton(onClick = { onOpen("routine") }, modifier = Modifier.weight(1f)) { Text("روتین") }
            TextButton(onClick = { onOpen("breath") }, modifier = Modifier.weight(1f)) { Text("تنفس") }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextButton(onClick = { onOpen("yoga") }, modifier = Modifier.weight(1f)) { Text("یوگا") }
            TextButton(onClick = { onOpen("sleep") }, modifier = Modifier.weight(1f)) { Text("خواب") }
            TextButton(onClick = { onOpen("progress") }, modifier = Modifier.weight(1f)) { Text("گزارش") }
        }

        if (snapshot.activities.isNotEmpty()) {
            HealthSectionTitle("آخرین فعالیت‌ها", "🕒")
            snapshot.activities.take(3).forEach { activity ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(activity.title, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text(
                        JalaliDate.clockFa(activity.atMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickPill(
    label: String,
    modifier: Modifier = Modifier,
    tint: Color = HealthPalette.Mint,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier
            .clip(shape)
            .background(tint)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = Color.White)
    }
}

@Composable
private fun HealthMetric(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .55f))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text("$icon $value", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * فهرستِ قطعیِ کارت‌های سلامتی — همان کارت‌های همیار سلامتی من (سالم و کامل)،
 * بدونِ بخش‌های مدرسه/آزمون. جلدِ هر کارت از assets/practice-covers می‌آید.
 */
fun healthTiles(nav: NavController, includePeriod: Boolean): List<HubCoverTile> = buildList {
    add(
        HubCoverTile(
            "hl-progress", "پیشرفت سلامتی", "آب، ورزش، روتین و فعالیت‌های روزانه",
            { nav.hubTo(Screen.HealthProgress.route) },
        ),
    )
    if (includePeriod) {
        add(
            HubCoverTile(
                "hl-period", "چرخه ماهانه", "تقویم، علائم، تنفس درد و تمرین ملایم",
                { nav.layerTo(Screen.PracticeGroup.of("hl-cycle")) },
            ),
        )
    }
    add(
        HubCoverTile(
            "hl-yoga", "یوگا", "حرکات تعاملی با ثبت خودکار فعالیت",
            { nav.hubTo(Screen.ContentCategory.of("yoga")) },
        ),
    )
    add(
        HubCoverTile(
            "hl-exercise", "ورزش عمومی", "تمرین‌های مرحله‌به‌مرحله با ثبت خودکار",
            { nav.hubTo(Screen.ContentCategory.of("sport")) },
        ),
    )
    add(
        HubCoverTile(
            "hl-food", "آب و تغذیه", "ثبت سریع آب و هدف روزانه",
            { nav.layerTo(Screen.PracticeGroup.of("hl-nutrition")) },
        ),
    )
    add(
        HubCoverTile(
            "hl-sleep", "خواب", "ثبت زمان خواب و بیداری",
            { nav.layerTo(Screen.PracticeGroup.of("hl-sleep")) },
        ),
    )
    add(
        HubCoverTile(
            "hl-meds", "یادآور دارو و مراقبت", "هشدارهای زمان‌دار",
            { nav.hubTo(Screen.Meds.route) },
        ),
    )
    add(
        HubCoverTile(
            "hl-routine", "روتین امروز", "افزودن، ویرایش و تیک‌زدن",
            { nav.hubTo(Screen.Routine.route) },
        ),
    )
    add(
        HubCoverTile(
            "cy-cal", "تقویم و علائم چرخه", "نمودار، ثبت روزها و پیش‌بینی دورهٔ بعد",
            { nav.layerTo(Screen.CycleCal.route) },
        ),
    )
    add(
        HubCoverTile(
            "hl-sleep-breath", "تنفس پیش از خواب", "۴ دم، ۶ بازدم — آرام‌سازی شبانه",
            { nav.hubTo(Screen.SleepBreath.route) },
        ),
    )
}
