package com.example.data

import com.example.model.AppLanguage
import com.example.model.CounterMode
import com.example.model.NusukType
import com.example.model.RitualBadgeType
import com.example.model.RitualStep
import com.example.model.Scene3DType
import com.example.model.StepIllustrationKey

internal object LocalizedRitualGuideDataExtra {

    fun illustrationLabelsFor(lang: AppLanguage, key: StepIllustrationKey): List<String> = when (lang) {
        AppLanguage.UR -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("لبیک اللهم", "میقات سے احرام")
            StepIllustrationKey.TAWAF -> listOf("گھڑی کی مخالف سمت ↺", "حجرِ اسود ↘")
            StepIllustrationKey.SAI -> listOf("صفا", "سبز نشان", "مروہ")
            StepIllustrationKey.HALQ -> listOf("حلق یا قصر (تحلل)", "مکمل سر منڈوانا یا بال کٹوانا")
            StepIllustrationKey.MINA -> listOf("منیٰ کے خیمے — 8 ذوالحجہ و تشریق")
            StepIllustrationKey.ARAFAT -> listOf("جبلِ رحمت عرفات • رکنِ اعظم")
            StepIllustrationKey.MUZDALIFAH -> listOf("مزدلفہ میں رات اور کنکریاں")
            StepIllustrationKey.JAMARAT -> listOf("صغریٰ ← وسطیٰ ← جمرہ کبریٰ")
            StepIllustrationKey.MADINAH -> listOf("مسجدِ نبوی اور گنبدِ خضرا")
        }
        AppLanguage.ID -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("Labbayk Allahumma", "Ihram dari Miqat")
            StepIllustrationKey.TAWAF -> listOf("Berlawanan Jarum Jam ↺", "Hajar Aswad ↘")
            StepIllustrationKey.SAI -> listOf("Shafa", "Lampu Hijau", "Marwah")
            StepIllustrationKey.HALQ -> listOf("Tahallul: Cukur / Potong Rambut", "Keluar dari Larangan Ihram")
            StepIllustrationKey.MINA -> listOf("Tenda Mina — Tarwiyah & Tasyriq")
            StepIllustrationKey.ARAFAT -> listOf("Jabal Rahmah Arafah • Rukun Haji")
            StepIllustrationKey.MUZDALIFAH -> listOf("Mabit di Muzdalifah & Kerikil")
            StepIllustrationKey.JAMARAT -> listOf("Ula → Wustha → Aqabah")
            StepIllustrationKey.MADINAH -> listOf("Masjid Nabawi & Kubah Hijau")
        }
        AppLanguage.TR -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("Lebbeyk Allahümme", "Mikattan İhram")
            StepIllustrationKey.TAWAF -> listOf("Saat Yönünün Tersi ↺", "Hacerülesved ↘")
            StepIllustrationKey.SAI -> listOf("Safa", "Yeşil Işıklar", "Merve")
            StepIllustrationKey.HALQ -> listOf("Tıraş veya Kısaltma", "İhramdan Çıkış (Tehallül)")
            StepIllustrationKey.MINA -> listOf("Mina Çadırları — Terviye ve Teşrik")
            StepIllustrationKey.ARAFAT -> listOf("Rahmet Tepesi Arafat • Haccın Rüknü")
            StepIllustrationKey.MUZDALIFAH -> listOf("Müzdelife Vakfesi ve Taş Toplama")
            StepIllustrationKey.JAMARAT -> listOf("Küçük → Orta → Büyük Şeytan")
            StepIllustrationKey.MADINAH -> listOf("Mescid-i Nebevi ve Yeşil Kubbe")
        }
        else -> emptyList()
    }

    val urSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep(
                id = "u1", badgeType = RitualBadgeType.WAJIB,
                title = "میقات سے احرام باندھنا",
                description = "غسل کر کے خوشبو لگائیں، احرام کی چادریں پہنیں اور میقات پر عمرہ کی نیت کریں۔",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "مردوں کے لیے دو سفید غیر سلے کپڑوں (ازار و ردا) اور میقات پر نیت کا تصویری خاکہ۔",
                detailedPoints = listOf(
                    "غسل کریں، ناخن تراشیں اور جسم پر خوشبو لگائیں۔",
                    "مرد دو سفید چادریں پہنیں؛ خواتین نقاب اور دستانے کے بغیر سادہ لباس پہنیں۔",
                    "میقات پر نیت کریں: «لبیک اللهم عمرة» اور تلبیہ پڑھیں۔"
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ عُمْرَةً • لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ",
                locationHint = "میقات یا اس کی محاذات"
            ),
            RitualStep(
                id = "u2", badgeType = RitualBadgeType.RUKN,
                title = "عمرہ کا طواف (7 چکر)",
                description = "حجرِ اسود سے شروع کر کے کعبہ کے گرد گھڑی کی مخالف سمت 7 چکر لگائیں۔",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "حجرِ اسود سے شروع ہونے والا طواف کا دائرہ (کعبہ بائیں جانب رہے)۔",
                detailedPoints = listOf(
                    "مرد پہلے 3 چکروں میں اضطباع (دایاں کندھا کھلا) اور رمل (تیز چال) کریں۔",
                    "حجرِ اسود سے آغاز کریں اور حطیم کے باہر سے چکر مکمل کریں۔",
                    "7 چکروں کے بعد مقامِ ابراہیم کے پیچھے 2 رکعت نماز پڑھیں اور آبِ زمزم پئیں۔"
                ),
                stageDua = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                locationHint = "مسجدِ حرام — مطاف"
            ),
            RitualStep(
                id = "u3", badgeType = RitualBadgeType.RUKN,
                title = "صفا اور مروہ کی سعی",
                description = "صفا سے مروہ تک 7 چکر لگائیں؛ مرد سبز بتیوں کے درمیان ہلکا دوڑیں۔",
                scene3D = Scene3DType.MASAA, illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "صفا اور مروہ کی راہداری جس میں سبز ستونوں کے درمیان تیز چلنے کا نشان ہے۔",
                detailedPoints = listOf(
                    "صفا پر چڑھ کر کعبہ کی طرف رخ کریں اور دعا مانگیں۔",
                    "صفا سے مروہ ایک چکر اور واپسی دوسرا چکر شمار ہوگا (کل 7 چکر)۔",
                    "سبز ستونوں کے درمیان مرد تیز چلیں اور ذکر جاری رکھیں۔"
                ),
                stageDua = "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ • اللَّهُ أَكْبَرُ",
                locationHint = "مسعیٰ — صفا و مروہ"
            ),
            RitualStep(
                id = "u4", badgeType = RitualBadgeType.WAJIB,
                title = "حلق یا قصر (احرام سے نکلنا)",
                description = "مرد سر منڈوائیں یا بال کٹوائیں اور خواتین ایک پورے کے برابر بال کاٹ کر حلال ہو جائیں۔",
                scene3D = Scene3DType.MASAA, illustrationKey = StepIllustrationKey.HALQ,
                visualCaption = "مروہ پر ساتویں چکر کے بعد بال کٹوانے یا منڈوانے کا مرحلہ۔",
                detailedPoints = listOf(
                    "مرد پورے سر کا حلق کروائیں (افضل) یا برابر قصر کروائیں۔",
                    "خواتین اپنے بالوں کے سرے سے تقریباً 2 سینٹی میٹر کاٹیں۔",
                    "اس کے بعد عمرہ مکمل ہو کر احرام کی پابندیاں ختم ہو جاتی ہیں۔"
                ),
                stageDua = "اللَّهُمَّ اغْفِرْ لِلْمُحَلِّقِينَ وَالْمُقَصِّرِينَ",
                locationHint = "مروہ کے باہر حجام کی دکانیں"
            )
        ),
        NusukType.TAMATTU to listOf(
            RitualStep(
                id = "t1", badgeType = RitualBadgeType.RUKN,
                title = "عمرہ تمتع اور احرام کھولنا",
                description = "حج کے مہینوں میں مکمل عمرہ ادا کر کے 8 ذوالحجہ تک عام لباس میں رہیں۔",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "حج سے قبل عمرہ کے طواف، سعی اور حلق کی تکمیل۔",
                detailedPoints = listOf("میقات سے عمرہ تمتع کی نیت کریں۔", "طواف، سعی اور بال کٹوا کر احرام کھول دیں۔", "عید کے دن قربانی (ہدی) واجب ہوگی۔"),
                stageDua = "لَبَّيْكَ اللَّهُمَّ عُمْرَةً مُتَمَتِّعاً بِهَا إِلَى الْحَجِّ", locationHint = "میقات ← مکہ"
            ),
            RitualStep(
                id = "t2", badgeType = RitualBadgeType.SUNNAH,
                title = "8 ذوالحجہ — یومِ ترویہ (منیٰ)",
                description = "مکہ سے حج کا احرام باندھ کر منیٰ کے خیموں میں قیام کریں۔",
                scene3D = Scene3DType.JAMARAT, illustrationKey = StepIllustrationKey.MINA,
                visualCaption = "منیٰ کے سفید خیمے جہاں 4 رکعت نمازیں قصر (2 رکعت) پڑھی جاتی ہیں۔",
                detailedPoints = listOf("صبح مکہ میں حج کا احرام باندھیں۔", "منیٰ جا کر ظہر، عصر، مغرب، عشاء اور فجر وقت پر قصر ادا کریں۔", "تلبیہ اور ذکر کثرت سے کریں۔"),
                stageDua = "لَبَّيْكَ اللَّهُمَّ حَجًّا", locationHint = "مکہ ← منیٰ"
            ),
            RitualStep(
                id = "t3", badgeType = RitualBadgeType.RUKN,
                title = "9 ذوالحجہ — وقوفِ عرفات",
                description = "حج کا سب سے بڑا رکن: غروبِ آفتاب تک حدودِ عرفات میں دعا و ذکر کرنا۔",
                scene3D = Scene3DType.ARAFAT, illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "جبلِ رحمت اور میدانِ عرفات؛ حدود کے اندر قیام سے رکن ادا ہوتا ہے۔",
                detailedPoints = listOf("یقین کریں کہ آپ عرفات کی شرعی حدود کے اندر ہیں۔", "ظہر کے وقت ظہر و عصر جمع اور قصر ادا کریں۔", "غروب تک قبلہ رخ ہو کر دعا اور استغفار کریں۔"),
                stageDua = "لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ", locationHint = "میدانِ عرفات"
            ),
            RitualStep(
                id = "t4", badgeType = RitualBadgeType.WAJIB,
                title = "10ویں شب — مزدلفہ میں قیام",
                description = "مغرب کے بعد مزدلفہ پہنچ کر مغرب و عشاء جمع کریں، رات گزاریں اور کنکریاں چنیں۔",
                scene3D = Scene3DType.JAMARAT, illustrationKey = StepIllustrationKey.MUZDALIFAH,
                visualCaption = "مزدلفہ میں کھلے آسمان تلے رات گزارنے اور کنکریاں جمع کرنے کا منظر۔",
                detailedPoints = listOf("مزدلفہ پہنچ کر مغرب اور عشاء قصر کے ساتھ جمع تاخیر سے پڑھیں۔", "فجر تک قیام کریں اور جمرہ کبریٰ کے لیے 7 کنکریاں چنیں۔", "فجر کے بعد مشعر الحرام کے پاس دعا کریں۔"),
                stageDua = "اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا", locationHint = "مزدلفہ"
            ),
            RitualStep(
                id = "t5", badgeType = RitualBadgeType.WAJIB,
                title = "10 ذوالحجہ — یومِ نحر اور رمی",
                description = "جمرہ کبریٰ کو 7 کنکریاں ماریں، قربانی کریں اور حلق یا قصر کروائیں۔",
                scene3D = Scene3DType.JAMARAT, illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "عید کے دن صرف بڑے شیطان (جمرہ کبریٰ) کو 7 کنکریاں ماری جاتی ہیں۔",
                detailedPoints = listOf("ہر کنکری پر «اللہ اکبر» کہہ کر 7 کنکریاں ماریں۔", "ہدی کی قربانی کریں (یا سرکاری ٹوکن استعمال کریں)۔", "حلق یا قصر کروا کر تحللِ اول حاصل کریں۔"),
                stageDua = "اللَّهُ أَكْبَرُ", locationHint = "جمرات پل — منیٰ"
            ),
            RitualStep(
                id = "t6", badgeType = RitualBadgeType.RUKN,
                title = "طوافِ افاضہ اور حج کی سعی",
                description = "مکہ جا کر طوافِ افاضہ (7 چکر) اور حج کی سعی (7 چکر) ادا کریں۔",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "کعبہ کے 7 چکر اور صفا و مروہ کی سعی سے تحللِ اکبر مکمل ہوتا ہے۔",
                detailedPoints = listOf("طوافِ افاضہ کے 7 چکر اور 2 رکعت نماز پڑھیں۔", "صفا و مروہ کے درمیان 7 چکر سعی کریں۔", "اس سے احرام کی تمام پابندیاں ختم ہو جاتی ہیں۔"),
                stageDua = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً", locationHint = "مسجدِ حرام — مکہ"
            ),
            RitualStep(
                id = "t7", badgeType = RitualBadgeType.WAJIB,
                title = "11 تا 13 ذوالحجہ — ایامِ تشریق",
                description = "منیٰ میں رات گزاریں اور زوال کے بعد ترتیب سے تینوں جمرات کو کنکریاں ماریں۔",
                scene3D = Scene3DType.JAMARAT, illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "ترتیب: صغریٰ ← وسطیٰ ← کبریٰ (ہر ایک کو 7 کنکریاں)۔",
                detailedPoints = listOf("پہلے صغریٰ (7 کنکریاں) مار کر قبلہ رخ دعا کریں۔", "پھر وسطیٰ (7 کنکریاں) مار کر دعا کریں۔", "آخر میں کبریٰ (7 کنکریاں) مار کر دعا کے بغیر لوٹ جائیں۔"),
                stageDua = "اللَّهُ أَكْبَرُ", locationHint = "منیٰ و جمرات"
            ),
            RitualStep(
                id = "t8", badgeType = RitualBadgeType.WAJIB,
                title = "طوافِ وداع",
                description = "مکہ سے روانگی سے پہلے آخری 7 چکر طوافِ وداع ادا کریں۔",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "سفرِ واپسی سے قبل بیت اللہ کا الوداعی طواف۔",
                detailedPoints = listOf("روانگی سے عین پہلے 7 چکر اور 2 رکعت نماز ادا کریں۔", "حائضہ خواتین کو طوافِ وداع معاف ہے۔"),
                stageDua = "اللَّهُمَّ تَقَبَّلْ مِنَّا", locationHint = "مسجدِ حرام"
            )
        ),
        NusukType.IFRAD to listOf(
            RitualStep("i1", RitualBadgeType.RUKN, "صرف حج کا احرام (افراد)", "میقات سے صرف حج کی نیت کریں اور یومِ نحر تک احرام میں رہیں (ہدی واجب نہیں)۔", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "صرف حج کی نیت اور مسلسل احرام۔", listOf("میقات پر کہیں: «لبیک اللهم حجاً»۔", "طوافِ قدوم کے بعد بال نہ کٹوائیں۔", "افراد میں قربانی واجب نہیں۔"), "لَبَّيْكَ اللَّهُمَّ حَجًّا", "میقات"),
            RitualStep("i2", RitualBadgeType.SUNNAH, "طوافِ قدوم اور سعی", "مکہ پہنچ کر طوافِ قدوم کریں اور چاہیں تو حج کی سعی پہلے ادا کر لیں۔", Scene3DType.MASAA, StepIllustrationKey.SAI, "طوافِ قدوم اور صفا و مروہ کی سعی۔", listOf("7 چکر طوافِ قدوم کریں۔", "حج کی سعی ساتھ ادا کی جا سکتی ہے۔", "احرام برقرار رکھیں۔"), "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ", "مکہ"),
            RitualStep("i3", RitualBadgeType.RUKN, "منیٰ، عرفات اور مزدلفہ", "8 کو منیٰ، 9 کو عرفات میں وقوف اور مزدلفہ میں رات گزاریں۔", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "منیٰ ← عرفات ← مزدلفہ کا روحانی سفر۔", listOf("8 ذوالحجہ کو منیٰ میں قیام۔", "9 ذوالحجہ کو غروب تک عرفات میں دعا۔", "مزدلفہ میں نمازیں اور کنکریاں۔"), "لا إِلَهَ إِلا اللَّهُ وَحْدَهُ", "مشاعرِ مقدسہ"),
            RitualStep("i4", RitualBadgeType.RUKN, "رمی، افاضہ، تشریق اور وداع", "جمرہ کبریٰ، حلق، طوافِ افاضہ، ایامِ تشریق اور طوافِ وداع مکمل کریں۔", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "یومِ نحر اور ایامِ تشریق کی رمی اور طواف۔", listOf("10 کو رمی اور حلق کروائیں۔", "طوافِ افاضہ ادا کریں۔", "تشریق کی رمی کے بعد طوافِ وداع کریں۔"), "اللَّهُ أَكْبَرُ", "منیٰ ← مکہ")
        ),
        NusukType.QIRAN to listOf(
            RitualStep("q1", RitualBadgeType.RUKN, "عمرہ و حج کا مشترکہ احرام", "میقات سے عمرہ اور حج دونوں کی اکٹھی نیت کریں اور ہدی لازم ہوگی۔", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "ایک ہی احرام میں عمرہ و حج کی نیت۔", listOf("میقات پر کہیں: «لبیک اللهم عمرة وحجاً»۔", "یومِ نحر تک احرام برقرار رکھیں۔", "قارن پر ہدی واجب ہے۔"), "لَبَّيْكَ اللَّهُمَّ عُمْرَةً وَحَجًّا", "میقات"),
            RitualStep("q2", RitualBadgeType.SUNNAH, "طوافِ قدوم اور ایک سعی", "طوافِ قدوم کے بعد ایک سعی عمرہ و حج دونوں کے لیے کافی ہے۔", Scene3DType.MASAA, StepIllustrationKey.TAWAF, "ایک مشترکہ سعی جو دونوں مناسک کو کافی ہو۔", listOf("طوافِ قدوم کے 7 چکر لگائیں۔", "عمرہ و حج کے لیے ایک ہی سعی کریں۔", "ابھی بال نہ کٹوائیں۔"), "اللَّهُمَّ بَارِكْ لَنَا فِي نُسُكِنَا", "مسجدِ حرام"),
            RitualStep("q3", RitualBadgeType.RUKN, "عرفات، منیٰ، افاضہ اور وداع", "عرفات، مزدلفہ، رمی، ہدی، حلق، طوافِ افاضہ اور طوافِ وداع ادا کریں۔", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "حج کے تمام ارکان و واجبات کی تکمیل۔", listOf("عرفات میں وقوف اور مزدلفہ میں قیام۔", "10 کو رمی، قربانی، حلق اور طوافِ افاضہ۔", "تشریق کی رمی اور طوافِ وداع۔"), "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً", "عرفات ← منیٰ ← مکہ")
        ),
        NusukType.MADINAH to listOf(
            RitualStep("m1", RitualBadgeType.SUNNAH, "مسجدِ نبوی کی زیارت", "مسجدِ نبوی میں ایک نماز ہزار نمازوں سے بہتر ہے (سوائے مسجدِ حرام کے)۔", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "گنبدِ خضرا اور مسجدِ نبوی کی سایہ دار چھتریوں کا منظر۔", listOf("دایاں قدم رکھ کر ادب سے داخل ہوں۔", "دو رکعت تحیۃ المسجد ادا کریں۔", "آواز پست رکھیں اور درود پڑھیں۔"), "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", "مسجدِ نبوی — مدینہ"),
            RitualStep("m2", RitualBadgeType.SUNNAH, "ریاض الجنہ اور روضۂ رسول ﷺ پر سلام", "نسک ایپ پر اجازت نامے کے ساتھ ریاض الجنہ میں نماز اور نبی ﷺ پر سلام۔", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "منبرِ رسول اور حجرۂ مبارکہ کے درمیان ریاض الجنہ۔", listOf("نسک ایپ سے ریاض الجنہ کا پرمٹ حاصل کریں۔", "ادب سے کھڑے ہو کر السلام علیک یا رسول اللہ کہیں۔", "حضرت ابوبکر و عمر رضی اللہ عنہما پر سلام عرض کریں۔"), "السَّلامُ عَلَيْكَ يَا رَسُولَ اللَّهِ", "ریاض الجنہ"),
            RitualStep("m3", RitualBadgeType.SUNNAH, "مسجدِ قبا، جنت البقیع اور احد", "مسجدِ قبا میں 2 رکعت نماز (عمرہ کے برابر ثواب) اور بقیع و احد کی زیارت۔", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "مدینہ منورہ کے مقدس تاریخی مقامات۔", listOf("قیام گاہ سے وضو کر کے مسجدِ قبا میں 2 رکعت پڑھیں۔", "جنت البقیع کے باہر سے اہلِ بقیع کو سلام و دعا کریں۔", "شہدائے احد کی زیارت کریں۔"), "السَّلامُ عَلَيْكُمْ دَارَ قَوْمٍ مُؤْمِنِينَ", "قبا و بقیع و احد")
        )
    )

    val idSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep("u1", RitualBadgeType.WAJIB, "Ihram dari Miqat", "Mandi sunnah, memakai kain ihram, dan berniat umrah saat melewati Miqat.", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Ilustrasi dua helai kain ihram putih tanpa jahitan (Izar & Rida) serta niat di Miqat.", listOf("Mandi besar, potong kuku, dan pakai wewangian di badan sebelum niat.", "Pria memakai 2 lembar kain putih; wanita memakai pakaian menutup aurat tanpa cadar/sarung tangan.", "Niat di Miqat: 'Labbayk Allahumma Umrah' lalu memperbanyak Talbiyah."), "Labbayk Allahumma Umrah • Labbayk Allahumma labbayk", "Batas Miqat"),
            RitualStep("u2", RitualBadgeType.RUKN, "Thawaf Umrah (7 Putaran)", "Mengelilingi Ka'bah 7 putaran berlawanan arah jarum jam mulai dari Hajar Aswad.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Lintasan melingkar berlawanan arah jarum jam dengan posisi Ka'bah di sebelah kiri.", listOf("Pria melakukan Idhtiba' dan lari kecil (Raml) pada 3 putaran pertama.", "Mulai sejajar Hajar Aswad, hindari melintas di dalam Hijr Ismail.", "Setelah 7 putaran, shalat 2 rakaat di belakang Maqam Ibrahim dan minum Zamzam."), "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar", "Masjidil Haram — Mataf"),
            RitualStep("u3", RitualBadgeType.RUKN, "Sa'i Shafa & Marwah", "Berjalan 7 kali lintasan antara Bukit Shafa dan Marwah.", Scene3DType.MASAA, StepIllustrationKey.SAI, "Koridor Shafa–Marwah dengan penanda lampu hijau sebagai area lari-lari kecil bagi pria.", listOf("Naik ke Bukit Shafa menghadap Ka'bah sambil berdoa.", "Shafa ke Marwah dihitung 1, Marwah ke Shafa dihitung 2 (total 7 lintasan).", "Pria berlari kecil di antara dua lampu hijau."), "Innas-Safa wal-Marwata min sha'a'irillah • Allahu Akbar", "Mas'a — Shafa & Marwah"),
            RitualStep("u4", RitualBadgeType.WAJIB, "Tahallul (Cukur / Potong Rambut)", "Mencukur habis atau memotong pendek rambut secara merata untuk keluar dari ihram.", Scene3DType.MASAA, StepIllustrationKey.HALQ, "Tahap akhir umrah di Marwah untuk melepaskan seluruh larangan ihram.", listOf("Pria dianjurkan mencukur gundul (Halq) atau memendekkan rambut merata.", "Wanita memotong ujung rambut sepanjang satu ruas jari (~2 cm).", "Seluruh larangan ihram kini telah berakhir."), "Allahumma-ghfir lil-muhalliqina wal-muqassirin", "Pintu Keluar Marwah")
        ),
        NusukType.TAMATTU to listOf(
            RitualStep("t1", RitualBadgeType.RUKN, "Umrah Tamattu & Tahallul", "Menyelesaikan umrah terlebih dahulu di bulan haji lalu melepas ihram hingga 8 Dzulhijjah.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Thawaf, Sa'i, dan tahallul umrah sebelum memasuki rangkaian Haji.", listOf("Niat Umrah Tamattu di Miqat.", "Selesaikan Thawaf, Sa'i, dan potong rambut.", "Wajib menyembelih Hady pada hari Idul Adha."), "Labbayk Allahumma Umratan mutamatti'an biha ilal-Hajj", "Miqat → Makkah"),
            RitualStep("t2", RitualBadgeType.SUNNAH, "8 Dzulhijjah — Hari Tarwiyah di Mina", "Berihram haji dari penginapan di Makkah dan bermalam di tenda Mina.", Scene3DType.JAMARAT, StepIllustrationKey.MINA, "Kota tenda putih di Mina tempat shalat 4 rakaat diqashar menjadi 2 rakaat.", listOf("Mandi dan berihram haji di Makkah pada pagi 8 Dzulhijjah.", "Menuju Mina dan shalat Dzuhur, Ashar, Maghrib, Isya, Subuh secara qashar.", "Perbanyak Talbiyah dan dzikir."), "Labbayk Allahumma Hajjan", "Makkah → Mina"),
            RitualStep("t3", RitualBadgeType.RUKN, "9 Dzulhijjah — Wukuf di Arafah", "Rukun haji teragung: berada di dalam batas Arafah hingga matahari terbenam.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Jabal Rahmah dan padang Arafah; seluruh area dalam batas sah untuk wukuf.", listOf("Pastikan berada di dalam batas resmi Arafah.", "Shalat Dzuhur dan Ashar dijamak taqdim dan diqashar.", "Fokus berdoa dan berdzikir menghadap kiblat hingga Maghrib."), "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd", "Padang Arafah"),
            RitualStep("t4", RitualBadgeType.WAJIB, "Malam 10 — Mabit di Muzdalifah", "Berangkat ke Muzdalifah setelah Maghrib, jamak shalat, bermalam, dan ambil kerikil.", Scene3DType.JAMARAT, StepIllustrationKey.MUZDALIFAH, "Mabit beratapkan langit di Muzdalifah sambil mengumpulkan 7 kerikil.", listOf("Shalat Maghrib & Isya jamak ta'khir dan qashar saat tiba.", "Bermalam hingga Subuh (lansia/wanita boleh berangkat setelah tengah malam).", "Kumpulkan 7 kerikil sebesar biji kacang untuk melontar Aqabah."), "Allahumma-j'alhu Hajjan mabrura", "Muzdalifah"),
            RitualStep("t5", RitualBadgeType.WAJIB, "10 Dzulhijjah — Hari Nahr & Lempar Jumrah", "Melontar Jumrah Aqabah (7 kerikil), menyembelih Hady, dan mencukur rambut (Tahallul Awal).", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Jembatan Jamarat; pada pagi 10 Dzulhijjah hanya Jumrah Aqabah yang dilontar.", listOf("Lontar 7 kerikil satu per satu ke Jumrah Kubra sambil bertakbir.", "Tunaikan Hady (atau melalui kupon Adahi resmi).", "Cukur/potong rambut untuk Tahallul Awal."), "Allahu Akbar (setiap lemparan)", "Jembatan Jamarat — Mina"),
            RitualStep("t6", RitualBadgeType.RUKN, "Thawaf Ifadhah & Sa'i Haji", "Rukun haji di Masjidil Haram yang menandai Tahallul Akbar (bebas seluruh larangan).", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "7 putaran Thawaf Ifadhah diikuti 7 lintasan Sa'i Shafa–Marwah.", listOf("Laksanakan 7 putaran Thawaf Ifadhah dan shalat 2 rakaat.", "Lanjutkan 7 kali lintasan Sa'i haji.", "Seluruh larangan ihram telah gugur."), "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasana", "Masjidil Haram"),
            RitualStep("t7", RitualBadgeType.WAJIB, "11–13 Dzulhijjah — Hari Tasyriq", "Bermalam di Mina dan melontar ketiga jumrah secara berurutan setelah Dzuhur.", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Urutan lontar: Ula → Wustha → Aqabah (masing-masing 7 kerikil).", listOf("Lontar Ula (7 kerikil) lalu berdoa menghadap kiblat.", "Lontar Wustha (7 kerikil) lalu berdoa menghadap kiblat.", "Lontar Aqabah (7 kerikil) lalu langsung meninggalkan tempat."), "Allahu Akbar", "Mina & Jamarat"),
            RitualStep("t8", RitualBadgeType.WAJIB, "Thawaf Wada' (Perpisahan)", "7 putaran terakhir mengelilingi Ka'bah tepat sebelum meninggalkan Makkah.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Thawaf penutup sebelum perjalanan pulang ke tanah air.", listOf("Lakukan 7 putaran tanpa Raml sesaat sebelum keluar Makkah.", "Shalat 2 rakaat di belakang Maqam Ibrahim.", "Dimaafkan bagi wanita yang sedang haid atau nifas."), "Allahumma taqabbal minna", "Masjidil Haram")
        ),
        NusukType.IFRAD to listOf(
            RitualStep("i1", RitualBadgeType.RUKN, "Ihram Haji Saja (Ifrad)", "Berniat haji saja di Miqat dan tetap berihram hingga hari Idul Adha (tanpa wajib Hady).", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Ihram tunggal khusus ibadah haji.", listOf("Ucapkan niat: 'Labbayk Allahumma Hajjan'.", "Tetap dalam ihram setelah Thawaf Qudum.", "Tidak wajib membayar dam Hady."), "Labbayk Allahumma Hajjan", "Miqat"),
            RitualStep("i2", RitualBadgeType.SUNNAH, "Thawaf Qudum & Sa'i Haji", "Melakukan Thawaf Kedatangan dan dapat langsung mendahulukan Sa'i haji.", Scene3DType.MASAA, StepIllustrationKey.SAI, "Thawaf Qudum lalu Sa'i Shafa–Marwah tanpa mencukur rambut.", listOf("7 putaran Thawaf Qudum.", "Boleh mendahulukan Sa'i haji.", "Tetap mengenakan pakaian ihram."), "Subhanallahi walhamdulillah", "Makkah"),
            RitualStep("i3", RitualBadgeType.RUKN, "Mina, Arafah & Muzdalifah", "Tarwiyah di Mina, Wukuf di Arafah (9), dan Mabit di Muzdalifah.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Rangkaian puncak haji di Mina, Arafah, dan Muzdalifah.", listOf("Bermalam di Mina tanggal 8.", "Wukuf di Arafah tanggal 9 hingga Maghrib.", "Mabit dan ambil kerikil di Muzdalifah."), "La ilaha illallahu wahdahu", "Mina → Arafah → Muzdalifah"),
            RitualStep("i4", RitualBadgeType.RUKN, "Idul Adha, Ifadhah & Wada'", "Lontar Aqabah, cukur, Thawaf Ifadhah, Tasyriq, lalu Thawaf Wada'.", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Pelaksanaan jumrah di Mina dan thawaf penutup di Makkah.", listOf("Tanggal 10: lontar Aqabah dan cukur rambut.", "Thawaf Ifadhah di Makkah.", "Lontar Tasyriq di Mina lalu Thawaf Wada'."), "Allahu Akbar", "Mina → Makkah")
        ),
        NusukType.QIRAN to listOf(
            RitualStep("q1", RitualBadgeType.RUKN, "Ihram Gabungan Umrah & Haji", "Menyatukan niat umrah dan haji dalam satu ihram sejak Miqat dengan kewajiban Hady.", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Satu ihram untuk umrah dan haji sekaligus.", listOf("Niat di Miqat: 'Labbayk Allahumma Umratan wa Hajjan'.", "Tetap berihram hingga 10 Dzulhijjah.", "Wajib menyembelih Hady."), "Labbayk Allahumma Umratan wa Hajjan", "Miqat"),
            RitualStep("q2", RitualBadgeType.SUNNAH, "Thawaf Qudum & Satu Sa'i", "Satu kali Sa'i setelah Thawaf Qudum sudah mencukupi untuk umrah dan haji.", Scene3DType.MASAA, StepIllustrationKey.TAWAF, "Satu Thawaf Qudum dan satu Sa'i gabungan.", listOf("Lakukan 7 putaran Thawaf Qudum.", "Satu kali Sa'i mencukupi dua ibadah.", "Belum memotong rambut hingga 10 Dzulhijjah."), "Allahumma barik lana fi nusukina", "Masjidil Haram"),
            RitualStep("q3", RitualBadgeType.RUKN, "Arafah, Mina, Ifadhah & Wada'", "Menyempurnakan wukuf Arafah, Muzdalifah, jumrah, Hady, cukur, Ifadhah, dan Wada'.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Seluruh rangkaian puncak haji hingga Thawaf Wada'.", listOf("Wukuf di Arafah dan mabit di Muzdalifah.", "Tanggal 10: lontar Aqabah, Hady, cukur, dan Thawaf Ifadhah.", "Lontar Tasyriq lalu Thawaf Wada'."), "Rabbana atina fid-dunya hasanah", "Arafah → Mina → Makkah")
        ),
        NusukType.MADINAH to listOf(
            RitualStep("m1", RitualBadgeType.SUNNAH, "Ziarah Masjid Nabawi", "Satu shalat di Masjid Nabawi lebih utama dari seribu shalat di masjid lain selain Masjidil Haram.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Kubah Hijau, menara, dan payung pelataran Masjid Nabawi di Madinah.", listOf("Masuk dengan kaki kanan penuh ketenangan.", "Shalat Tahiyatul Masjid 2 rakaat.", "Menjaga adab dan merendahkan suara."), "Allahumma salli wa sallim 'ala nabiyyina Muhammad", "Masjid Nabawi — Madinah"),
            RitualStep("m2", RitualBadgeType.SUNNAH, "Raudhah & Salam kepada Nabi ﷺ", "Shalat di Raudhah (dengan izin Nusuk) serta mengucapkan salam kepada Rasulullah ﷺ.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Taman surga Raudhah antara Mimbar dan Makam Rasulullah ﷺ.", listOf("Pesan jadwal izin Raudhah melalui aplikasi Nusuk.", "Ucapkan salam dengan penuh adab di hadapan Makam Nabi ﷺ.", "Sampaikan salam kepada Abu Bakar dan Umar radhiyallahu 'anhuma."), "As-salamu alayka ya Rasulallah wa rahmatullahi wa barakatuh", "Raudhah asy-Syarifah"),
            RitualStep("m3", RitualBadgeType.SUNNAH, "Masjid Quba, Baqi & Uhud", "Shalat 2 rakaat di Masjid Quba (pahala setara umrah) dan berziarah ke Baqi serta Uhud.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Situs bersejarah di Madinah untuk shalat sunnah dan mendoakan para sahabat.", listOf("Berwudhu dari hotel lalu shalat 2 rakaat di Masjid Quba.", "Ucapkan salam ahli kubur di gerbang Baqi.", "Ziarah ke makam syuhada Uhud."), "As-salamu alaykum dara qawmin mu'minin", "Quba, Baqi & Uhud")
        )
    )

    val trSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep("u1", RitualBadgeType.WAJIB, "Mikattan İhrama Giriş", "Gusül abdesti alıp güzel koku sürünün, ihram örtülerini giyin ve mikatta niyet edin.", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Erkekler için dikişsiz iki beyaz ihram örtüsü (İzar ve Rida) ve mikatta niyet şeması.", listOf("Niyetten önce gusül alın, tırnak kesin ve bedene koku sürün.", "Erkekler iki beyaz havlu giyer; kadınlar peçe ve eldivensiz tesettürlü kıyafet giyer.", "Mikatta niyet edin: 'Lebbeyk Allahümme Umreh' ve Telbiye getirin."), "Lebbeyk Allahümme Umreh • Lebbeyk Allahümme lebbeyk", "Mikat Sınırı"),
            RitualStep("u2", RitualBadgeType.RUKN, "Umre Tavafı (7 Şavt)", "Hacerülesved hizasından başlayarak Kabe'nin etrafında saat yönünün tersine 7 kez dönün.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Kabe solunuzda kalacak şekilde Hacerülesved'den başlayıp biten 7 şavtlık dairesel rota.", listOf("Erkekler ilk 3 şavtta Iztıba (sağ omuz açık) ve Remel (hızlı adımlar) yapar.", "Hacerülesved'e yönelip tekbir alın, Hicr-i İsmail'in dışından dolaşın.", "7 şavttan sonra Makam-ı İbrahim arkasında 2 rekat namaz kılıp Zemzem için."), "Rabbena atina fid-dünya haseneten ve fil-ahireti haseneten ve kına azaben-nar", "Mescid-i Haram — Tavaf Alanı"),
            RitualStep("u3", RitualBadgeType.RUKN, "Safa ile Merve Arasında Sa'y", "Safa'dan başlayıp Merve'de biten 7 gidiş-geliş şavtını tamamlayın.", Scene3DType.MASAA, StepIllustrationKey.SAI, "Safa ve Merve tepelerini bağlayan koridor ve erkeklerin hızlı yürüdüğü yeşil ışık bölgesi.", listOf("Safa tepesine çıkıp Kabe'ye dönerek dua edin.", "Safa'dan Merve'ye 1, Merve'den Safa'ya 2 olarak toplam 7 şavt yürüyün.", "Erkekler iki yeşil ışık arasında hafif koşar (hervele)."), "İnnes-Safa vel-Mervete min şeairillah • Allahu Ekber", "Mes'a — Safa ve Merve"),
            RitualStep("u4", RitualBadgeType.WAJIB, "Tıraş veya Kısaltma (İhramdan Çıkış)", "Saçı tamamen tıraş ederek veya kısaltarak umreyi tamamlayın ve ihramdan çıkın.", Scene3DType.MASAA, StepIllustrationKey.HALQ, "Merve çıkışında saç tıraşı veya kısaltma ile ihram yasaklarının sona ermesi.", listOf("Erkekler başın tamamını tıraş eder (faziletli) veya eşit kısaltır.", "Kadınlar saç uçlarından parmak ucu (~2 cm) kadar keser.", "Böylece umre tamamlanır ve tüm ihram yasakları kalkar."), "Allahümmeğfir lil-muhallikine vel-mukassirin", "Merve Çıkışı")
        ),
        NusukType.TAMATTU to listOf(
            RitualStep("t1", RitualBadgeType.RUKN, "Temettü Umresi ve İhramdan Çıkış", "Hac aylarında tam bir umre yapıp 8 Zilhicce'ye kadar ihramdan çıkın.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Hac öncesi umre tavafı, sa'yi ve saç kısaltma aşaması.", listOf("Mikatta Temettü umresi için ihrama girin.", "Tavaf, sa'y ve tıraş ile Mekke'de ihramdan çıkın.", "Bayram günü Hedy (şükür kurbanı) kesmek vaciptir."), "Lebbeyk Allahümme Umreten mütemettian biha ilel-Hac", "Mikat → Mekke"),
            RitualStep("t2", RitualBadgeType.SUNNAH, "8 Zilhicce — Terviye Günü (Mina)", "Mekke'den hac için tekrar ihrama girip Mina çadırlarında geceleyin.", Scene3DType.JAMARAT, StepIllustrationKey.MINA, "4 rekatlık namazların kısaltılarak (2 rekat) kılındığı Mina çadır kenti.", listOf("8 Zilhicce sabahı Mekke'de hac için ihrama girin.", "Mina'ya geçip Öğle, İkindi, Akşam, Yatsı ve Sabah namazlarını vaktinde kısaltarak kılın.", "Telbiye ve zikri artırın."), "Lebbeyk Allahümme Haccen", "Mekke → Mina"),
            RitualStep("t3", RitualBadgeType.RUKN, "9 Zilhicce — Arafat Vakfesi", "Haccın en büyük rüknü: gün batımına kadar Arafat sınırları içinde dua ve vakfe yapın.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Rahmet Tepesi ve Arafat ovası; sınırlar içinde bulunmak rüknü gerçekleştirir.", listOf("Arafat sınırları içinde bulunduğunuzdan emin olun.", "Öğle vaktinde Öğle ve İkindi'yi cem-i takdim ve kasr ile kılın.", "Gün batımına kadar kıbleye dönük içtenlikle dua edin."), "La ilahe illallahu vahdehu la şerike leh, lehül-mülkü ve lehül-hamd", "Arafat Ovası"),
            RitualStep("t4", RitualBadgeType.WAJIB, "10. Gece — Müzdelife Vakfesi", "Akşamdan sonra Müzdelife'ye inip namazları cem edin, geceleyin ve taş toplayın.", Scene3DType.JAMARAT, StepIllustrationKey.MUZDALIFAH, "Müzdelife'de açık havada geceleme ve şeytan taşlama için 7 taş toplama.", listOf("Müzdelife'ye varınca Akşam ve Yatsı'yı cem-i tehir ile kılın.", "Sabah namazına kadar kalın (yaşlılar/kadınlar gece yarısından sonra ayrılabilir).", "Büyük Cemre için nohut büyüklüğünde 7 taş toplayın."), "Allahümmec'alhu Haccen mebruren", "Müzdelife"),
            RitualStep("t5", RitualBadgeType.WAJIB, "10 Zilhicce — Bayram ve Büyük Şeytan Taşlama", "Büyük Cemre'ye 7 taş atın, Hedy kurbanını kesin ve tıraş olun (İlk Tehallül).", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Cemerat Köprüsü; bayram sabahı yalnızca Büyük Cemre (Akabe) taşlanır.", listOf("Telbiyeyi kesip her taşta 'Allahu Ekber' diyerek 7 taş atın.", "Hedy kurbanını kesin (veya resmi vekalet verin).", "Saç tıraşı/kısaltma ile ilk tehallülü tamamlayın."), "Allahu Ekber (her taşta)", "Cemerat Köprüsü — Mina"),
            RitualStep("t6", RitualBadgeType.RUKN, "Ziyaret (İfaza) Tavafı ve Hac Sa'yi", "Mekke'de 7 şavt İfaza Tavafı ve 7 şavt Sa'y ile tam tehallüle ulaşın.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Kabe etrafında 7 şavt ardından Safa–Merve arası 7 şavt sa'y.", listOf("7 şavt Ziyaret Tavafı yapıp 2 rekat namaz kılın.", "Temettü haccı için 7 şavt Sa'y yapın.", "Kalan tüm ihram yasakları sona erer."), "Rabbena atina fid-dünya haseneten ve fil-ahireti haseneh", "Mescid-i Haram"),
            RitualStep("t7", RitualBadgeType.WAJIB, "11–13 Zilhicce — Teşrik Günleri", "Mina'da geceleyip zevalden sonra sırayla 3 cemreyi taşlayın.", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Taşlama sırası: Küçük → Orta → Büyük Cemre (her birine 7 taş).", listOf("Öğleden sonra Küçük Cemre'ye 7 taş atıp kıbleye dönerek dua edin.", "Orta Cemre'ye 7 taş atıp sola çekilerek dua edin.", "Büyük Cemre'ye 7 taş atıp beklemeden ayrılın."), "Allahu Ekber", "Mina ve Cemerat"),
            RitualStep("t8", RitualBadgeType.WAJIB, "Veda Tavafı", "Mekke'den ayrılmadan hemen önce yapılan son 7 şavtlık tavaf.", Scene3DType.KAABA, StepIllustrationKey.TAWAF, "Dönüş yolculuğu öncesi Kabe ile son vedalaşma tavafı.", listOf("Yolculuk öncesi remelsiz 7 şavt tavaf ve 2 rekat namaz kılın.", "Hayız ve nifas halindeki kadınlardan veda tavafı düşer."), "Allahümme tekabbel minna", "Mescid-i Haram")
        ),
        NusukType.IFRAD to listOf(
            RitualStep("i1", RitualBadgeType.RUKN, "Yalnız Hac İçin İhram (İfrad)", "Mikatta sadece hacca niyet edip bayram gününe kadar ihramda kalın (kurban gerekmez).", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Sadece hac ibadetine adanmış kesintisiz ihram.", listOf("Mikatta 'Lebbeyk Allahümme Haccen' deyin.", "Kudüm tavafından sonra tıraş olmayın.", "İfrad haccında Hedy kurbanı vacip değildir."), "Lebbeyk Allahümme Haccen", "Mikat"),
            RitualStep("i2", RitualBadgeType.SUNNAH, "Kudüm Tavafı ve Hac Sa'yi", "Mekke'ye varınca Kudüm Tavafı yapın; dilerseniz hac sa'yini öne alabilirsiniz.", Scene3DType.MASAA, StepIllustrationKey.SAI, "İhramı bozmadan Kudüm Tavafı ve sa'y.", listOf("7 şavt Kudüm Tavafı yapın.", "Hac sa'yini peşinden öne alabilirsiniz.", "Bayrama kadar ihramda kalın."), "Sübhanallahi velhamdülillah", "Mekke"),
            RitualStep("i3", RitualBadgeType.RUKN, "Mina, Arafat ve Müzdelife", "8'de Mina, 9'da Arafat Vakfesi ve Müzdelife gecesi.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Mina → Arafat → Müzdelife kutsal güzergahı.", listOf("8 Zilhicce'de Mina'da konaklayın.", "9 Zilhicce'de gün batımına kadar Arafat'ta dua edin.", "Müzdelife'de namaz ve taş toplama."), "La ilahe illallahu vahdeh", "Mina → Arafat → Müzdelife"),
            RitualStep("i4", RitualBadgeType.RUKN, "Bayram, İfaza, Teşrik ve Veda", "Akabe taşlama, tıraş, İfaza Tavafı, teşrik taşlamaları ve Veda Tavafı.", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT, "Cemerat taşlaması ve Mekke'deki son tavaflar.", listOf("10'da Akabe taşlaması ve tıraş.", "Mekke'de İfaza Tavafı.", "Teşrik taşlamaları ve Veda Tavafı."), "Allahu Ekber", "Mina → Mekke")
        ),
        NusukType.QIRAN to listOf(
            RitualStep("q1", RitualBadgeType.RUKN, "Umre ve Hacca Birlikte İhram", "Mikattan itibaren tek ihramda hem umre hem hac niyeti (Hedy kurbanı vaciptir).", Scene3DType.KAABA, StepIllustrationKey.IHRAM, "Umre ve haccı birleştiren tek ihram hali.", listOf("Mikatta 'Lebbeyk Allahümme Umreten ve Haccen' deyin.", "Bayrama kadar ihramdan çıkmayın.", "Kıran haccında Hedy kurbanı vaciptir."), "Lebbeyk Allahümme Umreten ve Haccen", "Mikat"),
            RitualStep("q2", RitualBadgeType.SUNNAH, "Kudüm Tavafı ve Tek Sa'y", "Kudüm tavafından sonraki tek bir sa'y hem umre hem hac için yeterlidir.", Scene3DType.MASAA, StepIllustrationKey.TAWAF, "Her iki ibadet için geçerli tek sa'y.", listOf("7 şavt Kudüm Tavafı yapın.", "Tek sa'y hem umre hem hac yerine geçer.", "Bayrama kadar saç kesmeyin."), "Allahümme barik lena fi nüsükina", "Mescid-i Haram"),
            RitualStep("q3", RitualBadgeType.RUKN, "Arafat, Mina, İfaza ve Veda", "Arafat, Müzdelife, taşlama, kurban, tıraş, İfaza ve Veda Tavafı'nı tamamlayın.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT, "Arafat'tan Veda Tavafı'na tam hac sıralaması.", listOf("Arafat Vakfesi ve Müzdelife gecesi.", "10'da taşlama, kurban, tıraş ve İfaza Tavafı.", "Teşrik günleri ve Veda Tavafı."), "Rabbena atina fid-dünya haseneh", "Arafat → Mina → Mekke")
        ),
        NusukType.MADINAH to listOf(
            RitualStep("m1", RitualBadgeType.SUNNAH, "Mescid-i Nebevi Ziyareti", "Mescid-i Haram hariç diğer mescitlerdeki bin namazdan daha faziletli namaz kılın.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Medine'deki Yeşil Kubbe, minareler ve avlu gölgelikleri.", listOf("Sağ ayakla edep ve huzur içinde girin.", "2 rekat Tahiyyetül-Mescid namazı kılın.", "Sesi alçaltıp salavat getirin."), "Allahümme salli ve sellim ala nebiyyina Muhammed", "Mescid-i Nebevi — Medine"),
            RitualStep("m2", RitualBadgeType.SUNNAH, "Ravza-i Mutahhara ve Selam", "Nusuk izniyle Ravza'da namaz kılıp Peygamber Efendimiz'e ﷺ selam verin.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Minber ile Hücre-i Saadet arasındaki mübarek Ravza alanı.", listOf("Nusuk uygulamasından Ravza randevusu alın.", "Edeple 'Es-selamü aleyke ya Resulallah' deyin.", "Hz. Ebu Bekir ve Hz. Ömer'e (r.a.) selam verin."), "Es-selamü aleyke ya Resulallah", "Ravza-i Mutahhara"),
            RitualStep("m3", RitualBadgeType.SUNNAH, "Kuba Mescidi, Baki ve Uhud", "Kuba'da 2 rekat namaz (bir umre sevabı) ve Cennetül-Baki ile Uhud ziyareti.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH, "Medine'nin faziletli ziyaret mekanları.", listOf("Otelde abdest alıp Kuba Mescidi'nde 2 rekat kılın.", "Baki kapısında dua edin.", "Uhud şehitlerini ziyaret edip dua edin."), "Es-selamü aleyküm dara kavmin mü'minin", "Kuba, Baki ve Uhud")
        )
    )

    val urRoundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "چکر 1: «سبحان الله والحمد لله ولا إله إلا الله والله أكبر» — حجرِ اسود سے آغاز۔",
            "چکر 2: «اللهم إني أسألك العفو والعافية في الدنيا والآخرة»۔",
            "چکر 3: «اللهم اغفر لي ذنوبي ووسع لي في داري وبارك لي في رزقي»۔",
            "چکر 4: «رب اغفر وارحم وتجاوز عما تعلم إنك أنت الأعز الأكرم»۔",
            "چکر 5: «اللهم أظلني تحت ظل عرشك يوم لا ظل إلا ظلك»۔",
            "چکر 6: «اللهم إني أسألك موجبات رحمتك وعزائم مغفرتك»۔",
            "چکر 7: «ربنا آتنا في الدنيا حسنة وفي الآخرة حسنة وقنا عذاب النار» پھر 2 رکعت نماز۔"
        ),
        CounterMode.SAI to listOf(
            "چکر 1 (صفا ← مروہ): «إن الصفا والمروة من شعائر الله» — مرد سبز نشانوں کے درمیان تیز چلیں۔",
            "چکر 2 (مروہ ← صفا): کثرت سے ذکر و دعا کریں۔",
            "چکر 3: «رب اغفر لي ولوالدي وللمؤمنين يوم يقوم الحساب»۔",
            "چکر 4: «اللهم حبب إلينا الإيمان وزينه في قلوبنا»۔",
            "چکر 5: «اللهم قني عذابك يوم تبعث عبادك»۔",
            "چکر 6: «اللهم آت نفسي تقواها وزكها أنت خير من زكاها»۔",
            "چکر 7 (آخری): مروہ پر دعا کر کے حلق یا قصر کروائیں۔"
        ),
        CounterMode.JAMARAT to (1..7).map { i ->
            if (i < 7) "کنکری $i: «الله أكبر»۔" else "کنکری 7: «الله أكبر» — صغریٰ اور وسطیٰ کے بعد قبلہ رخ دعا کریں۔"
        }
    )

    val idRoundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "Putaran 1: 'Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu Akbar' dari Hajar Aswad.",
            "Putaran 2: 'Allahumma inni as'alukal-'afwa wal-'afiyah fid-dunya wal-akhirah.'",
            "Putaran 3: 'Allahumma-ghfir li dhunubi wa barik li fi rizqi.'",
            "Putaran 4: 'Rabbighfir warham innaka Antal-A'azzul-Akram.'",
            "Putaran 5: 'Allahumma azillani tahta zilli 'arshika yawma la zilla illa zilluk.'",
            "Putaran 6: 'Allahumma inni as'aluka mujibati rahmatik.'",
            "Putaran 7: 'Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar' lalu shalat 2 rakaat."
        ),
        CounterMode.SAI to listOf(
            "Lintasan 1 (Shafa → Marwah): 'Innas-Safa wal-Marwata min sha'a'irillah' — pria berlari kecil di lampu hijau.",
            "Lintasan 2 (Marwah → Shafa): Perbanyak dzikir dan doa.",
            "Lintasan 3: 'Rabbighfir li wa li-walidayya wa lil-mu'minin.'",
            "Lintasan 4: 'Allahumma habbib ilaynal-imana wa zayyinhu fi qulubina.'",
            "Lintasan 5: 'Allahumma qini 'adhabaka yawma tab'athu 'ibadak.'",
            "Lintasan 6: 'Allahumma ati nafsi taqwaha wa zakkiha.'",
            "Lintasan 7 (Terakhir): Berdoa di Marwah lalu tahallul."
        ),
        CounterMode.JAMARAT to (1..7).map { i ->
            if (i < 7) "Kerikil $i: 'Allahu Akbar.'" else "Kerikil 7: 'Allahu Akbar' — berdoa menghadap kiblat setelah Ula & Wustha."
        }
    )

    val trRoundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "1. Şavt: Hacerülesved'den başlayarak 'Sübhanallahi velhamdülillahi ve la ilahe illallahu vallahu Ekber'.",
            "2. Şavt: 'Allahümme inni es'elükel-afve vel-afiyete fid-dünya vel-ahireh.'",
            "3. Şavt: 'Allahümmeğfir li zünubi ve barik li fi rızki.'",
            "4. Şavt: 'Rabbiğfir verham inneke Entel-Eazzül-Ekram.'",
            "5. Şavt: 'Allahümme ezılleni tahte zılli arşike yevme la zılle illa zıllük.'",
            "6. Şavt: 'Allahümme inni es'elüke mucibati rahmetik.'",
            "7. Şavt: 'Rabbena atina fid-dünya haseneten ve fil-ahireti haseneten ve kına azaben-nar' sonra 2 rekat namaz."
        ),
        CounterMode.SAI to listOf(
            "1. Şavt (Safa → Merve): 'İnnes-Safa vel-Mervete min şeairillah' — erkekler yeşil ışıklarda hervele yapar.",
            "2. Şavt (Merve → Safa): Zikir ve duayı artırın.",
            "3. Şavt: 'Rabbiğfir li ve li-valideyye ve lil-mü'minin.'",
            "4. Şavt: 'Allahümme habbib ileynel-imane ve zeyyinhu fi kulubina.'",
            "5. Şavt: 'Allahümme kıni azabeke yevme teb'asü ibadek.'",
            "6. Şavt: 'Allahümme ati nefsi takvaha ve zekkiha.'",
            "7. Şavt (Son): Merve'de dua edip tıraş/kısaltma ile ihramdan çıkın."
        ),
        CounterMode.JAMARAT to (1..7).map { i ->
            if (i < 7) "$i. Taş: 'Allahu Ekber.'" else "7. Taş: 'Allahu Ekber' — Küçük ve Orta Cemre'den sonra kıbleye dönüp dua edin."
        }
    )
}
