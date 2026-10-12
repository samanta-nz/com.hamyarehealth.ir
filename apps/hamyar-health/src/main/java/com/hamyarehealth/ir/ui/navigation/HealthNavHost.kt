package com.hamyarehealth.ir.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hamyarehealth.ir.LocalAppContainer
import com.hamyarehealth.ir.ui.appearance.AppearanceScreen
import com.hamyarehealth.ir.ui.calmdown.BackgroundMusicScreen
import com.hamyarehealth.ir.ui.calmdown.BreathingScreen
import com.hamyarehealth.ir.ui.calmdown.CalmHubScreen
import com.hamyarehealth.ir.ui.calmdown.CalmMenuScreen
import com.hamyarehealth.ir.ui.calmdown.GratitudeJournalScreen
import com.hamyarehealth.ir.ui.calmdown.JournalScreen
import com.hamyarehealth.ir.ui.content.ContentCategoryScreen
import com.hamyarehealth.ir.ui.content.ContentHubScreen
import com.hamyarehealth.ir.ui.content.ContentHtmlScreen
import com.hamyarehealth.ir.ui.cycle.CycleCalScreen
import com.hamyarehealth.ir.ui.cycle.CycleLogScreen
import com.hamyarehealth.ir.ui.cycle.CycleTodayScreen
import com.hamyarehealth.ir.ui.cycle.MindfulnessScreen
import com.hamyarehealth.ir.ui.cycle.MoodCheckInScreen
import com.hamyarehealth.ir.ui.cycle.PeriodTrainingScreen
import com.hamyarehealth.ir.ui.health.HealthHomeScreen
import com.hamyarehealth.ir.ui.hub.AwarenessHubScreen
import com.hamyarehealth.ir.ui.hub.HealthHubScreen
import com.hamyarehealth.ir.ui.hub.MedsScreen
import com.hamyarehealth.ir.ui.hub.SleepLogScreen
import com.hamyarehealth.ir.ui.hub.layerTo
import com.hamyarehealth.ir.ui.more.ContactScreen
import com.hamyarehealth.ir.ui.more.MoreScreen
import com.hamyarehealth.ir.ui.more.AboutScreen
import com.hamyarehealth.ir.ui.routine.RoutineScreen
import com.hamyarehealth.ir.ui.safespace.DiaryScreen
import com.hamyarehealth.ir.ui.safespace.HelplinesScreen
import com.hamyarehealth.ir.ui.safespace.NotebooksScreen
import com.hamyarehealth.ir.ui.safespace.PoetryBookScreen
import com.hamyarehealth.ir.ui.safespace.SafeContentGuard
import com.hamyarehealth.ir.ui.safespace.SafeFreeWritingScreen
import com.hamyarehealth.ir.ui.safespace.SafeSpaceScreen
import com.hamyarehealth.ir.ui.safespace.WritingPromptScreen
import com.hamyarehealth.ir.ui.study.ClassPlanScreen
import com.hamyarehealth.ir.ui.study.HealthProgressScreen
import com.hamyarehealth.ir.ui.study.SecureMediaGalleryScreen
import com.hamyarehealth.ir.ui.study.SleepBreathScreen
import com.hamyarehealth.ir.ui.study.SleepNightScreen
import com.hamyarehealth.ir.ui.settings.AppLockScreen
import com.hamyarehealth.ir.ui.settings.PrivacySettingsScreen
import com.hamyarehealth.ir.ui.settings.RemindersScreen
import com.hamyarehealth.ir.ui.settings.SettingsScreen
import com.hamyarehealth.ir.ui.settings.SyncScreen
import com.hamyarehealth.ir.ui.settings.UserGuideScreen
import com.hamyarehealth.ir.ui.water.WaterScreen
import com.hamyarehealth.ir.ui.wellness.BetweenLessonsHubScreen
import com.hamyarehealth.ir.ui.wellness.PracticeGroupScreen
import com.hamyarehealth.ir.ui.wellness.PracticeItemScreen
import com.hamyarehealth.ir.ui.wellness.SketchGalleryScreen
import com.hamyarehealth.ir.ui.wellness.WellnessScreen

/**
 * گرافِ مسیرهای «همیار سلامتی من».
 *
 * پنج تبِ پایین: داشبورد · سلامتی · کسب آرامش · ذهن‌آگاهی · بیشتر.
 * هر کارت/زیرمنو عیناً همان مقصدِ اپِ همیار سلامتی من را باز می‌کند (همان گروه‌های
 * `WellnessMenu`، همان کاتالوگِ باکت و همان لینک‌های ParsPack) — فقط مسیرهای
 * مدرسه/آزمون/ابزار در این اپ وجود ندارند.
 */
