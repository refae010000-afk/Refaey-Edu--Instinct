package com.example.data

import com.example.model.AppLanguage
import com.example.model.CounterMode
import com.example.model.NusukType
import com.example.model.RitualBadgeType
import com.example.model.RitualStep
import com.example.model.Scene3DType
import com.example.model.StepIllustrationKey
import com.example.model.TawafFloor

object LocalizedRitualGuideData {

    fun ritualStepsFor(lang: AppLanguage, nusuk: NusukType): List<RitualStep> = when (lang) {
        AppLanguage.AR -> arSteps[nusuk].orEmpty()
        AppLanguage.EN -> enSteps[nusuk].orEmpty()
        AppLanguage.FR -> frSteps[nusuk].orEmpty()
        AppLanguage.UR -> LocalizedRitualGuideDataExtra.urSteps[nusuk].orEmpty()
        AppLanguage.ID -> LocalizedRitualGuideDataExtra.idSteps[nusuk].orEmpty()
        AppLanguage.TR -> LocalizedRitualGuideDataExtra.trSteps[nusuk].orEmpty()
    }

    fun roundDuasFor(lang: AppLanguage, mode: CounterMode): List<String> = when (lang) {
        AppLanguage.AR -> PilgrimContent.roundDuas[mode].orEmpty()
        AppLanguage.EN -> enRoundDuas[mode].orEmpty()
        AppLanguage.FR -> frRoundDuas[mode].orEmpty()
        AppLanguage.UR -> LocalizedRitualGuideDataExtra.urRoundDuas[mode].orEmpty()
        AppLanguage.ID -> LocalizedRitualGuideDataExtra.idRoundDuas[mode].orEmpty()
        AppLanguage.TR -> LocalizedRitualGuideDataExtra.trRoundDuas[mode].orEmpty()
    }

    fun jamaratNamesFor(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.AR -> listOf("1. الصغرى", "2. الوسطى", "3. الكبرى")
        AppLanguage.EN -> listOf("1. Sughra", "2. Wusta", "3. Kubra")
        AppLanguage.FR -> listOf("1. Sughra", "2. Wusta", "3. Kubra")
        AppLanguage.UR -> listOf("1. جمرہ صغریٰ", "2. جمرہ وسطیٰ", "3. جمرہ کبریٰ")
        AppLanguage.ID -> listOf("1. Ula", "2. Wustha", "3. Aqabah")
        AppLanguage.TR -> listOf("1. Küçük", "2. Orta", "3. Büyük")
    }

    fun tawafFloorLabelFor(lang: AppLanguage, floor: TawafFloor): String = when (lang) {
        AppLanguage.AR -> floor.label
        AppLanguage.EN -> when (floor) {
            TawafFloor.SAHN -> "Mataf Courtyard (~20 min)"
            TawafFloor.GROUND -> "Ground Floor (~35 min)"
            TawafFloor.FIRST -> "First Floor (~45 min)"
            TawafFloor.ROOF -> "Roof Level (~55 min - Spacious)"
        }
        AppLanguage.FR -> when (floor) {
            TawafFloor.SAHN -> "Parvis du Mataf (~20 min)"
            TawafFloor.GROUND -> "Rez-de-chaussée (~35 min)"
            TawafFloor.FIRST -> "1er Étage (~45 min)"
            TawafFloor.ROOF -> "Toit (~55 min - Spacieux)"
        }
        AppLanguage.UR -> when (floor) {
            TawafFloor.SAHN -> "صحن مطاف (~20 منٹ)"
            TawafFloor.GROUND -> "گراؤنڈ فلور (~35 منٹ)"
            TawafFloor.FIRST -> "پہلی منزل (~45 منٹ)"
            TawafFloor.ROOF -> "چھت (~55 منٹ - کشادہ)"
        }
        AppLanguage.ID -> when (floor) {
            TawafFloor.SAHN -> "Pelataran Mataf (~20 mnt)"
            TawafFloor.GROUND -> "Lantai Dasar (~35 mnt)"
            TawafFloor.FIRST -> "Lantai 1 (~45 mnt)"
            TawafFloor.ROOF -> "Atap Masjid (~55 mnt - Luas)"
        }
        AppLanguage.TR -> when (floor) {
            TawafFloor.SAHN -> "Kabe Avlusu (~20 dk)"
            TawafFloor.GROUND -> "Zemin Kat (~35 dk)"
            TawafFloor.FIRST -> "1. Kat (~45 dk)"
            TawafFloor.ROOF -> "Çatı Katı (~55 dk - Ferah)"
        }
    }

