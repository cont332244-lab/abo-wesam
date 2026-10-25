package com.moallem.moudamej.lessonplanner

import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent\nimport androidx.activity.compose.rememberLauncherForActivityResult\nimport androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.io.OutputStreamWriter
import kotlin.concurrent.thread

private val Navy = Color(0xFF183153)
private val Teal = Color(0xFF138A83)
private val Pale = Color(0xFFF3F7FA)
private val Ink = Color(0xFF243447)
private val grades = listOf("الصف الأول", "الصف الثاني", "الصف الثالث", "الصف الرابع", "الصف الخامس", "الصف السادس")
private val subjects = listOf("اللغة العربية", "الرياضيات", "اللغة الإنكليزية", "العلوم", "الاجتماعيات", "التربية الإسلامية", "الفنون", "الرياضة", "أخرى")
private val teachingMethods = listOf("التعلّم النشط", "التعلّم بالاكتشاف", "التعلّم التعاوني", "الطريقة الاستقرائية", "الطريقة الاستنتاجية", "حلّ المشكلات", "الحوار والمناقشة", "العصف الذهني", "التعلّم باللعب", "التعلّم بالمشروعات", "التعليم المتمايز", "الشرح والنمذجة", "التعلّم بالاستقصاء")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Navy, secondary = Teal, background = Pale, surface = Color.White)) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { PlannerScreen(this) }
            }
        }
    }
}