@Composable
fun HealthNavHost() {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val navContext = LocalContext.current

    // فضای امن: بستن خودکار وقتی از مسیرهای محافظت‌شده بیرون می‌آییم.
    LaunchedEffect(route) {
        com.hamyarehealth.ir.ui.safespace.SafeSpaceSession.onRouteChanged(navContext, route)
    }

    // لمس اعلانِ «آرامش قبل خواب» → همان گروه/صفحهٔ درست در این اپ.
    LaunchedEffect(com.hamyarehealth.ir.ui.study.SleepLaunch.pendingDestination) {
        val destination = com.hamyarehealth.ir.ui.study.SleepLaunch.pendingDestination ?: return@LaunchedEffect
        com.hamyarehealth.ir.ui.study.SleepLaunch.pendingDestination = null
        val target = when (destination) {
            com.hamyarehealth.ir.platform.core.notifications.ReminderReceiver.SLEEP_STORY ->
                Screen.PracticeGroup.of("cl-story")
            com.hamyarehealth.ir.platform.core.notifications.ReminderReceiver.SLEEP_BREATH ->
                Screen.SleepBreath.route
            else -> Screen.SleepNight.route
        }
        nav.navigate(target) {
            popUpTo(Screen.Home.route)
            launchSingleTop = true
        }
    }

    Scaffold(bottomBar = {
        NavigationBar {
            Tabs.forEach { tab ->
                NavigationBarItem(
                    selected = route == tab.route,
                    onClick = {
                        // لمس عنوانِ منوی پایین همیشه به صفحهٔ اصلی همان عنوان می‌رود.
                        if (route == tab.route) return@NavigationBarItem
                        val landed = nav.popBackStack(tab.route, inclusive = false)
                        if (!landed) {
                            nav.navigate(tab.route) {
                                popUpTo(Screen.Home.route) { inclusive = false; saveState = false }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    },
                    icon = { Icon(tab.icon, tab.label) },
                    label = {
                        Text(
                            tab.label,
                            fontFamily = com.hamyarehealth.ir.ui.AppTypography.navBar.family,
                            fontWeight = com.hamyarehealth.ir.ui.AppTypography.navBar.weight,
                            fontSize = com.hamyarehealth.ir.ui.AppTypography.navBar.size,
                        )
                    },
                )
            }
        }
    }) { pad ->
        NavHost(nav, startDestination = Screen.Home.route, modifier = Modifier.padding(pad)) {
            composable(Screen.Home.route) { HealthHomeScreen(nav) }
            composable(Screen.HealthHub.route) { HealthHubScreen(nav) { nav.popBackStack() } }
            composable(Screen.CalmHub.route) { CalmHubScreen(nav) { nav.popBackStack() } }
            composable(Screen.AwarenessHub.route) { AwarenessHubScreen(nav) { nav.popBackStack() } }
            composable(Screen.More.route) { MoreScreen(nav) }

            // --- صفحه‌های شخصی/عمومی ---
            composable(Screen.About.route) { AboutScreen { nav.popBackStack() } }
            composable(Screen.Contact.route) { ContactScreen { nav.popBackStack() } }
            composable(Screen.Settings.route) { SettingsScreen(nav) }
            composable(Screen.Privacy.route) { PrivacySettingsScreen { nav.popBackStack() } }
            composable(Screen.Lock.route) { AppLockScreen { nav.popBackStack() } }
            composable(Screen.Reminders.route) { RemindersScreen { nav.popBackStack() } }
            composable(Screen.Sync.route) { SyncScreen { nav.popBackStack() } }
            composable(Screen.Appearance.route) {
                AppearanceScreen(
                    onBack = { nav.popBackStack() },
                    onHelp = { nav.navigate(Screen.UserGuide.of("appearance")) },
                )
            }
            composable(
                Screen.UserGuide.route,
                listOf(navArgument("section") { type = NavType.StringType; defaultValue = "" }),
            ) { entry ->
                UserGuideScreen(
                    onBack = { nav.popBackStack() },
                    initialSection = entry.arguments?.getString("section").orEmpty(),
                )
            }
            composable(Screen.UserProfile.route) { UserProfileRoute(nav) }
            composable(Screen.Subscription.route) { com.hamyarehealth.ir.ui.home.SubscriptionScreen { nav.popBackStack() } }

            // --- سلامتی ---
            composable(Screen.HealthProgress.route) { HealthProgressScreen(onBack = { nav.popBackStack() }) }
            // تقویمِ شمسی/قمری/میلادی — همان صفحهٔ «تقویم» اپ قبلی، بدون تب‌های مدرسه.
            composable(Screen.ClassPlanCalendar.route) {
                ClassPlanScreen(
                    onBack = { nav.popBackStack() },
                    calendarOnly = true,
                )
            }
            composable(Screen.Water.route) { WaterScreen() }
            composable(Screen.Routine.route) { RoutineScreen { nav.popBackStack() } }
            composable(Screen.Meds.route) { MedsScreen { nav.popBackStack() } }
            composable(Screen.SleepLog.route) { SleepLogScreen { nav.popBackStack() } }
            composable(Screen.SleepNight.route) { SleepNightScreen { nav.popBackStack() } }
            composable(Screen.SleepBreath.route) { SleepBreathScreen { nav.popBackStack() } }
            composable(Screen.CycleCal.route) { CycleCalScreen { nav.popBackStack() } }
            composable(Screen.CycleLog.route) { CycleLogScreen { nav.popBackStack() } }
            composable(Screen.CycleToday.route) { CycleTodayScreen(nav) { nav.popBackStack() } }
            composable(Screen.PeriodTraining.route) { PeriodTrainingScreen { nav.popBackStack() } }
            composable(Screen.Mood.route) { MoodCheckInScreen { nav.popBackStack() } }
            composable(Screen.Mindfulness.route) { MindfulnessScreen { nav.popBackStack() } }

            // --- کسب آرامش و تنفس ---
            composable(Screen.Calm.route) { CalmMenuScreen(nav) }
            composable(Screen.BackgroundMusic.route) { BackgroundMusicScreen { nav.popBackStack() } }
            composable(Screen.Breath.route) { BreathingScreen { nav.popBackStack() } }
            composable(Screen.GratitudeJournal.route) { GratitudeJournalScreen { nav.popBackStack() } }
            composable(Screen.Journal.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.Journal.route) { inclusive = true } }
                }) { JournalScreen { nav.popBackStack() } }
            }

            // --- تمرین‌ها (ذهن‌آگاهی، آرامش، بین دروس، حرکات) ---
            composable(Screen.BetweenLessons.route) { BetweenLessonsHubScreen(nav) { nav.popBackStack() } }
            composable(
                Screen.PracticeGroup.route,
                listOf(navArgument("groupId") { type = NavType.StringType }),
            ) { entry ->
                PracticeGroupScreen(
                    nav = nav,
                    groupId = entry.arguments?.getString("groupId").orEmpty(),
                    onBack = { nav.popBackStack() },
                )
            }
            composable(
                Screen.PracticeItem.route,
                listOf(navArgument("itemId") { type = NavType.StringType }),
            ) { entry ->
                PracticeItemScreen(
                    itemId = entry.arguments?.getString("itemId").orEmpty(),
                    onBack = { nav.popBackStack() },
                    onWellness = { cat -> nav.layerTo(Screen.Wellness.of(cat)) },
                    onRoute = { r -> nav.layerTo(r) },
                )
            }
            composable(
                Screen.Wellness.route,
                listOf(navArgument("cat") { type = NavType.StringType; defaultValue = "" }),
            ) { entry ->
                WellnessScreen(
                    onBack = { nav.popBackStack() },
                    onSketchGallery = { nav.navigate(Screen.SketchGallery.route) },
                    initialCategory = entry.arguments?.getString("cat").orEmpty().ifBlank { null },
                )
            }
            composable(Screen.SketchGallery.route) { SketchGalleryScreen(onBack = { nav.popBackStack() }) }

            // --- محتوای تعاملی روی باکت (یوگا، ورزش، تنفس، آموزش‌ها) ---
            composable(Screen.ContentHub.route) {
                ContentHubScreen(
                    onBack = { nav.popBackStack() },
                    onCategory = { c -> nav.navigate(Screen.ContentCategory.of(c)) },
                )
            }
            composable(
                Screen.ContentCategory.route,
                listOf(navArgument("cat") { type = NavType.StringType }),
            ) { entry ->
                ContentCategoryScreen(
                    cat = entry.arguments?.getString("cat").orEmpty(),
                    onBack = { nav.popBackStack() },
                    onOpen = { id -> nav.navigate(Screen.ContentHtml.of(id)) },
                )
            }
            composable(
                Screen.ContentHtml.route,
                listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                ContentHtmlScreen(
                    itemId = entry.arguments?.getString("id").orEmpty(),
                    onBack = { nav.popBackStack() },
                )
            }

            // --- فضای امن من ---
            composable(Screen.SafeSpace.route) { SafeSpaceScreen(nav) }
            composable(Screen.Diary.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.Diary.route) { inclusive = true } }
                }) { DiaryScreen(onBack = { nav.popBackStack() }, onHelp = { nav.navigate(Screen.UserGuide.of("diary")) }) }
            }
            composable(Screen.Notebooks.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.Notebooks.route) { inclusive = true } }
                }) { NotebooksScreen { nav.popBackStack() } }
            }
            composable(Screen.Poetry.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.Poetry.route) { inclusive = true } }
                }) { PoetryBookScreen(onBack = { nav.popBackStack() }, onHelp = { nav.navigate(Screen.UserGuide.of("poetry")) }) }
            }
            composable(Screen.SafeFreeWriting.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.SafeFreeWriting.route) { inclusive = true } }
                }) { SafeFreeWritingScreen { nav.popBackStack() } }
            }
            composable(Screen.Writing.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.Writing.route) { inclusive = true } }
                }) { WritingPromptScreen { nav.popBackStack() } }
            }
            composable(Screen.SecureGallery.route) {
                SafeContentGuard(onLocked = {
                    nav.navigate(Screen.SafeSpace.route) { popUpTo(Screen.SecureGallery.route) { inclusive = true } }
                }) {
                    SecureMediaGalleryScreen(
                        onBack = { nav.popBackStack() },
                        onHelp = { nav.navigate(Screen.UserGuide.of("album")) },
                    )
                }
            }
            composable(Screen.Helplines.route) { HelplinesScreen { nav.popBackStack() } }
        }
    }
}

