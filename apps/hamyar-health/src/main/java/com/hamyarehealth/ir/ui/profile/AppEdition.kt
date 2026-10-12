package com.hamyarehealth.ir.ui.profile

import com.hamyarehealth.ir.BuildConfig

/**
 * هویت این APK — از flavor پایه می‌آید، نه از انتخاب کاربر.
 * نهم: `com.hamyarehealth.ir` (نصب‌های فعلی). بقیه: `com.hamyarehealth.p04` … `p12`.
 */
object AppEdition {
    val grade: GradeLevel get() = GradeLevel.byId(BuildConfig.GRADE_ID)
    val faNumeral: String get() = BuildConfig.GRADE_FA_NUMERAL
    val booksFolder: String get() = BuildConfig.BOOKS_FOLDER
    val faShort: String get() = BuildConfig.GRADE_FA_SHORT
    val gradeFa: String get() = grade.fa
    val appTitle: String
        get() = if (grade == GradeLevel.G9) "همیار سلامتی من" else "همیار $faShort"
}
