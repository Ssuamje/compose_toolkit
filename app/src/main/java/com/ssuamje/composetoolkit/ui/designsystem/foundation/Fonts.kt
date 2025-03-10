package com.ssuamje.composetoolkit.ui.designsystem.foundation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.ssuamje.composetoolkit.R

/**
 * 디자인 시스템에 사용되는 폰트 오브젝트입니다.
 * 위계를 가진 구조로 사용자가 직관적으로 사용할 수 있도록 작성하였습니다.
 *
 */

object DSFonts {
    object Title {
        val XL = TextStyle(
            fontSize = 32.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 32.sp * -0.01,
            lineHeight = TextUnit(32 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val L = TextStyle(
            fontSize = 28.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 28.sp * -0.01,
            lineHeight = TextUnit(28 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val M = TextStyle(
            fontSize = 24.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 24.sp * -0.01,
            lineHeight = TextUnit(24 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val S = TextStyle(
            fontSize = 20.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 20.sp * -0.01,
            lineHeight = TextUnit(20 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )
    }

    object Subtitle {
        val L = TextStyle(
            fontSize = 20.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 20.sp * -0.01,
            lineHeight = TextUnit(20 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val M = TextStyle(
            fontSize = 18.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 18.sp * -0.01,
            lineHeight = TextUnit(18 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val S = TextStyle(
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 16.sp * -0.01,
            lineHeight = TextUnit(16 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val XS = TextStyle(
            fontSize = 14.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            letterSpacing = 14.sp * -0.01,
            lineHeight = TextUnit(14 * 1.4f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )
    }

    object Body {
        val XL = TextStyle(
            fontSize = 18.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            letterSpacing = 18.sp * -0.01,
            lineHeight = TextUnit(18 * 1.5f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val L = TextStyle(
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            letterSpacing = 16.sp * -0.01,
            lineHeight = TextUnit(16 * 1.5f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val M = TextStyle(
            fontSize = 15.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            letterSpacing = 15.sp * -0.01,
            lineHeight = TextUnit(15 * 1.5f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )

        val S = TextStyle(
            fontSize = 14.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            letterSpacing = 14.sp * -0.01,
            lineHeight = TextUnit(14 * 1.5f, TextUnitType.Sp),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
        )
    }
}

val pretendard = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin),
)