@Composable
private fun PlannerScreen(activity: ComponentActivity) {
    var grade by remember { mutableStateOf(grades[0]) }
    var subject by remember { mutableStateOf(subjects[0]) }
    var lesson by remember { mutableStateOf("") }
    var method by remember { mutableStateOf(teachingMethods[0]) }
    var duration by remember { mutableStateOf("45 دقيقة") }
    var notes by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("يعمل التطبيق دون إنترنت بقوالب جاهزة، ولا يحتاج إلى مفتاح API.") }
    val prefs = remember { activity.getSharedPreferences("planner_settings", Context.MODE_PRIVATE) }
    var teacher by remember { mutableStateOf(prefs.getString("teacher", "") ?: "") }
    var school by remember { mutableStateOf(prefs.getString("school", "") ?: "") }
    var signature by remember { mutableStateOf(prefs.getString("signature", "معلم دمج") ?: "معلم دمج") }
    var curriculum by remember { mutableStateOf(prefs.getString("curriculum", "المنهاج السوري") ?: "المنهاج السوري") }
    var customSubject by remember { mutableStateOf("") }
    var showProfile by remember { mutableStateOf(false) }
    var savedPlans by remember { mutableStateOf(try { val a = JSONArray(prefs.getString("saved_plans", "[]")); (0 until a.length()).map { a.getJSONObject(it) } } catch (_: Exception) { emptyList<JSONObject>() }) }
    val wordLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/msword")) { uri ->
        if (uri != null && plan.isNotBlank()) try {
            val html = "<html><meta charset=\\"UTF-8\\"><body dir=\\"rtl\\"><pre style=\\"white-space:pre-wrap;font-family:Arial\\">" + plan.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") + "</pre></body></html>"
            activity.contentResolver.openOutputStream(uri)?.use { it.write(html.toByteArray(Charsets.UTF_8)) }
            message = "تم تصدير الملف لفتحه وتعديله في Word."
        } catch (_: Exception) { message = "تعذر حفظ ملف Word." }
    }

    Column(Modifier.fillMaxSize().background(Pale).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("📚 دفتر التحضير الإلكتروني", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("تحضير مخصص للصف والمادة وطريقة التدريس", color = Color(0xFFD9E7F5), fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { showProfile = !showProfile }, modifier = Modifier.fillMaxWidth()) { Text("⚙ بيانات المعلم والمدرسة والمنهج") }
        if (showProfile) {
            InputField("اسم المعلم", teacher, { teacher = it; prefs.edit().putString("teacher", it).apply() }, "اسم المعلم")
            InputField("اسم المدرسة", school, { school = it; prefs.edit().putString("school", it).apply() }, "اسم المدرسة")
            InputField("التوقيع أو الصفة", signature, { signature = it; prefs.edit().putString("signature", it).apply() }, "معلم دمج")
            InputField("المنهج (يمكن تغيير الاسم)", curriculum, { curriculum = it; prefs.edit().putString("curriculum", it).apply() }, "المنهاج السوري")
            InputField("مادة إضافية اختيارية", customSubject, { customSubject = it }, "اكتب اسم مادة جديدة")
            Text("تُحفظ بيانات المعلم والمدرسة على هذا الهاتف. يمكن كتابة اسم أي منهج لإضافته.", color = Ink, fontSize = 12.sp)
        }
        Text("بيانات الدرس", fontSize = 20.sp, color = Navy, fontWeight = FontWeight.Bold)
        PickerField("الصف", grade, grades) { grade = it }
        PickerField("المادة", subject, if (customSubject.isBlank() || subjects.contains(customSubject.trim())) subjects else subjects + customSubject.trim()) { subject = it }
        PickerField("طريقة التدريس", method, teachingMethods) { method = it }
        InputField("عنوان الدرس", lesson, { lesson = it }, "مثال: الكسور المتكافئة")
        InputField("مدة الحصة", duration, { duration = it }, "45 دقيقة")
        InputField("ملاحظات إضافية (اختياري)", notes, { notes = it }, "مستوى التلاميذ أو أهداف محددة", 2)
        Spacer(Modifier.height(12.dp))
        Text("إنشاء التحضير دون إنترنت", fontSize = 18.sp, color = Navy, fontWeight = FontWeight.Bold)
        Text("لا يحتاج التطبيق إلى اتصال أو مفتاح API. يُنشئ تحضيرًا محليًا منظمًا حسب الصف والمادة وطريقة التدريس، ويمكنك تعديله قبل الطباعة.", color = Ink, fontSize = 13.sp, lineHeight = 20.sp)
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            if (lesson.isBlank()) {
                message = "يرجى إدخال عنوان الدرس أولاً."
            } else {
                plan = generateOfflinePlan(grade, subject, lesson.trim(), method, duration, notes)\n                plan = plan.replace("تحضير درس", "تحضير درس — $curriculum").replace("إعداد: معلم دمج", "المعلم: ${teacher.ifBlank { "........................" }}\\nالمدرسة: ${school.ifBlank { "........................" }}\\nإعداد: $signature")
                message = "تم إنشاء التحضير محليًا دون إنترنت. راجعه وعدّله ثم انسخه أو اطبعه."
            }
        }, modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal), shape = RoundedCornerShape(15.dp)) {
            Text("📘 إنشاء التحضير دون إنترنت", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Text(message, color = Ink, fontSize = 13.sp, lineHeight = 20.sp)
        if (plan.isNotBlank()) {
            Spacer(Modifier.height(16.dp))
            Text("التحضير الناتج — قابل للتعديل", fontSize = 20.sp, color = Navy, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = plan, onValueChange = { plan = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 360.dp), shape = RoundedCornerShape(16.dp), minLines = 12,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Teal))
            Spacer(Modifier.height(10.dp))
            Button(onClick = {
                val arr = JSONArray().put(JSONObject().put("grade", grade).put("subject", subject).put("lesson", lesson).put("plan", plan))
                savedPlans.forEach { arr.put(it) }; while (arr.length() > 30) arr.remove(arr.length() - 1)
                prefs.edit().putString("saved_plans", arr.toString()).apply()
                savedPlans = try { (0 until arr.length()).map { arr.getJSONObject(it) } } catch (_: Exception) { emptyList() }
                message = "تم حفظ التحضير على الهاتف."
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Teal)) { Text("💾 حفظ التحضير") }
            if (savedPlans.isNotEmpty()) {
                Text("التحاضير المحفوظة", color = Navy, fontWeight = FontWeight.Bold)
                savedPlans.forEach { item ->
                    OutlinedButton(onClick = { plan = item.optString("plan"); lesson = item.optString("lesson"); grade = item.optString("grade", grades[0]); subject = item.optString("subject", subjects[0]); message = "تم فتح التحضير المحفوظ." }, modifier = Modifier.fillMaxWidth()) { Text("${item.optString("grade")} • ${item.optString("subject")} • ${item.optString("lesson")}") }
                }
            }
            OutlinedButton(onClick = { wordLauncher.launch("تحضير-${lesson.ifBlank { "درس" }}.doc") }, modifier = Modifier.fillMaxWidth()) { Text("📄 تصدير إلى Word") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = {
                    val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("تحضير الدرس", plan))
                    message = "تم نسخ التحضير إلى الحافظة."
                }, modifier = Modifier.weight(1f)) { Text("نسخ التحضير") }
                Button(onClick = { printPlan(activity, plan) }, modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy)) { Text("طباعة / PDF") }
            }
        }
        Spacer(Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))
        Text("إعداد: $signature", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)\n        if (teacher.isNotBlank()) Text("المعلم: $teacher", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Ink)\n        if (school.isNotBlank()) Text("المدرسة: $school", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Ink)
        Text("بالتعاون مع أبو محمد وأبو عبدالله وعصام", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Ink, fontSize = 12.sp)
        Text("منصة معلمي الجزيرة السورية", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Navy, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
private fun PickerField(label: String, selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(13.dp)) {
                Text(selected, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                Text("▾")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(0.88f)) {
                options.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { onSelected(option); expanded = false }) }
            }
        }
    }
}

