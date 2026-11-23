package com.example.data

import com.example.model.AppLanguage
import com.example.model.ChecklistTemplate
import com.example.model.CounterMode
import com.example.model.DuaEntry
import com.example.model.FaqEntry
import com.example.model.LocalizedLabels
import com.example.model.MoreTextTab
import com.example.model.NusukType
import com.example.model.PhraseEntry
import com.example.model.RitualBadgeType
import com.example.model.RitualStep
import com.example.model.Scene3DInfo
import com.example.model.Scene3DType
import com.example.model.StepIllustrationKey

object PilgrimContent {

    val i18nLabels: Map<AppLanguage, LocalizedLabels> = mapOf(
        AppLanguage.AR to LocalizedLabels(
            title = "رفيق المعتمر والحاج",
            subtitle = "نسخة ٢٠٢٦ • 3D Global",
            navHome = "الرئيسية",
            nav3D = "جولة 3D",
            navRituals = "المناسك",
            navMap = "الخريطة",
            navMore = "الأدعية ورحلتي",
            heroButton = "🕋 ابدأ وتابع مناسك الحج والعمرة"
        ),
        AppLanguage.EN to LocalizedLabels(
            title = "Pilgrim Companion",
            subtitle = "2026 Edition • 3D Global",
            navHome = "Home",
            nav3D = "3D Tour",
            navRituals = "Rituals",
            navMap = "Map & Qibla",
            navMore = "Duas & Trip",
            heroButton = "🕋 Start Hajj & Umrah Rituals"
        ),
        AppLanguage.FR to LocalizedLabels(
            title = "Compagnon Omra & Hajj",
            subtitle = "Édition 2026 • 3D",
            navHome = "Accueil",
            nav3D = "Visite 3D",
            navRituals = "Rites",
            navMap = "Carte/Qibla",
            navMore = "Douas",
            heroButton = "🕋 Guide des Rites"
        ),
        AppLanguage.UR to LocalizedLabels(
            title = "رفیقِ معتمر و حاجی",
            subtitle = "2026 ایڈیشن • 3D",
            navHome = "ہوم",
            nav3D = "3D سیر",
            navRituals = "مناسک",
            navMap = "نقشہ",
            navMore = "دعائیں",
            heroButton = "🕋 حج و عمرہ کے مناسک"
        ),
        AppLanguage.ID to LocalizedLabels(
            title = "Sahabat Umrah & Haji",
            subtitle = "Edisi 2026 • 3D",
            navHome = "Beranda",
            nav3D = "Tur 3D",
            navRituals = "Manasik",
            navMap = "Peta/Kiblat",
            navMore = "Doa",
            heroButton = "🕋 Panduan Manasik"
        ),
        AppLanguage.TR to LocalizedLabels(
            title = "Umre & Hac Rehberi",
            subtitle = "2026 Sürümü • 3D",
            navHome = "Ana Sayfa",
            nav3D = "3D Tur",
            navRituals = "İbadetler",
            navMap = "Harita",
            navMore = "Dualar",
            heroButton = "🕋 Hac ve Umre Rehberi"
        )
    )

    val scene3DInfos: Map<Scene3DType, Scene3DInfo> = mapOf(
        Scene3DType.KAABA to Scene3DInfo(
            type = Scene3DType.KAABA,
            title = "الكعبة المشرفة والمطاف (↺ عكس عقارب الساعة)",
            description = "يبدأ الطواف من الحجر الأسود (جنوب شرق) بحيث تكون الكعبة عن يسارك دائماً، 7 أشواط عكس عقارب الساعة، ثم ركعتين خلف مقام إبراهيم.",
            tags = listOf("⬛ الحجر الأسود", "↺ عكس عقارب الساعة", "🕌 مقام إبراهيم", "🌙 حجر إسماعيل")
        ),
        Scene3DType.MASAA to Scene3DInfo(
            type = Scene3DType.MASAA,
            title = "المسعى: الصفا والمروة (3D)",
            description = "7 أشواط تبدأ بالصفا وتنتهي بالمروة، مع الهرولة للرجال بين الميلين الأخضرين.",
            tags = listOf("⛰️ الصفا", "💚 الميلان الأخضران", "⛰️ المروة")
        ),
        Scene3DType.ARAFAT to Scene3DInfo(
            type = Scene3DType.ARAFAT,
            title = "مشعر عرفات وجبل الرحمة (3D)",
            description = "الوقوف بعرفة يوم 9 ذي الحجة ركن الحج الأعظم؛ يكثر الحاج من الدعاء حتى الغروب.",
            tags = listOf("⛰️ جبل الرحمة", "🕌 نمرة", "🤲 يوم 9")
        ),
        Scene3DType.JAMARAT to Scene3DInfo(
            type = Scene3DType.JAMARAT,
            title = "منى والجمرات الثلاث (3D)",
            description = "رمي العقبة الكبرى يوم العيد بـ 7 حصيات، والثلاث جمرات في التشريق بالترتيب.",
            tags = listOf("⛺ منى", "1️⃣ الصغرى", "2️⃣ الوسطى", "3️⃣ الكبرى")
        ),
        Scene3DType.MADINAH to Scene3DInfo(
            type = Scene3DType.MADINAH,
            title = "المسجد النبوي والقبة الخضراء (3D)",
            description = "زيارة المسجد النبوي بالمدينة المنورة والصلاة في الروضة الشريفة والسلام على النبي ﷺ.",
            tags = listOf("🟢 القبة الخضراء", "🕌 الروضة", "⛱️ المظلات")
        )
    )

