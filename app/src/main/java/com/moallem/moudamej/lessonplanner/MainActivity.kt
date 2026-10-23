package com.moallem.moudamej.lessonplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF183153)
private val Teal = Color(0xFF138A83)
private val Pale = Color(0xFFF3F7FA)
private val Ink = Color(0xFF243447)

private data class TeachingMethod(
    val title: String,
    val icon: String,
    val summary: String,
    val steps: String,
    val suitable: String
)

private val methods = listOf(
    TeachingMethod("التعلّم التعاوني", "🤝", "يتعلم التلاميذ ضمن مجموعات صغيرة لتحقيق هدف مشترك.", "١. قسّم التلاميذ إلى مجموعات.\n٢. وزّع أدوارًا واضحة.\n٣. قدّم مهمة مشتركة.\n٤. ناقش النتائج مع الصف.", "القراءة، العلوم، الرياضيات، وحل المشكلات."),
    TeachingMethod("التعلّم النشط", "🙋", "يجعل التلميذ مشاركًا في السؤال والتجريب والتفسير بدل الاكتفاء بالاستماع.", "١. اطرح سؤالًا محفّزًا.\n٢. امنح وقتًا للتفكير.\n٣. اطلب مشاركة الإجابات.\n٤. لخّص الفكرة الأساسية.", "جميع المواد والمراحل."),
    TeachingMethod("العصف الذهني", "💡", "توليد أكبر عدد ممكن من الأفكار حول سؤال أو مشكلة قبل تقييمها.", "١. اكتب السؤال بوضوح.\n٢. استقبل الأفكار دون نقد.\n٣. سجّل الإجابات.\n٤. صنّف الأفكار واختر الأنسب.", "التمهيد للدروس، التعبير، والعلوم."),
    TeachingMethod("التعلّم بالاكتشاف", "🔎", "يصل التلميذ إلى المفهوم من خلال الملاحظة والمقارنة والاستنتاج.", "١. اعرض أمثلة أو أدوات.\n٢. اطلب الملاحظة والمقارنة.\n٣. وجّه بأسئلة قصيرة.\n٤. ساعد التلاميذ على صياغة الاستنتاج.", "الرياضيات والعلوم والقواعد."),
    TeachingMethod("حلّ المشكلات", "🧩", "يتعامل التلاميذ مع موقف يحتاج إلى تحليل واختيار حل مناسب.", "١. اعرض مشكلة واقعية.\n٢. حدّد المعطيات.\n٣. اقترح حلولًا.\n٤. اختبر الحل وناقش النتيجة.", "الرياضيات والعلوم والمواقف الحياتية."),
    TeachingMethod("التعلّم باللعب", "🎲", "توظيف لعبة تعليمية قصيرة لترسيخ المفاهيم وزيادة الدافعية.", "١. حدّد هدف اللعبة.\n٢. اشرح القواعد.\n٣. أشرك الجميع.\n٤. اربط النتيجة بهدف الدرس.", "الصفوف الأولى والمراجعة."),
    TeachingMethod("التعلّم البصري", "🖼️", "استخدام الصور والخرائط والمخططات لتوضيح العلاقات والأفكار.", "١. اعرض صورة أو مخططًا.\n٢. اسأل عمّا يلاحظه التلاميذ.\n٣. اربط العناصر بالمفهوم.\n٤. اطلب وصف الصورة أو إكمالها.", "القراءة والعلوم والاجتماعيات."),
    TeachingMethod("التعليم المتمايز", "🌈", "تنويع الأنشطة والدعم وفق مستويات التلاميذ واحتياجاتهم.", "١. حدّد هدفًا مشتركًا.\n٢. جهّز مهامًا بمستويات مختلفة.\n٣. قدّم دعمًا إضافيًا عند الحاجة.\n٤. قيّم التقدم بمرونة.", "الصفوف المتفاوتة المستوى ومعلمو الدمج."),
    TeachingMethod("التعلّم القائم على المشاريع", "🛠️", "إنجاز منتج أو مشروع صغير يطبّق فيه التلاميذ ما تعلموه.", "١. اختر سؤالًا أو مشكلة.\n٢. خطّط للمهام والوقت.\n٣. نفّذ العمل ضمن فرق.\n٤. اعرض المنتج وقيّمه.", "العلوم واللغة والأنشطة."),
    TeachingMethod("التعلّم بالاستقصاء", "🧪", "طرح سؤال ثم جمع الملاحظات والأدلة للوصول إلى تفسير.", "١. ابدأ بسؤال.\n٢. توقّع النتائج.\n٣. اجمع الملاحظات أو البيانات.\n٤. استنتج وناقش الأدلة.", "العلوم والبحث والملاحظة.")
)