@Composable
private fun InputField(label: String, value: String, onValue: (String) -> Unit, placeholder: String, minLines: Int = 1) {
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(value = value, onValueChange = onValue, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp) }, minLines = minLines,
            shape = RoundedCornerShape(13.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Teal))
    }
}

private fun generateOfflinePlan(grade: String, subject: String, lesson: String, method: String, duration: String, notes: String): String {
    val noteText = if (notes.isBlank()) "لا توجد ملاحظات إضافية." else notes
    val methodDescription = when (method) {
        "التعلّم بالاكتشاف" -> "يعرض المعلم أمثلة، ويوجه التلاميذ إلى الملاحظة والمقارنة واكتشاف الفكرة، ثم يناقش استنتاجاتهم."
        "التعلّم التعاوني" -> "يقسم المعلم التلاميذ إلى مجموعات صغيرة بأدوار واضحة، ويكلفهم بمهمة ثم تعرض كل مجموعة نتائجها."
        "حلّ المشكلات" -> "يطرح المعلم مشكلة مرتبطة بالدرس، ويطلب اقتراح الحلول واختبارها ومناقشة الطريقة الأنسب."
        "الحوار والمناقشة" -> "يطرح المعلم أسئلة متدرجة، ويمنح التلاميذ فرصة للتفكير والتعبير، ثم يلخص الإجابات ويصحح المفاهيم."
        "العصف الذهني" -> "يجمع المعلم أفكار التلاميذ حول سؤال محوري، ثم يصنفها ويناقش مدى ارتباطها بالدرس."
        "التعلّم باللعب" -> "ينظم المعلم لعبة تعليمية قصيرة مرتبطة بالهدف، ثم يربط نتائجها بالمفهوم المستهدف."
        "الطريقة الاستقرائية" -> "يقدم المعلم أمثلة متنوعة، ويوجه التلاميذ إلى ملاحظة أوجه الشبه والاختلاف واستنتاج القاعدة."
        "الطريقة الاستنتاجية" -> "يشرح المعلم القاعدة أولًا، ثم يقدم أمثلة محلولة ويطلب تطبيقها على أمثلة جديدة."
        "التعليم المتمايز" -> "يقدم المعلم مهامًا متدرجة: دعمًا إضافيًا للمتعثرين، ومهمة أساسية للجميع، ونشاطًا إثرائيًا للمتقدمين."
        "الشرح والنمذجة" -> "يوضح المعلم المهارة خطوة بخطوة، ويحل مثالًا بصوت مسموع، ثم يوجه التلاميذ إلى التطبيق المستقل."
        else -> "يمهد المعلم بسؤال أو مثال، ويعرض المفهوم تدريجيًا، ثم يوجه تدريبًا مشتركًا يتبعه تطبيق فردي."
    }
    val activity = when (subject) {
        "الرياضيات" -> "حل مثال نموذجي على اللوح، ثم مسائل متدرجة يشرح التلاميذ خطوات حلها."
        "اللغة العربية" -> "قراءة مثال قصير، وتحديد المهارة المستهدفة، ثم تطبيقها شفهيًا وكتابيًا في جمل."
        "اللغة الإنكليزية" -> "التدرب على مفردات أو تراكيب الدرس، ثم استخدامها في جمل أو حوار قصير."
        "العلوم" -> "ملاحظة مثال أو ظاهرة آمنة، وتسجيل الملاحظات ومقارنتها بالمفهوم المستهدف."
        "الاجتماعيات" -> "قراءة نص أو خريطة أو صورة، واستخراج المعلومات والإجابة عن أسئلة الفهم والاستنتاج."
        "التربية الإسلامية" -> "مناقشة الفكرة أو القيمة المستهدفة وربطها بموقف عملي من الحياة اليومية."
        "الرياضة" -> "إحماء مناسب ثم تدريب متدرج على المهارة مع مراعاة السلامة والفروق الفردية."
        else -> "تنفيذ نشاط تطبيقي قصير مرتبط بعنوان الدرس، ثم عرض النتائج ومناقشتها."
    }
    return """
تحضير درس

أولًا: معلومات الدرس
الصف: $grade
المادة: $subject
عنوان الدرس: $lesson
طريقة التدريس: $method
مدة الحصة: $duration
ملاحظات المعلم: $noteText

ثانيًا: الأهداف التعليمية
يتوقع من التلميذ أن:
1. يوضح الفكرة أو المهارة الأساسية في درس $lesson.
2. يحدد أهم العناصر المرتبطة بالدرس.
3. يطبق ما تعلمه في مثال مناسب لمستواه.
4. يجيب عن أسئلة التقويم ويصحح أخطاءه.

ثالثًا: الخبرات السابقة
مراجعة المعلومات السابقة ذات الصلة من خلال أسئلة قصيرة وتحديد ما يحتاج إلى توضيح.

رابعًا: التمهيد وإثارة الدافعية
يطرح المعلم سؤالًا أو يعرض صورة أو مثالًا من البيئة القريبة يرتبط بعنوان الدرس، ثم يوضح هدف التعلم.

خامسًا: الوسائل التعليمية
السبورة والأقلام، الكتاب المدرسي عند توفره، بطاقات وأمثلة وأدوات صفية مناسبة لطبيعة الدرس.

سادسًا: سير الدرس
1. التهيئة والمراجعة — 5 دقائق: استرجاع الخبرات السابقة وطرح سؤال تمهيدي.
2. تقديم المفهوم — 10 دقائق: $methodDescription
3. التدريب الموجه — 10 دقائق: حل مثال مشترك وطرح أسئلة للتحقق من الفهم.
4. النشاط التطبيقي — 15 دقيقة: $activity
5. التقويم والخلاصة — 5 دقائق: أسئلة ختامية وتلخيص الفكرة الأساسية.

تنبيه: التوزيع الزمني نموذج لحصة من 45 دقيقة؛ عدّله ليتناسب مع مدة الحصة الفعلية ($duration).

سابعًا: دور المعلم والتلاميذ
دور المعلم: التنظيم والتوجيه وطرح الأسئلة وملاحظة المشاركة وتقديم تغذية راجعة.
دور التلاميذ: الملاحظة والمناقشة وتنفيذ النشاط وشرح طريقة التفكير ومراجعة الإجابات.

ثامنًا: التقويم
1. ما الفكرة الأساسية في درس $lesson؟
2. قدم مثالًا يوضح فهمك للدرس.
3. ما النقطة التي تحتاج إلى تدريب إضافي؟
تُعدّل الأسئلة لتناسب المحتوى الدقيق للدرس، لأن هذا التحضير أُنشئ محليًا من دون اتصال بالإنترنت.

تاسعًا: مراعاة الفروق الفردية والدمج
- المتعثرون: تعليمات قصيرة، مثال محلول، بطاقات أو صور، وتقسيم المهمة إلى خطوات.
- بقية التلاميذ: تنفيذ المهمة الأساسية مع متابعة المعلم.
- المتقدمون: سؤال إثرائي أو مثال جديد يتطلب التفسير.
- عند الحاجة: وقت إضافي والتحقق من الفهم دون إحراج.

عاشرًا: الخلاصة والواجب
يلخص التلاميذ ما تعلموه بجملة أو مثال، ثم يكلفهم المعلم بتدريب قصير مناسب للدرس من الكتاب عند توفره أو من إعداد المعلم.

إعداد: معلم دمج
بالتعاون مع أبو محمد وأبو عبدالله وعصام
منصة معلمي الجزيرة السورية
""".trimIndent()
}

private fun printPlan(context: Context, text: String) {
    val webView = WebView(context)
    val escaped = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>")
    webView.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            val manager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            manager.print("تحضير الدرس", view.createPrintDocumentAdapter("تحضير الدرس"), PrintAttributes.Builder().build())
        }
    }
    webView.loadDataWithBaseURL(null, "<html><meta charset=\"utf-8\"><body dir=\"rtl\" style=\"font-family:sans-serif;padding:24px;line-height:1.8;font-size:14px\"><h2>دفتر التحضير الإلكتروني</h2>$escaped</body></html>", "text/html", "UTF-8", null)
}
