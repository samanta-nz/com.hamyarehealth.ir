package com.hamyarehealth.ir.ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hamyarehealth.ir.platform.core.designsystem.AppTopBar

private data class GuideSection(
    val id: String,
    val title: String,
    val icon: String,
    val summary: String,
    val steps: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
)

private val guideSections = listOf(
    GuideSection(
        id = "calendar",
        title = "تقویم و مناسبت‌ها",
        icon = "📅",
        summary = "تقویم شمسی، قمری و میلادی در «بیشتر» باز می‌شود و مناسبت‌های هر روز، زیر تاریخ داشبورد نوشته می‌شود.",
        steps = listOf(
            "روی تاریخ داشبورد (بالای صفحهٔ خانه) بزن، یا از «بیشتر» گزینهٔ «تقویم شمسی، قمری و میلادی» را باز کن.",
            "با «ماه قبل» و «ماه بعد» بگرد؛ مناسبت‌های همان ماه پایین صفحه فهرست می‌شوند.",
            "در «بیشتر» → «تنظیم تقویم»، تیک هر نوع مناسبت را که می‌خواهی نشان داده شود، بردار یا بگذار.",
        ),
        tips = listOf(
            "جمعه‌ها تعطیل هفتگی است و مناسبت‌های رسمی با رنگ متفاوت دیده می‌شوند.",
            "ساعت آنالوگ و تاریخ داشبورد زنده‌اند؛ ساعت با منطقهٔ زمانی تهران حساب می‌شود.",
        ),
    ),
    GuideSection(
        id = "alarms",
        title = "یادآورها و اعلان‌ها",
        icon = "⏰",
        summary = "یادآورهای روزانه (آب، خواب، تنفس و مرور) و یادآورهای دارو در یک صفحه جمع‌اند؛ ساعات سکوت هم همان‌جاست.",
        steps = listOf(
            "صفحهٔ «بیشتر» → «یادآورها» را باز کن؛ همهٔ زمان‌بندی‌ها و یادآورهای دارو آن‌جا فهرست شده‌اند.",
            "هر یادآور را با کلید کنارش روشن یا خاموش کن؛ تغییر همان لحظه ذخیره می‌شود.",
            "اگر اعلانی نمی‌رسد، «فعال‌سازی دوباره» را بزن یا از «تنظیمات اعلان اندروید» مجوز اعلان را بررسی کن.",
            "در اعلان دعوت به خواب سه دکمه هست: «بریم قصه»، «بریم موسیقی» و «بریم تنفس»؛ اعلان با انتخاب هرکدام بسته می‌شود.",
        ),
        tips = listOf(
            "در ساعات سکوت، یادآورهای عمومی صدا و اعلان ندارند.",
            "برای ساعات سکوت در اندرویدهای جدید، «دسترسی سیستم» را از همان صفحهٔ یادآورها فعال کن.",
        ),
    ),
    GuideSection(
        id = "diary",
        title = "دفتر خاطرات و دفترچه‌ها",
        icon = "📔",
        summary = "هر صفحه را روی همان ورقی که بعداً ورق می‌زنی می‌نویسی؛ نوشته دقیقاً روی خط‌های ورق می‌نشیند و جای عکس همان‌جا که می‌گذاری در کتاب هم دیده می‌شود.",
        steps = listOf(
            "عنوان را از فهرست پیشنهادی انتخاب کن یا خودت بنویس؛ فیلد عنوان همیشه قابل ویرایش است.",
            "روی ورق بنویس. سطر اول عنوان است، یک سطر خالی می‌ماند و از سطر سوم متن شروع می‌شود. وقتی صفحه پر شود، صفحهٔ بعد خودکار باز می‌شود.",
            "برای عکس، «افزودن عکس» را بزن. جای عکس بالای صفحه یا پایین صفحه است و چند سطر برایش کنار گذاشته می‌شود؛ متن هیچ‌وقت زیر عکس نمی‌رود.",
            "با یک انگشت عکس را جابه‌جا کن و با دو انگشت بزرگ/کوچک یا بچرخانش. می‌توانی برایش پانویس هم بنویسی.",
            "تراز متن (راست، وسط، چپ) را از نوار تراز انتخاب کن و «ذخیره» را بزن.",
        ),
        tips = listOf(
            "پیش‌نویس خودکار: هر چه تایپ می‌کنی لحظه‌به‌لحظه روی گوشی نگه داشته می‌شود. حتی اگر از صفحه یا برنامه خارج شوی، دفعهٔ بعد همان‌جا که بودی ادامه می‌دهی.",
            "برای برگشتن از یک صفحهٔ خالی که اتفاقی باز شده، در ابتدای همان صفحه پس‌بر (Backspace) بزن تا به صفحهٔ قبل برگردد.",
            "دفترچه‌های یادداشت هم همین ورق، همین پیش‌نویس و همین ذخیره را دارند.",
        ),
    ),
    GuideSection(
        id = "flip",
        title = "تورق کتاب",
        icon = "📖",
        summary = "خاطرات و شعرها در حالت کتاب نمایش داده می‌شوند. متن در حالت تورق پررنگ و درشت‌تر است تا راحت خوانده شود.",
        steps = listOf(
            "یک لمس روی صفحه: ورق بعدی.",
            "دو لمس پیاپی: ورق قبلی.",
            "کشیدن انگشت روی صفحه: ورق‌زدن دستی؛ ورق مثل کاغذ واقعی دور شیرازه جمع می‌شود.",
            "«ویرایش» همان صفحه‌ای را که روی آن هستی در ویرایشگر باز می‌کند.",
        ),
        tips = listOf(
            "اگر متن یک صفحه با فونت درشت‌تر جا نشود، خودکار روی ورق بعدی ادامه پیدا می‌کند و چیزی از نوشته‌ات کم نمی‌شود.",
        ),
    ),
    GuideSection(
        id = "poetry",
        title = "دفتر شعر",
        icon = "✒️",
        summary = "دفتر شعر جلد چرمی و ورق مخصوص خودش را دارد. شعرهای دو مصراعی (غزل، قصیده، دوبیتی، رباعی، مثنوی و …) با دو مصراع کنار هم روی هر سطر و بقیهٔ قالب‌ها آزاد نوشته می‌شوند.",
        steps = listOf(
            "نوع شعر را انتخاب کن: غزل، قصیده، دوبیتی، رباعی یا مثنوی؛ برای بقیه «سایر» را بزن.",
            "«سایر» فهرست قالب‌های دیگر را باز می‌کند: قطعه، شعر نو، نیمایی، سپید، چهارپاره، مستزاد، ترکیب‌بند، ترجیع‌بند، مسمط، مخمس، تک‌بیت، ترانه، نثر شاعرانه، هایکو و شعر کودک.",
            "عنوان بعد از انتخاب نوع، خودکار پر می‌شود و هر وقت بخواهی می‌توانی آن را تغییر بدهی.",
            "در شعرهای دو مصراعی، مصراع اول و دوم هر بیت کنار هم نوشته می‌شوند؛ با دکمهٔ بعدی صفحه‌کلید به مصراع بعد می‌روی.",
            "ذخیره کن و از فهرست، شعر را باز کن تا با تورق ببینی.",
        ),
        tips = listOf(
            "اگر مصراعی در حالت تورق بلندتر از یک سطر باشد، دو سطر می‌گیرد و بریده نمی‌شود.",
            "پیش‌نویس شعر هم خودکار نگه داشته می‌شود.",
        ),
    ),
    GuideSection(
        id = "album",
        title = "آلبوم شخصی",
        icon = "🖼️",
        summary = "آلبوم شخصی برای عکس، ویدیو و صوت است. فایل‌ها فقط داخل پوشهٔ خصوصی برنامه نگه‌داری می‌شوند.",
        steps = listOf(
            "«افزودن» را بزن و فایل‌های مورد نظر را انتخاب کن.",
            "روی عکس بزن تا بزرگ شود؛ با دو انگشت بزرگ‌نمایی کن یا بچرخان، و برای برش از دکمه‌های پایین صفحه استفاده کن.",
            "روی ویدیو یا صوت بزن تا پلیر اختصاصی باز شود: پخش/مکث، ۱۰ ثانیه جلو و عقب، قبلی/بعدی، سرعت، تصادفی و تکرار.",
            "برای فرستادن یا ذخیره بیرون از برنامه، «خروجی» را بزن.",
        ),
        tips = listOf(
            "اگر وسط یک فایل خارج شوی، دفعهٔ بعد از همان‌جا ادامه می‌دهد. اگر فایل تا انتها پخش شده باشد، دفعهٔ بعد از اول شروع می‌شود.",
            "اگر خطایی نمایش داده شد، «تلاش دوباره» را بزن.",
        ),
    ),
    GuideSection(
        id = "sleep",
        title = "بشنو و بخواب",
        icon = "🌙",
        summary = "صداهای آرام‌بخش و موسیقی برای خوابیدن. کارت بالای صفحه همیشه جمع‌شده است و با لمس باز می‌شود.",
        steps = listOf(
            "کارت بالایی را لمس کن تا فهرست صداها پایین باز شود.",
            "یک یا دو صدا را انتخاب کن و بلندی هر کدام را جدا تنظیم کن.",
            "اگر دو ثانیه لمس نکنی، کارت خودکار جمع می‌شود و صدا ادامه دارد.",
            "تایمر خواب برنامه را تنظیم کن تا خودش پخش را متوقف کند.",
        ),
        tips = listOf(
            "قصهٔ شب و تمرین تنفس هم از همین بخش در دسترس‌اند و از اعلان دعوت به خواب هم باز می‌شوند.",
        ),
    ),
    GuideSection(
        id = "appearance",
        title = "ظاهر، تم و فونت",
        icon = "🎨",
        summary = "رنگ تم، حالت روشن/تیره و اندازهٔ فونت از بخش ظاهر تنظیم می‌شوند.",
        steps = listOf(
            "یک تم را انتخاب کن و تأیید بزن.",
            "برای برگشت به رنگ اصلی برنامه، «بازگشت به تم پیش‌فرض همیار» را بزن؛ این گزینه در بالای فهرست تم‌ها هست.",
            "اندازهٔ متن و فونت را تغییر بده؛ «بازگردانی فونت پیش‌فرض» تنظیمات فونت را به حالت استاندارد برمی‌گرداند.",
        ),
        tips = listOf(
            "حالت روشن/تیره در صفحه‌های آموزشی از همین تنظیم برنامه تبعیت می‌کند، نه از تم گوشی.",
        ),
    ),
    GuideSection(
        id = "safe",
        title = "فضای امن",
        icon = "🔒",
        summary = "دفتر خاطرات، دفتر شعر، دفترچه‌ها و آلبوم شخصی داخل فضای امن‌اند و با قفل برنامه محافظت می‌شوند.",
        steps = listOf(
            "قفل برنامه را در تنظیمات فعال کن.",
            "با باز کردن فضای امن، محتوا نمایش داده می‌شود و با خروج یا خاموش شدن صفحه دوباره قفل می‌شود.",
        ),
        tips = listOf(
            "متن دفترها و پیش‌نویس‌ها رمزگذاری‌شده ذخیره می‌شوند.",
        ),
    ),
    GuideSection(
        id = "settings",
        title = "تنظیمات و به‌روزرسانی",
        icon = "⚙️",
        summary = "ظاهر، قفل، یادآورها، همگام‌سازی و همین راهنما از تنظیمات باز می‌شوند.",
        steps = listOf(
            "در تنظیمات، ساعات سکوت و یادآورها را تنظیم کن.",
            "برای نسخهٔ جدید، صفحهٔ به‌روزرسانی را دنبال کن؛ هنگام وجود نسخهٔ اجباری، نصب باید کامل شود.",
        ),
    ),
    GuideSection(
        id = "help",
        title = "اگر چیزی کار نکرد",
        icon = "🛠️",
        summary = "چند راه سریع برای مشکلات رایج:",
        steps = listOf(
            "اعلان یا آلارم نمی‌آید: مجوز اعلان، حالت «مزاحم نشوید» گوشی و ساعات سکوت برنامه را ببین.",
            "پلیر پخش نمی‌کند: «تلاش دوباره» را بزن؛ اگر ادامه داشت، فایل را دوباره اضافه کن.",
            "عکس یا فایل باز نمی‌شود: فایل را از حافظهٔ گوشی دوباره انتخاب کن.",
            "متن دفتری از دست رفت: همان صفحه را دوباره باز کن؛ پیش‌نویس خودکار همان‌جا برگردانده می‌شود.",
        ),
    ),
)