private data class AutomaticPlan(
    val subject: String, val prior: String, val objectives: String, val intro: String,
    val method: String, val steps: String, val materials: String, val assessment: String,
    val guidance: String, val support: String, val closure: String
)

private fun buildAutomaticPlan(title: String): AutomaticPlan {
    val t = title.trim().lowercase()
    val math = listOf("جمع", "طرح", "ضرب", "قسمة", "كسر", "كسور", "عدد", "أعداد", "هندسة", "مستقيم", "زاوية", "محيط", "مساحة", "قياس", "مضاعف", "قاسم", "معادلة", "رياضيات").any { t.contains(it) }
    val english = listOf("english", "إنجليزي", "انكليزي", "حروف", "كلمات", "ضمائر", "verb", "noun").any { t.contains(it) }
    val science = listOf("علوم", "نبات", "حيوان", "ماء", "هواء", "طاقة", "جسم الإنسان", "كهرباء", "مادة", "بيئة", "ضوء", "حرارة", "دورة").any { t.contains(it) }
    val social = listOf("تاريخ", "جغرافيا", "وطن", "خريطة", "سكان", "حضارة", "سوريا", "سورية", "اجتماعيات").any { t.contains(it) }
    val arabic = listOf("لغة عربية", "نحو", "فاعل", "مفعول", "مبتدأ", "خبر", "فعل", "اسم", "حرف", "إملاء", "املاء", "قراءة", "نص", "تعبير", "همزة", "تاء", "جمع المذكر", "جمع المؤنث").any { t.contains(it) }
    val subject = when { math -> "الرياضيات"; english -> "اللغة الإنكليزية"; science -> "العلوم"; social -> "الاجتماعيات"; arabic -> "اللغة العربية"; else -> "تُحدَّد وفق عنوان الدرس" }
    val method = when { math -> "التعلّم بالاكتشاف وحلّ المشكلات"; science -> "الاستقصاء والملاحظة والتجريب الآمن"; english -> "النمذجة والتكرار والممارسة التواصلية"; social -> "الحوار وتحليل الصور والخرائط"; else -> "التعلّم النشط والحوار الموجّه" }
    val prior = "ابدأ بسؤال شفهي قصير مرتبط بعنوان «$title»، واستمع إلى إجابات التلاميذ لتقدير معرفتهم السابقة، ثم اربط إجاباتهم بهدف الدرس."
    val objectives = "بنهاية الحصة يُتوقّع أن يكون التلميذ قادرًا على: (1) شرح الفكرة الأساسية المرتبطة بـ«$title» بلغته. (2) تمييز المفهوم أو القاعدة في مثال مناسب. (3) تطبيق ما تعلّمه في نشاط قصير. (4) تفسير إجابته أو عرض خطوات تفكيره."
    val intro = when { math -> "اكتب مسألة بسيطة مرتبطة بعنوان الدرس، واطلب من التلاميذ توقّع طريقة الحل قبل تقديم القاعدة."; science -> "اعرض صورة أو موقفًا من الحياة اليومية مرتبطًا بالموضوع، ثم اسأل: ماذا نلاحظ؟ ولماذا يحدث ذلك؟"; english -> "اعرض كلمة أو صورة مرتبطة بالعنوان، وانطقها بوضوح، ثم اطلب من التلاميذ التوقّع أو التكرار."; social -> "اعرض صورة أو خريطة أو حقيقة قصيرة ذات صلة بالعنوان، واطلب من التلاميذ وصف ما يلاحظونه."; else -> "ابدأ بسؤال محفّز أو مثال قصير يتضمن فكرة من عنوان الدرس، ثم اربط إجابات التلاميذ بموضوع الحصة." }
    val steps = when {
        math -> "١. عرض مثال محسوس أو مرسوم.\n٢. نمذجة الحل خطوةً خطوةً مع التفكير بصوت مسموع.\n٣. حل مثال مشترك بمشاركة التلاميذ.\n٤. تكليف التلاميذ بمسألة فردية ثم مناقشة استراتيجيات الحل.\n٥. تصحيح الأخطاء الشائعة وتلخيص القاعدة."
        science -> "١. طرح سؤال استقصائي مرتبط بالموضوع.\n٢. طلب التنبؤات وتسجيلها.\n٣. فحص صورة أو أداة أو ملاحظة مناسبة وآمنة.\n٤. مقارنة الملاحظات بالتنبؤات وصياغة استنتاج.\n٥. ربط الاستنتاج بحياة التلميذ."
        english -> "١. تقديم المفردات أو التركيب بنموذج واضح.\n٢. شرح المعنى باستخدام صورة أو مثال.\n٣. تدريب جماعي موجّه على النطق أو الاستخدام.\n٤. ممارسة ثنائية قصيرة.\n٥. مهمة فردية للتحقق من الفهم."
        social -> "١. تحديد الفكرة أو المكان أو الحدث الأساسي.\n٢. قراءة النص أو تحليل الصورة والخريطة.\n٣. استخراج المعلومات المهمة وتفسيرها.\n٤. مناقشة سؤال يربط الدرس بالحياة.\n٥. تلخيص النتائج بكلمات التلاميذ."
        else -> "١. تقديم مثال واضح يشرح الفكرة الواردة في العنوان.\n٢. شرح المفهوم أو القاعدة على مراحل قصيرة.\n٣. التفكير بصوت مسموع أثناء حل مثال.\n٤. تنفيذ نشاط موجّه ثم تدريب فردي.\n٥. مناقشة الإجابات وتثبيت الفكرة الأساسية."
    }
    val materials = when { math -> "السبورة، الكتاب المدرسي، بطاقات أعداد أو أشكال، وأدوات بسيطة للرسم."; science -> "الكتاب المدرسي، صور أو مخططات، وأدوات ملاحظة آمنة متاحة."; english -> "الكتاب، بطاقات كلمات وصور، والسبورة."; social -> "الكتاب، خريطة أو صورة مرتبطة بالدرس، والسبورة."; else -> "الكتاب المدرسي، السبورة، وأمثلة أو بطاقات تعليمية مرتبطة بالعنوان." }
    val assessment = "أثناء الحصة: اطرح سؤالين قصيرين يختبران الفهم، واطلب من تلميذ شرح الإجابة. في الختام: أعطِ مهمة فردية قصيرة مرتبطة بـ«$title». معيار النجاح المقترح: إجابة صحيحة مع تفسير أو تطبيق مناسب."
    val guidance = "وزّع زمن الحصة: 5 دقائق للتمهيد، 10 دقائق للشرح والنمذجة، 15 دقيقة للنشاط والممارسة، 10 دقائق للتقويم والتغذية الراجعة، و5 دقائق للتلخيص. استخدم تعليمات قصيرة، وتحقّق من فهم المطلوب قبل بدء النشاط، ولا تنتقل إلى خطوة جديدة قبل التأكد من استيعاب الفكرة الأساسية."
    val support = "للتلاميذ الذين يحتاجون إلى دعم: قسّم المهمة إلى خطوات أصغر، وقدّم مثالًا محلولًا أو صورة/بطاقة، واسمح بوقت إضافي وإجابة شفهية عند الحاجة. للمتقدمين: أضف سؤالًا يتطلب تفسيرًا أو تطبيقًا جديدًا. راعِ الفروق الفردية دون إحراج أي تلميذ."
    val closure = "اطلب من التلميذ إكمال العبارة: «أهم ما تعلّمته اليوم هو…». واجب مقترح: سؤال أو تطبيق واحد من الكتاب أو مثال من الحياة اليومية مرتبط بعنوان «$title»."
    return AutomaticPlan(subject, prior, objectives, intro, method, steps, materials, assessment, guidance, support, closure)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Navy,
                    secondary = Teal,
                    background = Pale,
                    surface = Color.White
                )
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    LessonPlannerApp()
                }
            }
        }
    }
}