    fun illustrationLabelsFor(lang: AppLanguage, key: StepIllustrationKey): List<String> = when (lang) {
        AppLanguage.AR -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("لبيك اللهم", "الإحرام من الميقات")
            StepIllustrationKey.TAWAF -> listOf("عكس العقارب ↺", "الحجر الأسود ↘")
            StepIllustrationKey.SAI -> listOf("الصفا", "الميلان الأخضران", "المروة")
            StepIllustrationKey.HALQ -> listOf("التحلل والحلق/التقصير", "تعميم الرأس أو قدر أنملة للنساء")
            StepIllustrationKey.MINA -> listOf("خيام مشعر منى — يوم التروية والتشريق")
            StepIllustrationKey.ARAFAT -> listOf("جبل الرحمة بعرفات • ركن الحج الأعظم")
            StepIllustrationKey.MUZDALIFAH -> listOf("المبيت بمزدلفة وجمع الحصى")
            StepIllustrationKey.JAMARAT -> listOf("الصغرى ← الوسطى ← جمرة العقبة الكبرى")
            StepIllustrationKey.MADINAH -> listOf("المسجد النبوي والقبة الخضراء والروضة")
        }
        AppLanguage.EN -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("Labbayk Allahumma", "Ihram at Miqat")
            StepIllustrationKey.TAWAF -> listOf("Counter-Clockwise ↺", "Black Stone ↘")
            StepIllustrationKey.SAI -> listOf("Safa", "Green Markers", "Marwah")
            StepIllustrationKey.HALQ -> listOf("Shaving or Trimming Hair", "Exiting Ihram (Tahallul)")
            StepIllustrationKey.MINA -> listOf("Mina Tent City — Day 8 & Tashreeq")
            StepIllustrationKey.ARAFAT -> listOf("Mount of Mercy • Pillar of Hajj")
            StepIllustrationKey.MUZDALIFAH -> listOf("Night at Muzdalifah & Pebbles")
            StepIllustrationKey.JAMARAT -> listOf("Sughra → Wusta → Kubra Pillar")
            StepIllustrationKey.MADINAH -> listOf("Prophet's Mosque & Green Dome")
        }
        AppLanguage.FR -> when (key) {
            StepIllustrationKey.IHRAM -> listOf("Labbayk Allahumma", "Ihram au Miqat")
            StepIllustrationKey.TAWAF -> listOf("Sens Antihoraire ↺", "Pierre Noire ↘")
            StepIllustrationKey.SAI -> listOf("Safa", "Repères Verts", "Marwah")
            StepIllustrationKey.HALQ -> listOf("Rasage ou Coupe", "Désacralisation (Tahallul)")
            StepIllustrationKey.MINA -> listOf("Tentes de Mina — 8e Jour & Tachriq")
            StepIllustrationKey.ARAFAT -> listOf("Mont Arafat • Pilier du Hajj")
            StepIllustrationKey.MUZDALIFAH -> listOf("Nuit à Muzdalifah & Cailloux")
            StepIllustrationKey.JAMARAT -> listOf("Sughra → Wusta → Grande Jamrah")
            StepIllustrationKey.MADINAH -> listOf("Mosquée du Prophète & Dôme Vert")
        }
        AppLanguage.UR, AppLanguage.ID, AppLanguage.TR ->
            LocalizedRitualGuideDataExtra.illustrationLabelsFor(lang, key)
    }

    private val arSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep(
                id = "u1",
                badgeType = RitualBadgeType.WAJIB,
                title = "الإحرام من الميقات",
                description = "الاغتسال والتطيب في البدن ولبس ملابس الإحرام وعقد النية عند الميقات.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "رسم توضيحي لرداء الإحرام الأبيض غير المخيط للرجال (إزار ورداء) مع استقبال القبلة وعقد النية.",
                detailedPoints = listOf(
                    "الاغتسال والتطيب في البدن قبل عقد النية، وتقليم الأظافر.",
                    "يلبس الرجل إزاراً ورداءً أبيضين، وتلبس المرأة ثيابها الساترة دون نقاب أو قفازين.",
                    "عقد النية عند محاذاة الميقات بقول: «لبيك اللهم عمرة» ثم الإكثار من التلبية."
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ عُمْرَةً • لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لا شَرِيكَ لَكَ لَبَّيْكَ",
                locationHint = "الميقات المكاني أو محاذاته"
            ),
            RitualStep(
                id = "u2",
                badgeType = RitualBadgeType.RUKN,
                title = "طواف العمرة (7 أشواط)",
                description = "الدوران حول الكعبة المشرفة 7 أشواط عكس عقارب الساعة بدءاً من الحجر الأسود.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "مسار الطواف حول الكعبة عكس عقارب الساعة (الكعبة عن يسارك)، يبدأ وينتهي عند خط الحجر الأسود.",
                detailedPoints = listOf(
                    "الاضطباع للرجل (كشف الكتف الأيمن) والرمل (الإسراع) في الأشواط الثلاثة الأولى فقط.",
                    "البدء من محاذاة الحجر الأسود بالإشارة باليد اليمنى والتكبير، والحرص على عدم الطواف داخل حجر إسماعيل.",
                    "صلاة ركعتين خلف مقام إبراهيم بعد إتمام الشوط السابع وشرب ماء زمزم."
                ),
                stageDua = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                locationHint = "المسجد الحرام — صحن المطاف"
            ),
            RitualStep(
                id = "u3",
                badgeType = RitualBadgeType.RUKN,
                title = "السعي بين الصفا والمروة",
                description = "المشي 7 أشواط تبدأ من جبل الصفا وتنتهي بجبل المروة.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "ممر المسعى بين جبل الصفا والمروة؛ تُبرز العلامات الخضراء منطقة الهرولة (الجري الخفيف للرجال).",
                detailedPoints = listOf(
                    "الصعود على الصفا واستقبال الكعبة والدعاء: «إن الصفا والمروة من شعائر الله».",
                    "الذهاب من الصفا إلى المروة يُحسب شوطاً، والعودة من المروة إلى الصفا شوطاً ثانياً.",
                    "الهرولة للرجال بين الميلين الأخضرين والدعاء والذكر طوال المسير."
                ),
                stageDua = "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ • اللَّهُ أَكْبَرُ لا إِلَهَ إِلا اللَّهُ وَحْدَهُ",
                locationHint = "المسعى — الصفا والمروة"
            ),
            RitualStep(
                id = "u4",
                badgeType = RitualBadgeType.WAJIB,
                title = "الحلق أو التقصير (التحلل)",
                description = "إتمام العمرة بحلق شعر الرأس أو تقصيره للرجال، وتقصير قدر أنملة للنساء.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.HALQ,
                visualCaption = "مرحلة التحلل الكامل: تعميم الرأس بالحلق أو التقصير بعد نهاية الشوط السابع عند المروة.",
                detailedPoints = listOf(
                    "الحلق بالموسى أفضل للرجل، أو التقصير بتعميم جميع جوانب الرأس.",
                    "تقص المرأة قدر أنملة (نحو 2 سم) من أطراف ضفائرها أو شعرها.",
                    "بهذا يتحلل المعتمر تحللاً كاملاً وتُباح له جميع محظورات الإحرام."
                ),
                stageDua = "اللَّهُمَّ اغْفِرْ لِلْمُحَلِّقِينَ وَالْمُقَصِّرِينَ وَتَقَبَّلْ مِنَّا",
                locationHint = "مخرج المروة — محلات الحلاقة المعتمدة"
            )
        ),
        NusukType.TAMATTU to listOf(
            RitualStep(
                id = "t1",
                badgeType = RitualBadgeType.RUKN,
                title = "عمرة التمتع والتحلل",
                description = "أداء عمرة كاملة في أشهر الحج ثم التحلل حتى يوم الثامن من ذي الحجة.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "طواف 7 أشواط حول الكعبة ثم السعي والتقصير والتحلل الكامل قبل بدء أعمال الحج.",
                detailedPoints = listOf(
                    "الإحرام بالعمرة من الميقات: «لبيك اللهم عمرة متمتعاً بها إلى الحج».",
                    "أداء الطواف والسعي والحلق أو التقصير، ثم خلع ملابس الإحرام.",
                    "تجب على المتمتع شاة الهدي يوم العيد، ومن لم يجد يصوم 3 أيام في الحج و7 إذا رجع."
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ عُمْرَةً مُتَمَتِّعاً بِهَا إِلَى الْحَجِّ",
                locationHint = "الميقات ← المسجد الحرام"
            ),
            RitualStep(
                id = "t2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "8 ذو الحجة — يوم التروية بمنى",
                description = "الإحرام بالحج من السكن بمكة والتوجه إلى مشعر منى والمبيت فيها.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MINA,
                visualCaption = "مدينة الخيام البيضاء في مشعر منى حيث يؤدي الحاج الصلوات قصراً دون جمع.",
                detailedPoints = listOf(
                    "الاغتسال ولبس الإحرام في مكة صباح 8 ذي الحجة بقول: «لبيك اللهم حجاً».",
                    "التوجه إلى منى وصلاة الظهر والعصر والمغرب والعشاء وفجر 9 ذي الحجة قصراً في أوقاتها.",
                    "الإكثار من التلبية والذكر والاستعداد ليوم عرفة."
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ حَجًّا • اللَّهُمَّ بَلِّغْنَا عَرَفَةَ وَبَارِكْ لَنَا",
                locationHint = "مكة ← مشعر منى"
            ),
            RitualStep(
                id = "t3",
                badgeType = RitualBadgeType.RUKN,
                title = "9 ذو الحجة — الوقوف بعرفة",
                description = "ركن الحج الأعظم؛ التواجد بحدود عرفة من الزوال حتى غروب الشمس.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "جبل الرحمة وسهل عرفات؛ يتحقق الوقوف بالبقاء داخل الحدود الشرعية لعرفات حتى الغروب.",
                detailedPoints = listOf(
                    "التحقق من البقاء داخل حدود عرفة (مقدمة مسجد نمرة خارج حدود عرفة).",
                    "صلاة الظهر والعصر جمع تقديم وقصراً بأذان واحد وإقامتين.",
                    "التفرغ للدعاء والذكر واستقبال القبلة بخشوع حتى غروب الشمس."
                ),
                stageDua = "لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
                locationHint = "مشعر عرفات"
            ),
            RitualStep(
                id = "t4",
                badgeType = RitualBadgeType.WAJIB,
                title = "ليلة 10 ذي الحجة — المبيت بمزدلفة",
                description = "الإفاضة بعد الغروب إلى مزدلفة وصلاة المغرب والعشاء جمعاً والمبيت فيها.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MUZDALIFAH,
                visualCaption = "توقف الحجاج في سهل مزدلفة تحت السماء المفتوحة لصلاة العشاءين وجمع الحصى.",
                detailedPoints = listOf(
                    "تأخير صلاة المغرب والعشاء وأداؤهما جمع تأخير وقصراً فور الوصول لمزدلفة.",
                    "المبيت حتى الفجر (ويرخص للنساء والضعفة الدفع بعد منتصف الليل).",
                    "جمع 7 حصيات بحجم حبة الحمص لرمي جمرة العقبة، وصلاة الفجر ثم الدعاء عند المشعر الحرام."
                ),
                stageDua = "اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا وَسَعْيًا مَشْكُورًا",
                locationHint = "مشعر مزدلفة"
            ),
            RitualStep(
                id = "t5",
                badgeType = RitualBadgeType.WAJIB,
                title = "10 ذو الحجة — يوم النحر والرمي",
                description = "أعمال يوم العيد بالترتيب: رمي العقبة الكبرى، الهدي، ثم الحلق أو التقصير.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "جسر الجمرات متعدد الطوابق؛ يرمي الحاج جمرة العقبة الكبرى فقط بـ 7 حصيات يوم العيد.",
                detailedPoints = listOf(
                    "التكبير مع كل حصاة من الحصيات السبع عند رمي جمرة العقبة الكبرى (وقطع التلبية).",
                    "نحر الهدي (أو التوكيل عبر مشروع أضاحي الرسمي).",
                    "الحلق أو التقصير لتحقق التحلل الأول (يحل كل شيء ما عدا النساء)."
                ),
                stageDua = "اللَّهُ أَكْبَرُ (مع كل حصاة) • بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ",
                locationHint = "جسر الجمرات — منى"
            ),
            RitualStep(
                id = "t6",
                badgeType = RitualBadgeType.RUKN,
                title = "طواف الإفاضة وسعي الحج",
                description = "ركن من أركان الحج يتم به التحلل الأكبر بعد النزول إلى مكة.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "7 أشواط حول الكعبة (طواف الزيارة) يليها 7 أشواط في المسعى بين الصفا والمروة.",
                detailedPoints = listOf(
                    "الطواف 7 أشواط حول الكعبة المشرفة بنية طواف الإفاضة وركعتان خلف المقام.",
                    "السعي 7 أشواط بين الصفا والمروة لحج التمتع.",
                    "بعد طواف الإفاضة والسعي والرمي والحلق يكتمل التحلل الأكبر."
                ),
                stageDua = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                locationHint = "المسجد الحرام — مكة"
            ),
            RitualStep(
                id = "t7",
                badgeType = RitualBadgeType.WAJIB,
                title = "11-13 ذو الحجة — أيام التشريق",
                description = "المبيت بمنى ورمي الجمرات الثلاث مرتبة بعد الزوال كل يوم.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "تسلسل رمي الجمرات الثلاث: الصغرى ← الوسطى ← الكبرى (7 حصيات لكل جمرة).",
                detailedPoints = listOf(
                    "البدء بالصغرى (7 حصيات) ثم التقدم والدعاء طويلاً مستقبل القبلة.",
                    "رمي الوسطى (7 حصيات) ثم التقدم يساراً والدعاء مستقبل القبلة.",
                    "رمي الكبرى (7 حصيات) والانصراف مباشرة دون وقوف، ويجوز التعجل في اليوم الـ 12 قبل الغروب."
                ),
                stageDua = "اللَّهُ أَكْبَرُ، اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا وَذَنْبًا مَغْفُورًا",
                locationHint = "مشعر منى وجسر الجمرات"
            ),
            RitualStep(
                id = "t8",
                badgeType = RitualBadgeType.WAJIB,
                title = "طواف الوداع",
                description = "آخر عهد الحاج بالبيت الحرام 7 أشواط قبل مغادرة مكة مباشرة.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "سبعة أشواط ختامية حول الكعبة قبل السفر (يسقط عن الحائض والنفساء تخفيفاً).",
                detailedPoints = listOf(
                    "جعل الطواف آخر عمل قبل ركوب السفر والخروج من مكة.",
                    "طواف 7 أشواط دون رمل أو اضطباع ثم صلاة ركعتين خلف المقام.",
                    "يُعفى منه النساء في فترة الحيض أو النفاس ويجوز لهن السفر."
                ),
                stageDua = "اللَّهُمَّ تَقَبَّلْ مِنَّا وَارْزُقْنَا الْعَوْدَةَ إِلَى بَيْتِكَ الْحَرَامِ",
                locationHint = "المسجد الحرام — مكة"
            )
        ),
        NusukType.IFRAD to listOf(
            RitualStep(
                id = "i1",
                badgeType = RitualBadgeType.RUKN,
                title = "الإحرام بالحج مفرداً",
                description = "نية الحج فقط من الميقات والبقاء على الإحرام حتى يوم النحر (دون هدي).",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "إحرام المفرد بالحج فقط مع الاستمرار في التلبية والالتزام بمحظورات الإحرام.",
                detailedPoints = listOf(
                    "عقد النية عند الميقات بقول: «لبيك اللهم حجاً».",
                    "البقاء محرماً عند دخول مكة وعدم التحلل بعد طواف القدوم.",
                    "لا يجب على المفرد هدي تمتع."
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ حَجًّا، لا رِيَاءَ فِيهِ وَلا سُمْعَةَ",
                locationHint = "الميقات المكاني"
            ),
            RitualStep(
                id = "i2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "طواف القدوم وتقديم سعي الحج",
                description = "تحية البيت بطواف القدوم ويمكن تقديم سعي الحج بعده مباشرة.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "طواف القدوم حول الكعبة يليه السعي بين الصفا والمروة مع بقاء الإحرام.",
                detailedPoints = listOf(
                    "طواف 7 أشواط للقدوم مع الاضطباع والرمل أول 3 أشواط للرجال.",
                    "السعي 7 أشواط بين الصفا والمروة إن أراد تقديم سعي الحج.",
                    "يبقى على إحرامه ولا يحلق أو يقصّر."
                ),
                stageDua = "سُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ وَلا إِلَهَ إِلا اللَّهُ وَاللَّهُ أَكْبَرُ",
                locationHint = "المطاف والمسعى — مكة"
            ),
            RitualStep(
                id = "i3",
                badgeType = RitualBadgeType.RUKN,
                title = "منى وعرفة ومزدلفة",
                description = "التروية بمنى في 8 ذي الحجة، ثم الوقوف بعرفة في 9 والمبيت بمزدلفة.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "مسار المشاعر: خيام منى ← جبل الرحمة بعرفة ← مزدلفة لجمع الحصى.",
                detailedPoints = listOf(
                    "المبيت بمنى ليلة عرفة وأداء الصلوات قصراً.",
                    "الوقوف بعرفة حتى غروب شمس اليوم التاسع مع الإكثار من الدعاء.",
                    "المبيت بمزدلفة وجمع الحصيات ليوم العيد."
                ),
                stageDua = "لا إِلَهَ إِلا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ",
                locationHint = "منى ← عرفات ← مزدلفة"
            ),
            RitualStep(
                id = "i4",
                badgeType = RitualBadgeType.RUKN,
                title = "يوم النحر والتشريق والوداع",
                description = "رمي العقبة، الحلق، طواف الإفاضة، أيام التشريق، ثم طواف الوداع.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "رمي الجمرات والتحلل ثم إتمام طواف الإفاضة والوداع.",
                detailedPoints = listOf(
                    "رمي جمرة العقبة الكبرى 7 حصيات ثم الحلق أو التقصير للتحلل الأول.",
                    "طواف الإفاضة بمكة (دون تكرار السعي إذا قدّمه بعد القدوم).",
                    "رمي الجمرات الثلاث أيام التشريق ثم طواف الوداع قبل المغادرة."
                ),
                stageDua = "اللَّهُ أَكْبَرُ، رَبَّنَا تَقَبَّلْ مِنَّا إِنَّكَ أَنْتَ السَّمِيعُ الْعَلِيمُ",
                locationHint = "الجمرات ← المسجد الحرام"
            )
        ),
        NusukType.QIRAN to listOf(
            RitualStep(
                id = "q1",
                badgeType = RitualBadgeType.RUKN,
                title = "الإحرام بالعمرة والحج معاً",
                description = "جمع العمرة والحج في نية وإحرام واحد من الميقات مع وجوب الهدي.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "نية القِران تجمع العمرة والحج معاً في إحرام واحد مستمر حتى يوم العيد.",
                detailedPoints = listOf(
                    "القول عند الميقات: «لبيك اللهم عمرة وحجاً».",
                    "الاستمرار على الإحرام بعد دخول مكة حتى يوم النحر.",
                    "يجب على القارن الهدي كالمتمتع يوم العاشر."
                ),
                stageDua = "لَبَّيْكَ اللَّهُمَّ عُمْرَةً وَحَجًّا",
                locationHint = "الميقات المكاني"
            ),
            RitualStep(
                id = "q2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "طواف القدوم وسعي واحد",
                description = "يطوف القارن للقدوم ويكفيه سعي واحد يجزئ عن العمرة والحج معاً.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "طواف القدوم 7 أشواط مع إمكانية تقديم السعي الواحد للعمرة والحج.",
                detailedPoints = listOf(
                    "الطواف حول الكعبة 7 أشواط بنية القدوم.",
                    "السعي بين الصفا والمروة مرة واحدة تكفي للحج والعمرة.",
                    "عدم قص الشعر أو التحلل حتى يوم النحر."
                ),
                stageDua = "اللَّهُمَّ بَارِكْ لَنَا فِي نُسُكِنَا وَأَعِنَّا عَلَى ذِكْرِكَ",
                locationHint = "المسجد الحرام"
            ),
            RitualStep(
                id = "q3",
                badgeType = RitualBadgeType.RUKN,
                title = "المشاعر والنحر والإفاضة والوداع",
                description = "أداء التروية وعرفة ومزدلفة، والرمي والهدي والحلق وطواف الإفاضة والوداع.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "تتسلسل مناسك القارن في عرفة ومزدلفة ومنى مطابقةً لأعمال الحج.",
                detailedPoints = listOf(
                    "الوقوف بعرفة والمبيت بمزدلفة في اليوم التاسع وليلة العاشر.",
                    "يوم العيد: رمي العقبة الكبرى، ذبح الهدي، والحلق أو التقصير، ثم طواف الإفاضة.",
                    "المبيت ورمي الجمرات أيام التشريق، ثم طواف الوداع عند السفر."
                ),
                stageDua = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                locationHint = "عرفة ← مزدلفة ← منى ← مكة"
            )
        ),
        NusukType.MADINAH to listOf(
            RitualStep(
                id = "m1",
                badgeType = RitualBadgeType.SUNNAH,
                title = "زيارة المسجد النبوي الشريف",
                description = "الصلاة في المسجد النبوي خير من ألف صلاة فيما سواه إلا المسجد الحرام.",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "القبة الخضراء والمآذن ومظلات الساحات في المسجد النبوي بالمدينة المنورة.",
                detailedPoints = listOf(
                    "الدخول بالقدم اليمنى وسكينة ووقار: «اللهم افتح لي أبواب رحمتك».",
                    "تحية المسجد ركعتان والحرص على الصلوات الخمس جماعة.",
                    "التحلي بالآداب وخفض الصوت تعظيماً لمكانة المسجد النبوي."
                ),
                stageDua = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ • اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
                locationHint = "المسجد النبوي — المدينة المنورة"
            ),
            RitualStep(
                id = "m2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "الروضة الشريفة والسلام على النبي ﷺ",
                description = "الصلاة في الروضة الشريفة والسلام على النبي ﷺ وصاحبيه رضي الله عنهما.",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "الروضة الشريفة (ما بين المنبر والبيت النبوي) وحجز التصريح المسبق عبر نسك.",
                detailedPoints = listOf(
                    "حجز موعد زيارة الروضة الشريفة مسبقاً عبر تطبيق نسك.",
                    "الوقوف بأدب أمام القبر الشريف والسلام: «السلام عليك يا رسول الله».",
                    "السلام على أبي بكر الصديق وعمر بن الخطاب رضي الله عنهما."
                ),
                stageDua = "السَّلامُ عَلَيْكَ يَا رَسُولَ اللَّهِ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ",
                locationHint = "الروضة الشريفة والمواجهة الشريفة"
            ),
            RitualStep(
                id = "m3",
                badgeType = RitualBadgeType.SUNNAH,
                title = "مسجد قباء والبقيع وجبل أحد",
                description = "زيارة مسجد قباء والصلاة فيه (تعدل أجر عمرة) وزيارة البقيع وشهداء أحد.",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "أبرز المعالم النبوية بالمدينة المنورة وزيارة السلام والدعاء لأهل البقيع.",
                detailedPoints = listOf(
                    "التطهر في السكن والتوجه لمسجد قباء لصلاة ركعتين فيه.",
                    "الوقوف بباب البقيع والدعاء لأهل المقبرة: «السلام عليكم دار قوم مؤمنين».",
                    "زيارة شهداء أحد والدعاء لهم اتباعاً للسنة النبوية."
                ),
                stageDua = "السَّلامُ عَلَيْكُمْ دَارَ قَوْمٍ مُؤْمِنِينَ وَإِنَّا إِنْ شَاءَ اللَّهُ بِكُمْ لاحِقُونَ",
                locationHint = "قباء والبقيع وأحد — المدينة"
            )
        )
    )

    private val enSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep(
                id = "u1",
                badgeType = RitualBadgeType.WAJIB,
                title = "Ihram from the Miqat",
                description = "Purify yourself (ghusl), wear the Ihram garments, and make the Umrah intention at the Miqat.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Diagram of the two unsewn white Ihram cloths (Izar & Rida) for men and modest dress for women at the Miqat.",
                detailedPoints = listOf(
                    "Take a shower (ghusl), trim nails, and apply perfume to the body (not the Ihram cloth).",
                    "Men wear two white unsewn wraps; women wear modest regular clothing without niqab or gloves.",
                    "Recite intention at the Miqat: 'Labbayk Allahumma Umrah' and begin the Talbiyah."
                ),
                stageDua = "Labbayk Allahumma Umrah • Labbayk Allahumma labbayk, labbayka la sharika laka labbayk",
                locationHint = "Miqat Boundary"
            ),
            RitualStep(
                id = "u2",
                badgeType = RitualBadgeType.RUKN,
                title = "Tawaf of Umrah (7 Rounds)",
                description = "Circumambulate the Holy Kaaba 7 times counter-clockwise starting and ending at the Black Stone.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Dashed circular path around the Kaaba moving counter-clockwise (Kaaba on your left) starting at the Black Stone.",
                detailedPoints = listOf(
                    "Men uncover the right shoulder (Idtiba') and walk briskly (Raml) in the first 3 rounds only.",
                    "Begin each round aligned with the Black Stone, stay outside Hijr Ismail, and make heartfelt dua.",
                    "Cover the shoulder after 7 rounds, pray 2 rak'ahs behind Maqam Ibrahim, and drink Zamzam."
                ),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar",
                locationHint = "Al-Masjid Al-Haram — Mataf"
            ),
            RitualStep(
                id = "u3",
                badgeType = RitualBadgeType.RUKN,
                title = "Sa'i Between Safa & Marwah",
                description = "Walk 7 laps between Mount Safa and Mount Marwah, jogging lightly for men between the green lights.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "Corridor linking Safa and Marwah hills with twin green marker pillars indicating the brisk-walking zone.",
                detailedPoints = listOf(
                    "Ascend Safa, face the Kaaba, recite the Ayah, and make dua three times.",
                    "Safa to Marwah counts as lap 1; Marwah back to Safa counts as lap 2 (7 laps total, ending at Marwah).",
                    "Men jog lightly between the two green markers; women walk calmly throughout."
                ),
                stageDua = "Innas-Safa wal-Marwata min sha'a'irillah • Allahu Akbar, la ilaha illallahu wahdah",
                locationHint = "Mas'aa Corridor — Safa & Marwah"
            ),
            RitualStep(
                id = "u4",
                badgeType = RitualBadgeType.WAJIB,
                title = "Halq or Taqsir (Exit Ihram)",
                description = "Complete Umrah by shaving or evenly trimming the hair to exit the state of Ihram.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.HALQ,
                visualCaption = "Completion badge showing hair shaving/trimming outside Marwah exit to lift all Ihram restrictions.",
                detailedPoints = listOf(
                    "Men shave the entire head (preferred) or trim hair evenly from all sides.",
                    "Women trim only a fingertip's length (~2 cm) from the ends of their hair.",
                    "Once done, Umrah is complete and all Ihram restrictions are lifted."
                ),
                stageDua = "Allahumma-ghfir lil-muhalliqina wal-muqassirin • Alhamdulillahi-lladhi hadana",
                locationHint = "Outside Marwah Exit"
            )
        ),
        NusukType.TAMATTU to listOf(
            RitualStep(
                id = "t1",
                badgeType = RitualBadgeType.RUKN,
                title = "Umrah of Tamattu & Exit Ihram",
                description = "Perform full Umrah during Hajj months, exit Ihram completely, and await 8th Dhul-Hijjah.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Complete Umrah Tawaf, Sa'i, and hair trim before putting on regular clothes until Hajj begins.",
                detailedPoints = listOf(
                    "Enter Ihram at the Miqat with intention for Umrah Tamattu.",
                    "Complete Tawaf (7), Sa'i (7), and trim hair to exit Ihram in Makkah.",
                    "Hady (sacrificial animal) is required on Eid day for Tamattu pilgrims."
                ),
                stageDua = "Labbayk Allahumma Umratan mutamatti'an biha ilal-Hajj",
                locationHint = "Miqat → Makkah"
            ),
            RitualStep(
                id = "t2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "8th Dhul-Hijjah — Day of Tarwiyah",
                description = "Re-enter Ihram for Hajj from your Makkah residence and stay overnight in Mina tents.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MINA,
                visualCaption = "Rows of white fire-resistant tents across Mina valley where 4-rak'ah prayers are shortened.",
                detailedPoints = listOf(
                    "Perform ghusl, wear Ihram in Makkah, and say: 'Labbayk Allahumma Hajjan'.",
                    "Proceed to Mina and pray Dhuhr, Asr, Maghrib, Isha, and Fajr shortened (2 rak'ahs) at their times.",
                    "Spend the night in Mina reciting Talbiyah and preparing spiritually for Arafat."
                ),
                stageDua = "Labbayk Allahumma Hajjan • Labbayk Allahumma labbayk",
                locationHint = "Makkah → Mina Tent City"
            ),
            RitualStep(
                id = "t3",
                badgeType = RitualBadgeType.RUKN,
                title = "9th Dhul-Hijjah — Standing at Arafat",
                description = "The supreme pillar of Hajj: remain inside Arafat boundaries in prayer and supplication until sunset.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Mount of Mercy (Jabal al-Rahmah) on the Arafat plain; standing anywhere inside Arafat's markers fulfills the pillar.",
                detailedPoints = listOf(
                    "Verify you are inside Arafat boundaries (note: the front section of Masjid Namirah lies outside).",
                    "Combine and shorten Dhuhr & Asr at Dhuhr time with one Adhan and two Iqamahs.",
                    "Dedicate the afternoon to heartfelt dua facing the Qibla until sunset."
                ),
                stageDua = "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadir",
                locationHint = "Plain of Arafat"
            ),
            RitualStep(
                id = "t4",
                badgeType = RitualBadgeType.WAJIB,
                title = "Night of 10th — Muzdalifah Stay",
                description = "Depart after sunset to Muzdalifah, combine Maghrib & Isha, sleep until Fajr, and gather pebbles.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MUZDALIFAH,
                visualCaption = "Open-air night stop at Muzdalifah under the stars for delayed combined prayers and pebble collection.",
                detailedPoints = listOf(
                    "Delay Maghrib and combine it with shortened Isha (2 rak'ahs) upon arriving in Muzdalifah.",
                    "Rest overnight until Fajr (elderly and women may leave after midnight).",
                    "Collect 7 chickpea-sized pebbles for Eid day stoning, pray Fajr early, and make dua."
                ),
                stageDua = "Allahumma-j'alhu Hajjan mabruran wa sa'yan mashkura",
                locationHint = "Muzdalifah"
            ),
            RitualStep(
                id = "t5",
                badgeType = RitualBadgeType.WAJIB,
                title = "10th Dhul-Hijjah — Eid Day & Stoning",
                description = "Stone Jamrat al-Aqabah (7 pebbles), offer Hady sacrifice, and shave/trim hair (First Tahallul).",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Multi-level Jamarat Bridge showing the 3 pillars; only the largest pillar (Kubra) is stoned on Eid morning.",
                detailedPoints = listOf(
                    "Throw 7 pebbles one by one at Jamrat al-Kubra saying 'Allahu Akbar' with each throw.",
                    "Stop Talbiyah and arrange the sacrificial Hady (e.g., via official Adahi voucher).",
                    "Shave or trim hair for First Tahallul (regular clothes permitted; marital intimacy remains restricted until Ifadah)."
                ),
                stageDua = "Allahu Akbar (with every pebble) • Bismillahi wallahu Akbar",
                locationHint = "Jamarat Bridge — Mina"
            ),
            RitualStep(
                id = "t6",
                badgeType = RitualBadgeType.RUKN,
                title = "Tawaf al-Ifadah & Hajj Sa'i",
                description = "Essential Hajj Tawaf (7 rounds) and Sa'i (7 laps) in Makkah completing Major Tahallul.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "7 counter-clockwise rounds around the Kaaba followed by 7 Sa'i laps between Safa and Marwah.",
                detailedPoints = listOf(
                    "Perform 7 rounds of Tawaf al-Ifadah around the Kaaba and pray 2 rak'ahs.",
                    "Perform 7 laps of Sa'i between Safa and Marwah (required for Tamattu pilgrims).",
                    "Completes full Major Tahallul, lifting all remaining Ihram restrictions."
                ),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar",
                locationHint = "Al-Masjid Al-Haram — Makkah"
            ),
            RitualStep(
                id = "t7",
                badgeType = RitualBadgeType.WAJIB,
                title = "11th–13th Dhul-Hijjah — Days of Tashreeq",
                description = "Stay overnight in Mina and stone all three Jamarat in order after solar noon each day.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Ordered progression across the bridge: Small (Sughra) → Middle (Wusta) → Large (Kubra), 7 pebbles each.",
                detailedPoints = listOf(
                    "After Dhuhr, stone Sughra (7 pebbles), step aside facing Qibla, and make long dua.",
                    "Stone Wusta (7 pebbles), step left facing Qibla, and make long dua.",
                    "Stone Kubra (7 pebbles) and depart without pausing. May leave early on the 12th before sunset."
                ),
                stageDua = "Allahu Akbar • Allahumma-j'alhu Hajjan mabruran wa dhanban maghfura",
                locationHint = "Mina & Jamarat Bridge"
            ),
            RitualStep(
                id = "t8",
                badgeType = RitualBadgeType.WAJIB,
                title = "Farewell Tawaf (Tawaf al-Wada')",
                description = "Final 7 rounds around the Kaaba immediately before departing Makkah.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Final 7 rounds around the Kaaba as the concluding act before your journey home.",
                detailedPoints = listOf(
                    "Perform 7 rounds without Raml or Idtiba' as your very last act in Makkah.",
                    "Pray 2 rak'ahs behind Maqam Ibrahim and depart promptly.",
                    "Waived for menstruating or postpartum women."
                ),
                stageDua = "Allahumma taqabbal minna warzuqnal-'awdata ila baytikal-haram",
                locationHint = "Al-Masjid Al-Haram — Makkah"
            )
        ),
        NusukType.IFRAD to listOf(
            RitualStep(
                id = "i1",
                badgeType = RitualBadgeType.RUKN,
                title = "Ihram for Hajj Only (Ifrad)",
                description = "Enter Ihram for Hajj alone at the Miqat and remain in Ihram until Eid day (no Hady required).",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Single continuous Ihram state dedicated solely to Hajj from Miqat through Eid.",
                detailedPoints = listOf(
                    "Intend Hajj only at the Miqat: 'Labbayk Allahumma Hajjan'.",
                    "Remain in Ihram after Tawaf al-Qudum without cutting hair.",
                    "No sacrificial Hady is obligatory for Ifrad pilgrims."
                ),
                stageDua = "Labbayk Allahumma Hajjan la riya'a fihi wa la sum'ah",
                locationHint = "Miqat Boundary"
            ),
            RitualStep(
                id = "i2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "Arrival Tawaf & Optional Hajj Sa'i",
                description = "Perform Tawaf al-Qudum upon reaching Makkah; you may optionally perform Hajj Sa'i right after.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "Tawaf al-Qudum at the Kaaba followed by Safa-Marwah Sa'i while staying in Ihram.",
                detailedPoints = listOf(
                    "Perform 7 rounds of Tawaf al-Qudum (with Raml & Idtiba' for men).",
                    "You may advance your 7-lap Hajj Sa'i immediately after it.",
                    "Stay in Ihram until the 10th of Dhul-Hijjah."
                ),
                stageDua = "Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu Akbar",
                locationHint = "Mataf & Mas'aa — Makkah"
            ),
            RitualStep(
                id = "i3",
                badgeType = RitualBadgeType.RUKN,
                title = "Mina, Arafat & Muzdalifah",
                description = "Spend 8th in Mina, stand at Arafat on 9th until sunset, and stay overnight in Muzdalifah.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Sacred progression: Mina tents → Mount of Mercy at Arafat → Muzdalifah under the night sky.",
                detailedPoints = listOf(
                    "Shorten prayers in Mina on 8th Dhul-Hijjah.",
                    "Stand inside Arafat boundaries on 9th Dhul-Hijjah in supplication until sunset.",
                    "Combine Maghrib & Isha in Muzdalifah and collect pebbles."
                ),
                stageDua = "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd",
                locationHint = "Mina → Arafat → Muzdalifah"
            ),
            RitualStep(
                id = "i4",
                badgeType = RitualBadgeType.RUKN,
                title = "Eid, Ifadah, Tashreeq & Farewell",
                description = "Stone Jamrat al-Aqabah, shave/trim, perform Tawaf al-Ifadah, Tashreeq stoning, and Farewell Tawaf.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Jamarat Bridge stoning followed by Tawaf al-Ifadah and concluding Farewell Tawaf.",
                detailedPoints = listOf(
                    "On 10th: stone Kubra (7 pebbles) and shave/trim hair (no Hady needed).",
                    "Perform Tawaf al-Ifadah (skip Sa'i if already done after Qudum).",
                    "Stone all 3 Jamarat during Tashreeq, then perform Farewell Tawaf."
                ),
                stageDua = "Allahu Akbar • Rabbana taqabbal minna innaka Antas-Sami'ul-'Alim",
                locationHint = "Jamarat → Al-Masjid Al-Haram"
            )
        ),
        NusukType.QIRAN to listOf(
            RitualStep(
                id = "q1",
                badgeType = RitualBadgeType.RUKN,
                title = "Combined Ihram for Umrah & Hajj",
                description = "Unite Umrah and Hajj in a single Ihram from the Miqat until Eid day (Hady required).",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Combined Qiran intention keeping one uninterrupted Ihram for both Umrah and Hajj.",
                detailedPoints = listOf(
                    "Declare intention at Miqat: 'Labbayk Allahumma Umratan wa Hajjan'.",
                    "Remain in Ihram after arriving in Makkah until Eid day.",
                    "Sacrificial Hady is obligatory on 10th Dhul-Hijjah."
                ),
                stageDua = "Labbayk Allahumma Umratan wa Hajjan",
                locationHint = "Miqat Boundary"
            ),
            RitualStep(
                id = "q2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "Arrival Tawaf & Single Sa'i",
                description = "Perform Tawaf al-Qudum and one 7-lap Sa'i that suffices for both Umrah and Hajj.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Tawaf al-Qudum around the Kaaba plus one shared Sa'i between Safa and Marwah.",
                detailedPoints = listOf(
                    "Perform 7 rounds of Arrival Tawaf upon entering Makkah.",
                    "One Sa'i covers both your Umrah and Hajj.",
                    "Do not cut hair yet; remain in Ihram until 10th Dhul-Hijjah."
                ),
                stageDua = "Allahumma barik lana fi nusukina wa a'inna 'ala dhikrik",
                locationHint = "Al-Masjid Al-Haram"
            ),
            RitualStep(
                id = "q3",
                badgeType = RitualBadgeType.RUKN,
                title = "Arafat, Mina, Ifadah & Farewell",
                description = "Complete Tarwiyah, Arafat, Muzdalifah, stoning, Hady, hair trim, Ifadah, Tashreeq, and Farewell Tawaf.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Full Hajj stages from Arafat and Muzdalifah through Jamarat and Farewell Tawaf.",
                detailedPoints = listOf(
                    "Stand at Arafat on 9th and stay overnight in Muzdalifah.",
                    "On 10th: stone Kubra, offer Hady sacrifice, shave/trim hair, and perform Tawaf al-Ifadah.",
                    "Complete Tashreeq stoning in Mina and conclude with Farewell Tawaf."
                ),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar",
                locationHint = "Arafat → Muzdalifah → Mina → Makkah"
            )
        ),
        NusukType.MADINAH to listOf(
            RitualStep(
                id = "m1",
                badgeType = RitualBadgeType.SUNNAH,
                title = "Visiting Al-Masjid an-Nabawi",
                description = "One prayer in the Prophet's Mosque is better than a thousand prayers elsewhere (except Al-Haram).",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "Illustration of the Green Dome, minarets, and courtyard shading umbrellas of Al-Masjid an-Nabawi.",
                detailedPoints = listOf(
                    "Enter with the right foot calmly reciting: 'Allahumma-ftah li abwaba rahmatik'.",
                    "Pray two rak'ahs of Tahiyyatul-Masjid and observe congregational prayers.",
                    "Maintain serenity and lowered voice out of reverence."
                ),
                stageDua = "Allahumma salli wa sallim 'ala nabiyyina Muhammad • Allahumma-ftah li abwaba rahmatik",
                locationHint = "Al-Masjid an-Nabawi — Madinah"
            ),
            RitualStep(
                id = "m2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "Al-Rawdah & Greeting the Prophet ﷺ",
                description = "Pray in Al-Rawdah ash-Sharifah (with Nusuk permit) and convey salam upon the Prophet ﷺ and his Companions.",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "The noble Rawdah area between the Prophet's Pulpit and Chamber beneath the Green Dome.",
                detailedPoints = listOf(
                    "Book your Al-Rawdah entry permit in advance via the Nusuk app.",
                    "Stand respectfully at the Sacred Chamber and say: 'As-salamu alayka ya Rasulallah'.",
                    "Offer salam to Abu Bakr as-Siddiq and Umar ibn al-Khattab (may Allah be pleased with them)."
                ),
                stageDua = "As-salamu alayka ya Rasulallah wa rahmatullahi wa barakatuh",
                locationHint = "Al-Rawdah ash-Sharifah"
            ),
            RitualStep(
                id = "m3",
                badgeType = RitualBadgeType.SUNNAH,
                title = "Masjid Quba, Al-Baqi & Uhud",
                description = "Purify at your hotel and pray 2 rak'ahs in Masjid Quba (equal to an Umrah reward), and visit Al-Baqi.",
                scene3D = Scene3DType.MADINAH,
                illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "Key prophetic landmarks in Madinah for Sunnah prayers and prayers for the righteous.",
                detailedPoints = listOf(
                    "Make wudu at your residence and pray 2 rak'ahs in Masjid Quba.",
                    "Visit Al-Baqi cemetery gate and recite the Sunnah salam for its people.",
                    "Visit the Martyrs of Uhud and make dua for them."
                ),
                stageDua = "As-salamu alaykum dara qawmin mu'minin wa inna in sha'Allahu bikum lahiqun",
                locationHint = "Quba, Al-Baqi & Uhud — Madinah"
            )
        )
    )

    private val frSteps: Map<NusukType, List<RitualStep>> = mapOf(
        NusukType.UMRAH to listOf(
            RitualStep(
                id = "u1",
                badgeType = RitualBadgeType.WAJIB,
                title = "L'Ihram depuis le Miqat",
                description = "Faire le ghusl, revêtir l'Ihram et formuler l'intention de l'Omra au Miqat.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Schéma des deux pièces blanches non cousues de l'Ihram (Izar et Rida) et de l'intention au Miqat.",
                detailedPoints = listOf(
                    "Prendre un bain rituel (ghusl) et se parfumer le corps avant l'intention.",
                    "L'homme porte deux draps blancs non cousus ; la femme garde ses vêtements pudiques sans niqab ni gants.",
                    "Prononcer au Miqat : « Labbayk Allahumma Umrah » et réciter la Talbiyah."
                ),
                stageDua = "Labbayk Allahumma Umrah • Labbayk Allahumma labbayk, labbayka la sharika laka labbayk",
                locationHint = "Zone du Miqat"
            ),
            RitualStep(
                id = "u2",
                badgeType = RitualBadgeType.RUKN,
                title = "Tawaf de l'Omra (7 tours)",
                description = "Effectuer 7 tours autour de la Kaaba dans le sens antihoraire à partir de la Pierre Noire.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Parcours circulaire antihoraire autour de la Kaaba (Kaaba à gauche), débutant à la Pierre Noire.",
                detailedPoints = listOf(
                    "Découvrir l'épaule droite (Idtiba') et presser le pas (Raml) durant les 3 premiers tours (hommes).",
                    "Commencer face à la Pierre Noire en contournant le Hijr Ismail.",
                    "Prier 2 rak'ahs derrière Maqam Ibrahim et boire l'eau de Zamzam."
                ),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar",
                locationHint = "Mosquée Sacrée — Mataf"
            ),
            RitualStep(
                id = "u3",
                badgeType = RitualBadgeType.RUKN,
                title = "Sa'i entre Safa et Marwah",
                description = "Parcourir 7 trajets entre les monts Safa et Marwah avec trot léger aux repères verts.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "Galerie reliant Safa à Marwah avec les deux colonnes lumineuses vertes indiquant l'accélération.",
                detailedPoints = listOf(
                    "Monter à Safa, faire face à la Kaaba et multiplier les invocations.",
                    "Safa → Marwah compte pour 1 trajet, Marwah → Safa pour le 2e (7 au total).",
                    "Trottiner entre les repères verts pour les hommes et marcher sereinement ailleurs."
                ),
                stageDua = "Innas-Safa wal-Marwata min sha'a'irillah • Allahu Akbar",
                locationHint = "Mas'aa — Safa et Marwah"
            ),
            RitualStep(
                id = "u4",
                badgeType = RitualBadgeType.WAJIB,
                title = "Rasage ou Coupe (Tahallul)",
                description = "Raser ou raccourcir les cheveux pour achever l'Omra et sortir de l'état d'Ihram.",
                scene3D = Scene3DType.MASAA,
                illustrationKey = StepIllustrationKey.HALQ,
                visualCaption = "Désacralisation complète après le 7e trajet à Marwah par le rasage ou la coupe uniforme.",
                detailedPoints = listOf(
                    "Le rasage complet est préférable pour l'homme, ou une coupe uniforme de toute la tête.",
                    "La femme coupe l'équivalent d'une phalange (~2 cm) aux pointes des cheveux.",
                    "Toutes les interdictions de l'Ihram sont désormais levées."
                ),
                stageDua = "Allahumma-ghfir lil-muhalliqina wal-muqassirin",
                locationHint = "Sortie de Marwah"
            )
        ),
        NusukType.TAMATTU to listOf(
            RitualStep(
                id = "t1",
                badgeType = RitualBadgeType.RUKN,
                title = "Omra du Tamattu et désacralisation",
                description = "Accomplir l'Omra complète durant les mois du Hajj puis quitter l'Ihram jusqu'au 8 Dhul-Hijjah.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Tawaf, Sa'i et coupe des cheveux avant de reprendre ses habits habituels jusqu'au Hajj.",
                detailedPoints = listOf(
                    "Formuler l'intention d'Omra Tamattu au Miqat.",
                    "Accomplir le Tawaf, le Sa'i et la coupe des cheveux.",
                    "Le sacrifice (Hady) sera obligatoire le jour de l'Aïd."
                ),
                stageDua = "Labbayk Allahumma Umratan mutamatti'an biha ilal-Hajj",
                locationHint = "Miqat → La Mecque"
            ),
            RitualStep(
                id = "t2",
                badgeType = RitualBadgeType.SUNNAH,
                title = "8 Dhul-Hijjah — Jour de Tarwiyah à Mina",
                description = "Reprendre l'Ihram pour le Hajj à La Mecque et passer la nuit dans les tentes de Mina.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MINA,
                visualCaption = "Cité des tentes blanches de Mina où les prières de 4 unités sont raccourcies à 2.",
                detailedPoints = listOf(
                    "Revêtir l'Ihram le matin du 8e jour à La Mecque : « Labbayk Allahumma Hajjan ».",
                    "Rejoindre Mina et raccourcir Dhuhr, Asr, Isha et prier Fajr à l'heure.",
                    "Multiplier la Talbiyah et le dhikr."
                ),
                stageDua = "Labbayk Allahumma Hajjan • Labbayk Allahumma labbayk",
                locationHint = "La Mecque → Mina"
            ),
            RitualStep(
                id = "t3",
                badgeType = RitualBadgeType.RUKN,
                title = "9 Dhul-Hijjah — Station à Arafat",
                description = "Pilier suprême du Hajj : demeurer dans les limites d'Arafat en invocation jusqu'au coucher du soleil.",
                scene3D = Scene3DType.ARAFAT,
                illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Mont de la Miséricorde et plaine d'Arafat ; tout le périmètre intérieur valide ce pilier.",
                detailedPoints = listOf(
                    "Vérifier de se trouver bien à l'intérieur des limites d'Arafat.",
                    "Regrouper et raccourcir Dhuhr et Asr à l'heure de Dhuhr.",
                    "Consacrer l'après-midi aux douas face à la Qibla jusqu'au Maghrib."
                ),
                stageDua = "La ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamd",
                locationHint = "Plaine d'Arafat"
            ),
            RitualStep(
                id = "t4",
                badgeType = RitualBadgeType.WAJIB,
                title = "Nuit du 10 — Muzdalifah",
                description = "Partir après le coucher vers Muzdalifah, regrouper Maghrib et Isha et ramasser 7 cailloux.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.MUZDALIFAH,
                visualCaption = "Halte nocturne à ciel ouvert à Muzdalifah pour la prière regroupée et la collecte des cailloux.",
                detailedPoints = listOf(
                    "Regrouper Maghrib et Isha (raccourci) dès l'arrivée à Muzdalifah.",
                    "Passer la nuit jusqu'au Fajr (dérogation après minuit pour les femmes et personnes fragiles).",
                    "Ramasser 7 petits cailloux pour la Grande Jamrah."
                ),
                stageDua = "Allahumma-j'alhu Hajjan mabruran wa sa'yan mashkura",
                locationHint = "Muzdalifah"
            ),
            RitualStep(
                id = "t5",
                badgeType = RitualBadgeType.WAJIB,
                title = "10 Dhul-Hijjah — Aïd et Lapidation",
                description = "Lancer 7 cailloux à la Grande Jamrah, offrir le Hady, puis raser ou couper les cheveux.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Pont des Jamarat ; seule la Grande stèle (Kubra) est lapidée le matin de l'Aïd.",
                detailedPoints = listOf(
                    "Lancer 7 cailloux un par un sur Jamrat al-Kubra en disant « Allahu Akbar ».",
                    "Accomplir le sacrifice du Hady (ou mandat officiel).",
                    "Se raser ou couper les cheveux (1er Tahallul)."
                ),
                stageDua = "Allahu Akbar (à chaque caillou)",
                locationHint = "Pont des Jamarat — Mina"
            ),
            RitualStep(
                id = "t6",
                badgeType = RitualBadgeType.RUKN,
                title = "Tawaf al-Ifadah et Sa'i du Hajj",
                description = "Pilier majeur du Hajj à La Mecque réalisant la désacralisation complète.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "7 tours autour de la Kaaba suivis de 7 trajets entre Safa et Marwah.",
                detailedPoints = listOf(
                    "Effectuer 7 tours autour de la Kaaba puis 2 rak'ahs.",
                    "Accomplir 7 trajets de Sa'i entre Safa et Marwah.",
                    "Lève l'ensemble des dernières restrictions d'Ihram."
                ),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar",
                locationHint = "Mosquée Sacrée — La Mecque"
            ),
            RitualStep(
                id = "t7",
                badgeType = RitualBadgeType.WAJIB,
                title = "11–13 Dhul-Hijjah — Jours de Tachriq",
                description = "Nuitée à Mina et lapidation des 3 Jamarat dans l'ordre chaque après-midi.",
                scene3D = Scene3DType.JAMARAT,
                illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Progression ordonnée : Petite (Sughra) → Moyenne (Wusta) → Grande (Kubra), 7 cailloux chacune.",
                detailedPoints = listOf(
                    "Après le zénith, lapider Sughra (7) puis invoquer longuement face à la Qibla.",
                    "Lapider Wusta (7) puis s'écarter à gauche pour invoquer.",
                    "Lapider Kubra (7) et repartir directement."
                ),
                stageDua = "Allahu Akbar • Allahumma-j'alhu Hajjan mabrura",
                locationHint = "Mina & Jamarat"
            ),
            RitualStep(
                id = "t8",
                badgeType = RitualBadgeType.WAJIB,
                title = "Tawaf d'Adieu (Al-Wada')",
                description = "Ultimes 7 tours autour de la Kaaba juste avant de quitter La Mecque.",
                scene3D = Scene3DType.KAABA,
                illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "Dernier acte sacré autour de la Kaaba avant le départ.",
                detailedPoints = listOf(
                    "Effectuer 7 tours puis 2 rak'ahs avant le voyage de retour.",
                    "Dispensé pour les femmes en période de menstrues ou lochies."
                ),
                stageDua = "Allahumma taqabbal minna warzuqnal-'awdata ila baytikal-haram",
                locationHint = "Mosquée Sacrée — La Mecque"
            )
        ),
        NusukType.IFRAD to listOf(
            RitualStep(
                id = "i1", badgeType = RitualBadgeType.RUKN,
                title = "Ihram pour le Hajj seul (Ifrad)",
                description = "Formuler l'intention du Hajj seul au Miqat sans sacrifice Hady obligatoire.",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Ihram continu consacré uniquement au Hajj jusqu'au jour de l'Aïd.",
                detailedPoints = listOf("Dire au Miqat : « Labbayk Allahumma Hajjan ».", "Rester en Ihram jusqu'au 10e jour.", "Aucun Hady requis."),
                stageDua = "Labbayk Allahumma Hajjan", locationHint = "Miqat"
            ),
            RitualStep(
                id = "i2", badgeType = RitualBadgeType.SUNNAH,
                title = "Tawaf d'Arrivée et Sa'i",
                description = "Effectuer le Tawaf al-Qudum à l'arrivée, avec possibilité d'avancer le Sa'i du Hajj.",
                scene3D = Scene3DType.MASAA, illustrationKey = StepIllustrationKey.SAI,
                visualCaption = "Tawaf d'arrivée suivi du Sa'i entre Safa et Marwah en restant en Ihram.",
                detailedPoints = listOf("7 tours autour de la Kaaba.", "Sa'i optionnel avancé entre Safa et Marwah.", "Ne pas couper les cheveux."),
                stageDua = "Subhanallahi walhamdulillahi wa la ilaha illallah", locationHint = "La Mecque"
            ),
            RitualStep(
                id = "i3", badgeType = RitualBadgeType.RUKN,
                title = "Mina, Arafat et Muzdalifah",
                description = "Accomplir les stations de Tarwiyah, Arafat (pilier) et Muzdalifah.",
                scene3D = Scene3DType.ARAFAT, illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Circuit des lieux saints : Mina → Arafat → Muzdalifah.",
                detailedPoints = listOf("Nuit à Mina le 8e jour.", "Station à Arafat le 9e jour jusqu'au coucher.", "Nuit et collecte de cailloux à Muzdalifah."),
                stageDua = "La ilaha illallahu wahdahu la sharika lah", locationHint = "Mina → Arafat → Muzdalifah"
            ),
            RitualStep(
                id = "i4", badgeType = RitualBadgeType.RUKN,
                title = "Aïd, Ifadah, Tachriq et Adieu",
                description = "Lapidation, rasage, Tawaf al-Ifadah, jours de Tachriq puis Tawaf d'Adieu.",
                scene3D = Scene3DType.JAMARAT, illustrationKey = StepIllustrationKey.JAMARAT,
                visualCaption = "Pont des Jamarat puis retour à la Kaaba pour l'Ifadah et l'Adieu.",
                detailedPoints = listOf("Le 10 : lapider Kubra et couper les cheveux.", "Tawaf al-Ifadah à La Mecque.", "Tachriq à Mina puis Tawaf d'Adieu."),
                stageDua = "Allahu Akbar • Rabbana taqabbal minna", locationHint = "Mina → La Mecque"
            )
        ),
        NusukType.QIRAN to listOf(
            RitualStep(
                id = "q1", badgeType = RitualBadgeType.RUKN,
                title = "Ihram conjoint Omra & Hajj",
                description = "Réunir Omra et Hajj dans un seul Ihram au Miqat avec sacrifice Hady.",
                scene3D = Scene3DType.KAABA, illustrationKey = StepIllustrationKey.IHRAM,
                visualCaption = "Un seul état d'Ihram ininterrompu regroupant Omra et Hajj.",
                detailedPoints = listOf("Dire au Miqat : « Labbayk Allahumma Umratan wa Hajjan ».", "Rester en Ihram jusqu'au 10e jour.", "Sacrifice Hady obligatoire."),
                stageDua = "Labbayk Allahumma Umratan wa Hajjan", locationHint = "Miqat"
            ),
            RitualStep(
                id = "q2", badgeType = RitualBadgeType.SUNNAH,
                title = "Tawaf d'Arrivée et Sa'i unique",
                description = "Un seul Sa'i après le Tawaf d'arrivée suffit pour l'Omra et le Hajj.",
                scene3D = Scene3DType.MASAA, illustrationKey = StepIllustrationKey.TAWAF,
                visualCaption = "7 tours autour de la Kaaba et 7 trajets uniques entre Safa et Marwah.",
                detailedPoints = listOf("7 tours de Tawaf al-Qudum.", "Un seul Sa'i valable pour les deux rites.", "Rester en Ihram jusqu'à l'Aïd."),
                stageDua = "Allahumma barik lana fi nusukina", locationHint = "Mosquée Sacrée"
            ),
            RitualStep(
                id = "q3", badgeType = RitualBadgeType.RUKN,
                title = "Arafat, Mina, Ifadah et Adieu",
                description = "Accomplir Arafat, Muzdalifah, lapidation, Hady, coupe, Ifadah et Adieu.",
                scene3D = Scene3DType.ARAFAT, illustrationKey = StepIllustrationKey.ARAFAT,
                visualCaption = "Toutes les étapes du Hajj d'Arafat jusqu'au Tawaf d'Adieu.",
                detailedPoints = listOf("Arafat le 9e jour et nuit à Muzdalifah.", "Le 10 : lapidation, sacrifice, coupe et Tawaf al-Ifadah.", "Tachriq à Mina et Tawaf d'Adieu."),
                stageDua = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasana", locationHint = "Arafat → Mina → La Mecque"
            )
        ),
        NusukType.MADINAH to listOf(
            RitualStep(
                id = "m1", badgeType = RitualBadgeType.SUNNAH,
                title = "Visite de la Mosquée du Prophète",
                description = "Une prière y vaut mieux que mille ailleurs (sauf à la Mosquée Sacrée).",
                scene3D = Scene3DType.MADINAH, illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "Le Dôme Vert, les minarets et les ombrelles de l'esplanade à Médine.",
                detailedPoints = listOf("Entrer du pied droit avec recueillement.", "Prier 2 rak'ahs de salutation de la mosquée.", "Maintenir calme et respect."),
                stageDua = "Allahumma salli wa sallim 'ala nabiyyina Muhammad", locationHint = "Mosquée du Prophète — Médine"
            ),
            RitualStep(
                id = "m2", badgeType = RitualBadgeType.SUNNAH,
                title = "Al-Rawdah et Salam au Prophète ﷺ",
                description = "Prier à Al-Rawdah (avec permis Nusuk) et saluer le Prophète ﷺ et ses Compagnons.",
                scene3D = Scene3DType.MADINAH, illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "Espace béni d'Al-Rawdah entre la Chaire et la Chambre Prophétique.",
                detailedPoints = listOf("Réserver un créneau Al-Rawdah sur l'application Nusuk.", "Saluer avec humilité : « As-salamu alayka ya Rasulallah ».", "Saluer Abu Bakr et Umar (qu'Allah les agrée)."),
                stageDua = "As-salamu alayka ya Rasulallah wa rahmatullahi wa barakatuh", locationHint = "Al-Rawdah"
            ),
            RitualStep(
                id = "m3", badgeType = RitualBadgeType.SUNNAH,
                title = "Mosquée de Quba, Al-Baqi et Uhud",
                description = "Prier 2 rak'ahs à Quba (récompense d'une Omra) et visiter Al-Baqi.",
                scene3D = Scene3DType.MADINAH, illustrationKey = StepIllustrationKey.MADINAH,
                visualCaption = "Hauts lieux historiques et spirituels de Médine.",
                detailedPoints = listOf("Faire ses ablutions à l'hôtel puis prier 2 rak'ahs à Quba.", "Invoquer pour les gens d'Al-Baqi.", "Visiter les martyrs d'Uhud."),
                stageDua = "As-salamu alaykum dara qawmin mu'minin", locationHint = "Quba, Al-Baqi & Uhud"
            )
        )
    )

    private val enRoundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "Round 1: 'Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu Akbar' — begin at Black Stone (Raml & Idtiba' for men).",
            "Round 2: 'Allahumma inni as'alukal-'afwa wal-'afiyata fid-dunya wal-akhirah.'",
            "Round 3: 'Allahumma-ghfir li dhunubi wa wassi' li fi dari wa barik li fi rizqi.'",
            "Round 4: 'Rabbighfir warham wa tajawaz 'amma ta'lamu innaka Antal-A'azzul-Akram.'",
            "Round 5: 'Allahumma azillani tahta zilli 'arshika yawma la zilla illa zilluk.'",
            "Round 6: 'Allahumma inni as'aluka mujibati rahmatik wa 'aza'ima maghfiratik.'",
            "Round 7: 'Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar' — then pray 2 rak'ahs."
        ),
        CounterMode.SAI to listOf(
            "Lap 1 (Safa → Marwah): 'Innas-Safa wal-Marwata min sha'a'irillah' — brisk jog between green lights for men.",
            "Lap 2 (Marwah → Safa): Increase remembrance of Allah and heartfelt supplication.",
            "Lap 3: 'Rabbighfir li wa li-walidayya wa lil-mu'minina yawma yaqumul-hisab.'",
            "Lap 4: 'Allahumma habbib ilaynal-imana wa zayyinhu fi qulubina.'",
            "Lap 5: 'Allahumma qini 'adhabaka yawma tab'athu 'ibadak.'",
            "Lap 6: 'Allahumma ati nafsi taqwaha wa zakkiha Anta khayru man zakkaha.'",
            "Lap 7 (Final): Conclude with dua at Marwah, then shave or trim hair."
        ),
        CounterMode.JAMARAT to listOf(
            "Pebble 1: 'Allahu Akbar.'",
            "Pebble 2: 'Allahu Akbar.'",
            "Pebble 3: 'Allahu Akbar.'",
            "Pebble 4: 'Allahu Akbar.'",
            "Pebble 5: 'Allahu Akbar.'",
            "Pebble 6: 'Allahu Akbar.'",
            "Pebble 7: 'Allahu Akbar' — face Qibla and make dua after Sughra & Wusta only."
        )
    )

    private val frRoundDuas: Map<CounterMode, List<String>> = mapOf(
        CounterMode.TAWAF to listOf(
            "Tour 1 : « Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu Akbar » dès la Pierre Noire.",
            "Tour 2 : « Allahumma inni as'alukal-'afwa wal-'afiyata fid-dunya wal-akhirah. »",
            "Tour 3 : « Allahumma-ghfir li dhunubi wa barik li fi rizqi. »",
            "Tour 4 : « Rabbighfir warham innaka Antal-A'azzul-Akram. »",
            "Tour 5 : « Allahumma azillani tahta zilli 'arshika yawma la zilla illa zilluk. »",
            "Tour 6 : « Allahumma inni as'aluka mujibati rahmatik. »",
            "Tour 7 : « Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar » puis 2 rak'ahs."
        ),
        CounterMode.SAI to listOf(
            "Trajet 1 (Safa → Marwah) : « Innas-Safa wal-Marwata min sha'a'irillah ».",
            "Trajet 2 (Marwah → Safa) : Multiplier le dhikr et les invocations.",
            "Trajet 3 : « Rabbighfir li wa li-walidayya wa lil-mu'minin. »",
            "Trajet 4 : « Allahumma habbib ilaynal-imana wa zayyinhu fi qulubina. »",
            "Trajet 5 : « Allahumma qini 'adhabaka yawma tab'athu 'ibadak. »",
            "Trajet 6 : « Allahumma ati nafsi taqwaha wa zakkiha. »",
            "Trajet 7 (Dernier) : Conclure à Marwah puis raser ou couper les cheveux."
        ),
        CounterMode.JAMARAT to listOf(
            "Caillou 1 : « Allahu Akbar. »",
            "Caillou 2 : « Allahu Akbar. »",
            "Caillou 3 : « Allahu Akbar. »",
            "Caillou 4 : « Allahu Akbar. »",
            "Caillou 5 : « Allahu Akbar. »",
            "Caillou 6 : « Allahu Akbar. »",
            "Caillou 7 : « Allahu Akbar » — invoquer face à la Qibla après Sughra et Wusta."
        )
    )
}