    val roundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "الشوط 1: «سبحان الله والحمد لله ولا إله إلا الله والله أكبر» واستلام الحجر الأسود مع الرمل والاضطباع للرجال.",
            "الشوط 2: «اللهم إني أسألك العفو والعافية في الدنيا والآخرة».",
            "الشوط 3: «اللهم اغفر لي ذنوبي ووسع لي في داري وبارك لي في رزقي».",
            "الشوط 4: «رب اغفر وارحم وتجاوز عما تعلم إنك أنت الأعز الأكرم».",
            "الشوط 5: «اللهم أظلني تحت ظل عرشك يوم لا ظل إلا ظلك».",
            "الشوط 6: «اللهم إني أسألك موجبات رحمتك وعزائم مغفرتك».",
            "الشوط 7: «ربنا آتنا في الدنيا حسنة وفي الآخرة حسنة وقنا عذاب النار» ثم صلِّ ركعتين خلف المقام."
        ),
        CounterMode.SAI to listOf(
            "الشوط 1 (الصفا ← المروة): «إن الصفا والمروة من شعائر الله» والهرولة بين الميلين للرجال.",
            "الشوط 2 (المروة ← الصفا): الإكثار من الذكر والدعاء.",
            "الشوط 3: «رب اغفر لي ولوالدي وللمؤمنين يوم يقوم الحساب».",
            "الشوط 4: «اللهم حبب إلينا الإيمان وزينه في قلوبنا».",
            "الشوط 5: «اللهم قني عذابك يوم تبعث عبادك».",
            "الشوط 6: «اللهم آت نفسي تقواها وزكها أنت خير من زكاها».",
            "الشوط 7 (الأخير): اختم بالدعاء عند المروة ثم الحلق أو التقصير."
        ),
        CounterMode.JAMARAT to listOf(
            "الحصاة 1: «الله أكبر».",
            "الحصاة 2: «الله أكبر».",
            "الحصاة 3: «الله أكبر».",
            "الحصاة 4: «الله أكبر».",
            "الحصاة 5: «الله أكبر».",
            "الحصاة 6: «الله أكبر».",
            "الحصاة 7: «الله أكبر» — ادعُ مستقبل القبلة بعد الصغرى والوسطى فقط."
        )
    )

    val ritualSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep("u1", RitualBadgeType.WAJIB, "الإحرام من الميقات", "الاغتسال والتطيب ولبس الإحرام وعقد النية: لبيك اللهم عمرة.", Scene3DType.KAABA, StepIllustrationKey.IHRAM),
            RitualStep("u2", RitualBadgeType.RUKN, "طواف العمرة (7 أشواط)", "يبدأ من الحجر الأسود عكس عقارب الساعة مع الاضطباع والرمل أول 3 أشواط للرجال، وركعتان خلف المقام.", Scene3DType.KAABA, StepIllustrationKey.TAWAF),
            RitualStep("u3", RitualBadgeType.RUKN, "السعي بين الصفا والمروة", "7 أشواط تبدأ بالصفا وتنتهي بالمروة مع الهرولة بين الميلين الأخضرين للرجال.", Scene3DType.MASAA, StepIllustrationKey.SAI),
            RitualStep("u4", RitualBadgeType.WAJIB, "الحلق أو التقصير", "الحلق أفضل للرجال، وللمرأة تقصير قدر أنملة من أطراف شعرها.", Scene3DType.MASAA, StepIllustrationKey.HALQ)
        ),
        NusukType.TAMATTU to listOf(
            RitualStep("t1", RitualBadgeType.RUKN, "عمرة التمتع", "الإحرام بالعمرة، الطواف والسعي والتقصير والتحلل الكامل قبل الحج.", Scene3DType.KAABA, StepIllustrationKey.TAWAF),
            RitualStep("t2", RitualBadgeType.SUNNAH, "8 ذو الحجة (التروية)", "الإحرام بالحج من مكة والمبيت بمنى وصلاة الصلوات قصراً دون جمع.", Scene3DType.JAMARAT, StepIllustrationKey.MINA),
            RitualStep("t3", RitualBadgeType.RUKN, "9 ذو الحجة (عرفة)", "ركن الحج الأعظم: صلاة الظهر والعصر جمع تقديم وقصراً والدعاء حتى الغروب.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT),
            RitualStep("t4", RitualBadgeType.WAJIB, "ليلة 10 (مزدلفة)", "صلاة المغرب والعشاء جمع تأخير، المبيت حتى الفجر، وجمع 7 حصيات.", Scene3DType.JAMARAT, StepIllustrationKey.MUZDALIFAH),
            RitualStep("t5", RitualBadgeType.WAJIB, "10 ذو الحجة (يوم النحر)", "رمي العقبة الكبرى 7 حصيات، ذبح الهدي، ثم الحلق أو التقصير (التحلل الأول).", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT),
            RitualStep("t6", RitualBadgeType.RUKN, "طواف الإفاضة وسعي الحج", "النزول لمكة لطواف الإفاضة 7 أشواط وسعي الحج (التحلل الأكبر).", Scene3DType.KAABA, StepIllustrationKey.TAWAF),
            RitualStep("t7", RitualBadgeType.WAJIB, "11-13 ذو الحجة (التشريق)", "المبيت بمنى ورمي الجمرات الثلاث بعد الزوال (صغرى ثم وسطى ثم كبرى).", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT),
            RitualStep("t8", RitualBadgeType.WAJIB, "طواف الوداع", "آخر عهد بالبيت 7 أشواط قبل السفر (يسقط عن الحائض).", Scene3DType.KAABA, StepIllustrationKey.TAWAF)
        ),
        NusukType.IFRAD to listOf(
            RitualStep("i1", RitualBadgeType.RUKN, "الإحرام بالحج مفرداً", "قول: لبيك اللهم حجاً والبقاء على الإحرام ليوم النحر (لا هدي عليه).", Scene3DType.KAABA, StepIllustrationKey.IHRAM),
            RitualStep("i2", RitualBadgeType.SUNNAH, "طواف القدوم وسعي الحج", "يطوف للقدوم وله تقديم سعي الحج بعده.", Scene3DType.MASAA, StepIllustrationKey.SAI),
            RitualStep("i3", RitualBadgeType.RUKN, "منى وعرفة ومزدلفة", "أداء مناسك التروية والوقوف بعرفة والمبيت بمزدلفة.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT),
            RitualStep("i4", RitualBadgeType.RUKN, "يوم العيد والتشريق والوداع", "رمي العقبة، الحلق، طواف الإفاضة، الرمي بمنى ثم الوداع.", Scene3DType.JAMARAT, StepIllustrationKey.JAMARAT)
        ),
        NusukType.QIRAN to listOf(
            RitualStep("q1", RitualBadgeType.RUKN, "الإحرام بهما معاً", "قول: لبيك اللهم عمرة وحجاً وعليه هدي كالمتمتع.", Scene3DType.KAABA, StepIllustrationKey.IHRAM),
            RitualStep("q2", RitualBadgeType.SUNNAH, "طواف القدوم وسعي واحد", "يطوف للقدوم ويكفيه سعي واحد للعمرة والحج.", Scene3DType.MASAA, StepIllustrationKey.TAWAF),
            RitualStep("q3", RitualBadgeType.RUKN, "عرفة والمشاعر والوداع", "إتمام عرفة ومزدلفة والنحر والإفاضة والتشريق والوداع.", Scene3DType.ARAFAT, StepIllustrationKey.ARAFAT)
        ),
        NusukType.MADINAH to listOf(
            RitualStep("m1", RitualBadgeType.SUNNAH, "زيارة المسجد النبوي", "الصلاة فيه خير من ألف صلاة فيما سواه إلا المسجد الحرام.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH),
            RitualStep("m2", RitualBadgeType.SUNNAH, "الصلاة بالروضة الشريفة", "حجز تصريح نسك مسبقاً والسلام على النبي ﷺ وصاحبيه.", Scene3DType.MADINAH, StepIllustrationKey.MADINAH),
            RitualStep("m3", RitualBadgeType.SUNNAH, "مسجد قباء والبقيع", "التطهر بالسكن وصلاة ركعتين في قباء (تعدل عمرة).", Scene3DType.MADINAH, StepIllustrationKey.MADINAH)
        )
    )

    val faqList: List<FaqEntry> = listOf(
        FaqEntry("شككت في عدد الأشواط، ماذا أفعل؟", "يبني على اليقين وهو العدد الأقل ويكمل الباقي."),
        FaqEntry("انتقض وضوئي أثناء الطواف؟", "يقطع الطواف ويتوضأ ثم يستأنف (أو يبني إن لم يطل الفاصل)."),
        FaqEntry("هل يشترط الوضوء للسعي؟", "يُستحب الوضوء للسعي ولكنه ليس شرطاً لصحته."),
        FaqEntry("نسيت التقصير ولبست ثيابي؟", "تخلع المخيط فوراً وتقصّر ولا فدية عليك لعدم التعمد."),
        FaqEntry("القصر والجمع في المشاعر؟", "في منى وعرفة ومزدلفة تُقصر الرباعية، وبالحرم المكي تُصلى تامة خلف الإمام.")
    )

    val phrases: List<PhraseEntry> = listOf(
        PhraseEntry("أين أقرب دورة مياه؟", "Where is the nearest restroom?"),
        PhraseEntry("أريد كرسياً متحركاً", "I need a wheelchair"),
        PhraseEntry("أين بوابة الملك عبدالعزيز؟", "Where is King Abdulaziz Gate?"),
        PhraseEntry("أحتاج لمساعدة طبية عاجلة", "I need urgent medical help")
    )

    val moreData: Map<MoreTextTab, List<DuaEntry>> = mapOf(
        MoreTextTab.DUAS to listOf(
            DuaEntry("التلبية", "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لا شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لا شَرِيكَ لَكَ"),
            DuaEntry("دعاء عرفة", "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد وهو على كل شيء قدير"),
            DuaEntry("بين الركن والحجر", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ"),
            DuaEntry("شرب زمزم", "اللهم إني أسألك علماً نافعاً، ورزقاً واسعاً، وشفاءً من كل داء")
        ),
        MoreTextTab.QURAN to listOf(
            DuaEntry("سورة الفاتحة", "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝ الرَّحْمَٰنِ الرَّحِيمِ ۝ مَالِكِ يَوْمِ الدِّينِ ۝ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"),
            DuaEntry("آية الكرسي", "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ"),
            DuaEntry("سورة الإخلاص", "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ")
        ),
        MoreTextTab.ADHKAR to listOf(
            DuaEntry("سيد الاستغفار", "اللَّهُمَّ أَنْتَ رَبِّي لا إِلَهَ إِلا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لا يَغْفِرُ الذُّنُوبَ إِلا أَنْتَ"),
            DuaEntry("حفظ الصباح والمساء", "بِسْمِ اللهِ الَّذِي لا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلا فِي السَّمَاءِ وَهُوَ السَّمِيعُ العَلِيمُ (ثلاثاً)")
        )
    )

    val checklistItems: List<ChecklistTemplate> = listOf(
        ChecklistTemplate("p1", "تصريح نسك وجواز السفر", true),
        ChecklistTemplate("p2", "ملابس الإحرام وحزام الخصر", true),
        ChecklistTemplate("p3", "مظلة شمسية وبخاخ ماء", false),
        ChecklistTemplate("p4", "شاحن متنقل وسماعة أذن", false),
        ChecklistTemplate("p5", "حذاء طبي مريح وحقيبة حصى", false)
    )

    val fidyahRules: Map<String, Pair<String, String>> = mapOf(
        "tamattu" to ("هدي التمتع والقران" to "ذبح شاة في الحرم. ومن لم يجد: صيام 3 أيام في الحج و7 إذا رجع لأهله."),
        "wajib" to ("ترك واجب من واجبات النسك" to "يجب فيه دم (ذبح شاة في مكة وتوزيعها على فقراء الحرم)."),
        "adha" to ("فعل محظور (فدية الأذى)" to "يُخيّر بين صيام 3 أيام، أو إطعام 6 مساكين، أو ذبح شاة."),
        "niyabah" to ("الحج أو العمرة بالنيابة" to "يشترط أن يكون النائب قد حج/اعتمر عن نفسه أولاً، ويقول عند الميقات: «لبيك اللهم عمرة/حجاً عن (فلان)».")
    )

    data class MiqatPoint(val name: String, val lat: Double, val lng: Double)

    val miqats = listOf(
        MiqatPoint("ذو الحليفة (أبيار علي) - المدينة", 24.4139, 39.5425),
        MiqatPoint("الجحفة (رابغ)", 22.7072, 39.1464),
        MiqatPoint("قرن المنازل (السيل الكبير)", 21.6317, 40.4264),
        MiqatPoint("يلملم (السعدية)", 20.5175, 39.8669),
        MiqatPoint("ذات عرق", 21.9250, 40.4250)
    )

    const val TALBIYAH_TEXT = "لبيك اللهم لبيك، لبيك لا شريك لك لبيك، إن الحمد والنعمة لك والملك، لا شريك لك"
    const val TAKBEERAT_TEXT = "الله أكبر الله أكبر الله أكبر، لا إله إلا الله، والله أكبر الله أكبر ولله الحمد"
}