@Composable
fun UserGuideScreen(
    onBack: () -> Unit,
    initialSection: String = "",
) {
    val ordered = if (initialSection.isBlank()) {
        guideSections
    } else {
        buildList {
            guideSections.firstOrNull { it.id == initialSection }?.let { add(it) }
            addAll(guideSections.filter { it.id != initialSection })
        }
    }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("راهنمای کاربری برنامه", onBack)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                "هر بخش را باز کن تا مراحل و نکته‌هایش را ببینی.",
                style = MaterialTheme.typography.bodyMedium,
            )
            ordered.forEachIndexed { index, section ->
                GuideCard(section, startExpanded = index == 0 && initialSection.isNotBlank())
            }
        }
    }
}

@Composable
private fun GuideCard(section: GuideSection, startExpanded: Boolean) {
    var expanded by remember(section.id) { mutableStateOf(startExpanded) }
    Card(Modifier.fillMaxWidth().animateContentSize()) {
        Column(
            Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(section.icon, style = MaterialTheme.typography.titleLarge)
                Text(
                    section.title,
                    Modifier.weight(1f).padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "بستن" else "باز کردن",
                )
            }
            Text(section.summary, style = MaterialTheme.typography.bodyMedium)
            if (expanded) {
                if (section.steps.isNotEmpty()) {
                    Text("مراحل", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    section.steps.forEachIndexed { i, step ->
                        Text("${i + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (section.tips.isNotEmpty()) {
                    Text("نکته‌ها", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    section.tips.forEach { tip ->
                        Text(
                            "💡 $tip",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