/** پروفایل با همان منطقِ قبلی: خواندن از سرور، آینه‌ی محلی، خروج و ذخیره. */
@Composable
private fun UserProfileRoute(nav: NavController) {
    val container = LocalAppContainer.current
    val ctx = LocalContext.current
    val scopeUp = rememberCoroutineScope()
    var profile by remember { mutableStateOf<com.hamyarehealth.ir.ui.profile.StudentProfile?>(null) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        com.hamyarehealth.ir.ui.profile.StudentProfileState.loadAvatarMirror(ctx)
        val uid = runCatching { container.auth.currentUserId() }.getOrNull().orEmpty()
        profile = if (uid.isBlank()) null else
            runCatching { com.hamyarehealth.ir.ui.profile.StudentProfileRepo.fetch(container.tables, uid) }.getOrNull()
        profile?.let { fetched ->
            com.hamyarehealth.ir.ui.profile.StudentProfileState.writeMirror(
                ctx, fetched.grade, true, fetched.firstName, fetched.subscription, fetched.gender,
            )
        }
        if (uid.isNotBlank()) {
            runCatching { com.hamyarehealth.ir.ui.profile.AvatarSync.pull(ctx, container.storage, uid) }
        }
        loading = false
    }
    when {
        loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        else -> com.hamyarehealth.ir.ui.profile.UserProfileScreen(
            profile = profile,
            onBack = { nav.popBackStack() },
            onLogout = {
                scopeUp.launch {
                    runCatching { container.auth.logout() }
                    com.hamyarehealth.ir.ui.profile.StudentProfileState.clearMirror(ctx)
                    (ctx as? com.hamyarehealth.ir.MainActivity)?.onLoggedOut()
                }
            },
            onOpenSubscription = { nav.navigate(Screen.Subscription.route) },
            onSave = { p ->
                val uid = runCatching { container.auth.currentUserId() }.getOrNull().orEmpty()
                val toSave = p.copy(userId = uid, grade = profile?.grade ?: com.hamyarehealth.ir.ui.profile.StudentProfileState.grade)
                val email = toSave.email.ifBlank { runCatching { container.auth.currentUser() }.getOrNull()?.email.orEmpty() }
                val ok = runCatching {
                    com.hamyarehealth.ir.ui.profile.StudentProfileRepo.save(container.tables, email, toSave)
                }.getOrDefault(false)
                if (ok) {
                    com.hamyarehealth.ir.ui.profile.StudentProfileState.writeMirror(
                        ctx, toSave.grade, true, toSave.firstName,
                        profile?.subscription ?: com.hamyarehealth.ir.ui.profile.StudentProfileState.subscription.ifBlank { "free" },
                        toSave.gender,
                    )
                    container.uiPrefs.applyDefaultForGender(toSave.gender)
                    profile = toSave.copy(subscription = profile?.subscription ?: "free")
                    runCatching { com.hamyarehealth.ir.ui.profile.AvatarSync.push(ctx, container.storage, uid) }
                }
                ok
            },
        )
    }
}