@Composable
private fun LessonPlannerApp() {
    var page by remember { mutableStateOf("home") }
    var grade by remember { mutableStateOf("الصف السادس") }
    var subject by remember { mutableStateOf("اللغة العربية") }
    var lesson by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("45 دقيقة") }
    var prior by remember { mutableStateOf("") }
    var objectives by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("التعلّم النشط") }
    var materials by remember { mutableStateOf("السبورة والكتاب وصور توضيحية") }
    var assessment by remember { mutableStateOf("أسئلة شفوية وتطبيق قصير") }
    var generated by remember { mutableStateOf(false) }
    var selectedMethod by remember { mutableStateOf(methods.first()) }

    Surface(modifier = Modifier.fillMaxSize(), color = Pale) {
        when (page) {
            "methods" -> MethodsPage(onBack = { page = "home" }, onSelect = {
                selectedMethod = it
                page = "method_detail"
            })
            "method_detail" -> MethodDetailPage(selectedMethod, onBack = { page = "methods" })
            "plan" -> PlanPage(
                grade, subject, lesson, duration, prior, objectives, method, materials, assessment,
                onBack = { page = "home" }
            )
            else -> HomePage(
                grade = grade, onGrade = { grade = it },
                subject = subject, onSubject = { subject = it },
                lesson = lesson, onLesson = { lesson = it },
                duration = duration, onDuration = { duration = it },
                prior = prior, onPrior = { prior = it },
                objectives = objectives, onObjectives = { objectives = it },
                method = method, onMethod = { method = it },
                materials = materials, onMaterials = { materials = it },
                assessment = assessment, onAssessment = { assessment = it },
                onMethods = { page = "methods" },
                onGenerate = { generated = true; page = "plan" }
            )
        }
    }
}

