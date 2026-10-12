package com.hamyarehealth.ir.ui.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamyarehealth.ir.BuildConfig
import com.hamyarehealth.ir.ui.health.HealthHero
import com.hamyarehealth.ir.ui.health.HealthPalette
import com.hamyarehealth.ir.ui.health.HealthSectionTitle
import com.hamyarehealth.ir.ui.home.CalendarPrefs
import com.hamyarehealth.ir.ui.home.OccasionKind
import com.hamyarehealth.ir.ui.hub.HubBody
import com.hamyarehealth.ir.ui.hub.HubCard
import com.hamyarehealth.ir.ui.hub.hubTo
import com.hamyarehealth.ir.ui.navigation.Screen
import com.hamyarehealth.ir.ui.update.UpdateCheckCard

/**
 * تبِ «بیشتر» — هر چیزِ شخصی، تقویمی و پشتیبانِ اپ:
 * فضای امن و شماره‌های کمک، تقویم و مناسبت‌ها، پروفایل، ظاهر، تنظیمات، راهنما و بروزرسانی.
 *
 * حذف‌شده‌ها (چون در این اپ معنا نداشتند): «زمان سکوت» اپ قبلی (کلیدِ بی‌صداکردنِ
 * پلیرِ درس‌ها) و کارت‌های مدرسه/آزمون.
 * اضافه‌شده‌ها: ورود به تقویمِ شمسی/قمری/میلادی و تنظیمِ مناسبت‌های زیرِ تاریخِ داشبورد.
 */
@Composable
fun MoreScreen(nav: NavController) {
    HubBody {
        HealthHero(
            title = "بیشتر",
            subtitle = "فضای شخصی، تقویم، تنظیمات و پشتیبانی «همیار سلامتی من»",
        )

        HealthSectionTitle("آرامش و امنیت", "🛟")
        HubCard("🛟", "فضای امن من", "نوشتن، آلبوم، شماره‌های کمک", slotId = "hub.more.item.safespace") { nav.hubTo(Screen.SafeSpace.route) }
        HubCard("🚨", "شماره‌های کمک", "همیشه در دسترس", slotId = "hub.more.item.helplines") { nav.hubTo(Screen.Helplines.route) }

        HealthSectionTitle("تقویم و مناسبت‌ها", "📅")
        HubCard(
            "📅",
            "تقویم شمسی، قمری و میلادی",
            "ماه‌ها، مناسبت‌های ماه و تعطیلات رسمی",
            slotId = "hub.more.item.calendar",
        ) { nav.hubTo(Screen.ClassPlanCalendar.route) }
        CalendarSettingsCard()

        HealthSectionTitle("حساب و ظاهر", "👤")
        HubCard("👤", "پروفایل من", "مشخصات، عکس و وضعیت اشتراک", slotId = "hub.more.item.user-profile") { nav.hubTo(Screen.UserProfile.route) }
        HubCard("🎨", "ظاهر و فونت", "تم رنگی و فونتِ دلخواه", slotId = "hub.more.item.appearance") { nav.hubTo(Screen.Appearance.route) }
        HubCard("⚙️", "تنظیمات", "تم برنامه، قفل، یادآورها و تنظیمات سرور", slotId = "hub.more.item.settings") { nav.hubTo(Screen.Settings.route) }
        HubCard("⏰", "یادآورها", "آب، خواب، تنفس و مرور روزانه", slotId = "hub.more.item.reminders") { nav.hubTo(Screen.Reminders.route) }

        HealthSectionTitle("راهنما و پشتیبانی", "💬")
        HubCard("📘", "راهنمای استفاده", "قدم‌به‌قدم با همین اپ", slotId = "hub.more.item.guide") { nav.hubTo(Screen.UserGuide.of()) }
        UpdateCheckCard()
        HubCard("ℹ️", "درباره ما", "هدف و حریم خصوصی همیار سلامتی من", slotId = "hub.more.item.about") { nav.hubTo(Screen.About.route) }
        HubCard("💬", "تماس با ما", "پیش‌نویس، ارسال، پیگیری و پاسخ پشتیبانی", slotId = "hub.more.item.contact") { nav.hubTo(Screen.Contact.route) }

        Text(
            "همیار سلامتی من — نسخه‌ی " + BuildConfig.VERSION_NAME,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * تنظیمِ مناسبت‌های زیرِ تاریخ داشبورد — همان کارتِ «تنظیم تقویم» اپِ قبلی
 * (انتخابِ این‌که کدام نوع مناسبت در خطِ «مناسبت‌های امروز» دیده شود).
 */
@Composable
private fun CalendarSettingsCard() {
    val ctx = LocalContext.current
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("📅 تنظیم تقویم", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                "مناسبت‌هایی که زیر تاریخ داشبورد نشان داده می‌شوند.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OccasionKind.entries.forEach { kind ->
                var on by remember(kind) { mutableStateOf(CalendarPrefs.show(ctx, kind)) }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = on,
                        onCheckedChange = {
                            on = it
                            CalendarPrefs.setShow(ctx, kind, it)
                        },
                    )
                    Text(
                        kind.fa,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (on) HealthPalette.MintDeep else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
