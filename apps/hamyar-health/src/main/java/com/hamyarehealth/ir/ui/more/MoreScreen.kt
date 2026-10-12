package com.hamyarehealth.ir.ui.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamyarehealth.ir.BuildConfig
import com.hamyarehealth.ir.platform.core.common.LocalStore
import com.hamyarehealth.ir.ui.health.HealthHero
import com.hamyarehealth.ir.ui.health.HealthSectionTitle
import com.hamyarehealth.ir.ui.hub.HubBody
import com.hamyarehealth.ir.ui.hub.HubCard
import com.hamyarehealth.ir.ui.hub.hubTo
import com.hamyarehealth.ir.ui.navigation.Screen
import com.hamyarehealth.ir.ui.update.UpdateCheckCard

/**
 * تبِ «بیشتر» — هر چیزِ شخصی و پشتیبانِ اپ:
 * فضای امن و شماره‌های کمک، پروفایل، ظاهر، تنظیمات، راهنما و بروزرسانی.
 * (کارت‌های مدرسه/آزمون/ابزار در «همیار سلامتی من» وجود ندارند.)
 */
@Composable
fun MoreScreen(nav: NavController) {
    HubBody {
        HealthHero(
            title = "بیشتر",
            subtitle = "فضای شخصی، تنظیمات و پشتیبانی «همیار سلامتی من»",
        )

        HealthSectionTitle("آرامش و امنیت", "🛟")
        HubCard("🛟", "فضای امن من", "نوشتن، آلبوم، شماره‌های کمک", slotId = "hub.more.item.safespace") { nav.hubTo(Screen.SafeSpace.route) }
        HubCard("🚨", "شماره‌های کمک", "همیشه در دسترس", slotId = "hub.more.item.helplines") { nav.hubTo(Screen.Helplines.route) }

        QuietModeCard()

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
 * «زمان سکوت» — کلیدِ سراسریِ بی‌صدا شدنِ پخش‌کننده‌ها (صوت‌های آرامش و درس‌ها)
 * وقتی می‌خواهی تمرکز یا استراحتِ بی‌صدا داشته باشی.
 */
@Composable
private fun QuietModeCard() {
    val ctx = LocalContext.current
    val store = remember { LocalStore(ctx, "hamyar_teach") }
    var on by remember { mutableStateOf(store.getString("quiet_mode", "0") == "1") }
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("🔇 زمان سکوت", style = MaterialTheme.typography.titleMedium)
                Text(
                    "وقتی روشن است، هیچ صدایی از پخش‌کننده‌ها (آرامش و درس) فعال نمی‌شود.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = on,
                onCheckedChange = {
                    on = it
                    store.putString("quiet_mode", if (it) "1" else "0")
                },
            )
        }
    }
}