@Composable
private fun HomePage(
    grade: String, onGrade: (String) -> Unit,
    subject: String, onSubject: (String) -> Unit,
    lesson: String, onLesson: (String) -> Unit,
    duration: String, onDuration: (String) -> Unit,
    prior: String, onPrior: (String) -> Unit,
    objectives: String, onObjectives: (String) -> Unit,
    method: String, onMethod: (String) -> Unit,
    materials: String, onMaterials: (String) -> Unit,
    assessment: String, onAssessment: (String) -> Unit,
    onMethods: () -> Unit, onGenerate: () -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Box(
            Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(24.dp)).padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📚", fontSize = 34.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("مُحضِّر الدرس", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                        Text("خطّط لدرس فعّال في دقائق", color = Color(0xFFD9E7F5), fontSize = 14.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("أدوات عملية للمعلم داخل الصف", color = Color.White, fontSize = 15.sp)
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FeatureTile("📖", "تحضير درس", "خطّة منظمة", Modifier.weight(1f))
            FeatureTile("🧠", "طرائق حديثة", "أفكار مصوّرة", Modifier.weight(1f), onClick = onMethods)
        }
        Spacer(Modifier.height(18.dp))
        Text("اكتب عنوان الدرس فقط", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(8.dp))
        Text("سيتولى التطبيق اقتراح المادة والأهداف والتمهيد وخطوات الشرح والأنشطة والتقويم والإرشادات تلقائيًا، دون أسئلة إضافية.", color = Ink, fontSize = 14.sp, lineHeight = 23.sp)
        Spacer(Modifier.height(8.dp))
        FormField("عنوان الدرس", lesson, onLesson, "مثال: الكسور الاعتيادية")
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onGenerate,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal)
        ) {
            Icon(Icons.Default.EditNote, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("إنشاء تحضير الدرس", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onMethods,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Lightbulb, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("استعراض طرائق التدريس الحديثة")
        }
        Spacer(Modifier.height(18.dp))
        Text("المصمم: معلم دمج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)
        Text("نسخة أولية تعليمية تعمل دون إنترنت", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun FeatureTile(icon: String, title: String, subtitle: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Card(
        modifier = modifier.then(if (onClick != null) Modifier else Modifier),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick?.invoke() }
    ) {
        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 30.sp)
            Text(title, fontWeight = FontWeight.Bold, color = Navy)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun FormField(label: String, value: String, onValue: (String) -> Unit, placeholder: String, minLines: Int = 1) {
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(5.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValue,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            minLines = minLines,
            shape = RoundedCornerShape(13.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Teal,
                unfocusedBorderColor = Color(0xFFD4DEE8)
            )
        )
    }
}

@Composable
private fun MethodsPage(onBack: () -> Unit, onSelect: (TeachingMethod) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Header("طرائق التدريس الحديثة", onBack)
        Text("اختر طريقة لعرض شرحها وخطوات تطبيقها داخل الصف.", color = Color.Gray)
        Spacer(Modifier.height(12.dp))
        methods.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick = { onSelect(item) }
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(58.dp).background(Color(0xFFE7F5F3), RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                        Text(item.icon, fontSize = 29.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.title, color = Navy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(item.summary, color = Ink, fontSize = 13.sp, maxLines = 2)
                    }
                    Text("›", color = Teal, fontSize = 28.sp)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("المصمم: معلم دمج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MethodDetailPage(item: TeachingMethod, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Header(item.title, onBack)
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Box(Modifier.fillMaxWidth().background(Color(0xFFE7F5F3), RoundedCornerShape(20.dp)).padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(item.icon, fontSize = 68.sp)
                }
                Spacer(Modifier.height(16.dp))
                Text("الفكرة الأساسية", color = Teal, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(item.summary, color = Ink, fontSize = 15.sp)
                Spacer(Modifier.height(16.dp))
                Text("خطوات التطبيق", color = Teal, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(item.steps, color = Ink, fontSize = 15.sp, lineHeight = 27.sp)
                Spacer(Modifier.height(16.dp))
                Text("متى تُستخدم؟", color = Teal, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(item.suitable, color = Ink, fontSize = 15.sp)
                Spacer(Modifier.height(18.dp))
                Text("مراعاة الفروق الفردية: قدّم تعليمات قصيرة، ونموذجًا بصريًا، وخيارات مشاركة تناسب قدرات التلاميذ.", color = Navy, fontWeight = FontWeight.Medium)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("المصمم: معلم دمج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PlanPage(
    grade: String, subject: String, lesson: String, duration: String, prior: String,
    objectives: String, method: String, materials: String, assessment: String, onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Header("تحضير الدرس", onBack)
        Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(18.dp)) {
                val auto = remember(lesson) { buildAutomaticPlan(lesson) }
                Text(if (lesson.isBlank()) "عنوان الدرس: اكتب العنوان للبدء" else lesson, fontSize = 22.sp, color = Navy, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("الصف المقترح: الصف السادس الأساسي", color = Ink)
                Text("المادة المستنتجة: ${auto.subject}", color = Ink)
                Text("المدة المقترحة: 45 دقيقة", color = Ink)
                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                PlanSection("الخبرات السابقة", auto.prior)
                PlanSection("الأهداف التعليمية", auto.objectives)
                PlanSection("التمهيد", auto.intro)
                PlanSection("طريقة التدريس المقترحة", auto.method)
                PlanSection("خطوات سير الدرس", auto.steps)
                PlanSection("الوسائل التعليمية", auto.materials)
                PlanSection("التقويم وأسئلة التحقق", auto.assessment)
                PlanSection("إرشادات تنفيذ الدرس", auto.guidance)
                PlanSection("الدعم ومراعاة الفروق الفردية", auto.support)
                PlanSection("النشاط الختامي والواجب", auto.closure)
            }
        }
        Spacer(Modifier.height(14.dp))
        Text("المصمم: معلم دمج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)
        Text("هذه مسودة منظمة؛ راجع الأهداف والخطوات بما يناسب المنهاج ومستوى التلاميذ.", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun PlanSection(title: String, body: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 7.dp)) {
        Text(title, color = Teal, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(3.dp))
        Text(body, color = Ink, fontSize = 14.sp, lineHeight = 24.sp)
    }
}

@Composable
private fun Header(title: String, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(bottom = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = Navy) }
        Spacer(Modifier.width(6.dp))
        Text(title, fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Navy)
    }
}
