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
import androidx.activity.compose.setContent
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
    var grade by remember { mutableStateOf(grades[5]) }
    var subject by remember { mutableStateOf(subjects[0]) }
    var lesson by remember { mutableStateOf("") }
    var method by remember { mutableStateOf(teachingMethods[0]) }
    var duration by remember { mutableStateOf("45 دقيقة") }
    var notes by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("يتطلب التوليد مفتاح Google Gemini API. لا يُحفظ المفتاح داخل التطبيق.") }

    Column(Modifier.fillMaxSize().background(Pale).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text("📚 دفتر التحضير الإلكتروني", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("تحضير مخصص للصف والمادة وطريقة التدريس", color = Color(0xFFD9E7F5), fontSize = 14.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("بيانات الدرس", fontSize = 20.sp, color = Navy, fontWeight = FontWeight.Bold)
        PickerField("الصف", grade, grades) { grade = it }
        PickerField("المادة", subject, subjects) { subject = it }
        PickerField("طريقة التدريس", method, teachingMethods) { method = it }
        InputField("عنوان الدرس", lesson, { lesson = it }, "مثال: الكسور المتكافئة")
        InputField("مدة الحصة", duration, { duration = it }, "45 دقيقة")
        InputField("ملاحظات إضافية (اختياري)", notes, { notes = it }, "مستوى التلاميذ أو أهداف محددة", 2)
        Spacer(Modifier.height(12.dp))
        Text("الاتصال بالذكاء الاصطناعي", fontSize = 18.sp, color = Navy, fontWeight = FontWeight.Bold)
        Text("احصل على مفتاح Gemini من aistudio.google.com/app/apikey. يُرسل المفتاح إلى Google لتوليد التحضير ولا يُحفظ داخل التطبيق.", color = Ink, fontSize = 13.sp, lineHeight = 20.sp)
        OutlinedTextField(value = apiKey, onValueChange = { apiKey = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Gemini API Key") }, placeholder = { Text("ألصق المفتاح هنا") },
            visualTransformation = PasswordVisualTransformation(), singleLine = true, shape = RoundedCornerShape(13.dp))
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            when {
                lesson.isBlank() -> message = "يرجى إدخال عنوان الدرس أولاً."
                apiKey.isBlank() -> message = "أدخل مفتاح Gemini API لتوليد التحضير عبر الإنترنت."
                else -> {
                    busy = true
                    message = "جارٍ إعداد التحضير بالذكاء الاصطناعي..."
                    thread {
                        val result = generatePlan(apiKey.trim(), grade, subject, lesson.trim(), method, duration, notes)
                        Handler(Looper.getMainLooper()).post {
                            busy = false
                            if (result.startsWith("ERROR:")) message = result.removePrefix("ERROR:")
                            else { plan = result; message = "اكتمل التحضير. راجعه وعدّله ثم انسخه أو اطبعه." }
                        }
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth().height(54.dp), enabled = !busy,
            colors = ButtonDefaults.buttonColors(containerColor = Teal), shape = RoundedCornerShape(15.dp)) {
            if (busy) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
            else Text("✨ إنشاء التحضير بالذكاء الاصطناعي", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
        Text("إعداد: معلم دمج", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Teal, fontWeight = FontWeight.Bold)
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

private fun generatePlan(apiKey: String, grade: String, subject: String, lesson: String, method: String, duration: String, notes: String): String {
    var connection: HttpURLConnection? = null
    return try {
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + URLEncoder.encode(apiKey, "UTF-8")
        connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"; connectTimeout = 20000; readTimeout = 60000; doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        }
        val prompt = """
أنت خبير في طرائق التدريس وتصميم الدروس للمرحلة الأساسية. أنشئ تحضيرًا عمليًا مفصلًا باللغة العربية، مناسبًا للصف والعمر، وليس قالبًا عامًا.
الصف: $grade
المادة: $subject
عنوان الدرس: $lesson
طريقة التدريس المطلوبة: $method
مدة الحصة: $duration
ملاحظات المعلم: $notes

اكتب التحضير بعناوين واضحة:
1. معلومات الدرس.
2. نواتج التعلم/الأهداف السلوكية القابلة للقياس.
3. الخبرات السابقة.
4. التمهيد وإثارة الدافعية.
5. الوسائل التعليمية.
6. خطوات سير الدرس بتوزيع زمني يساوي مدة الحصة، مع دور المعلم ودور التلاميذ.
7. نشاط تطبيقي فردي وتفاعلي.
8. تقويم تكويني وأسئلة ختامية مع إجابات نموذجية.
9. مراعاة الفروق الفردية ودعم المتعثرين وإثراء المتقدمين.
10. الخلاصة والواجب المنزلي.
اجعل الأنشطة والتقويم متوافقة مع طريقة التدريس المختارة وقابلة للتطبيق في صف حقيقي. لا تفترض صفحات كتاب أو معلومات منهجية غير مؤكدة. إذا كان العنوان غير كافٍ فاذكر افتراضك باختصار. لا تضف مقدمة خارج التحضير.
""".trimIndent()
        val request = JSONObject()
            .put("contents", JSONArray().put(JSONObject().put("parts", JSONArray().put(JSONObject().put("text", prompt)))))
            .put("generationConfig", JSONObject().put("temperature", 0.7).put("maxOutputTokens", 7000))
        OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { it.write(request.toString()) }
        val code = connection.responseCode
        val stream = if (code in 200..299) connection.inputStream else connection.errorStream
        val response = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        if (code !in 200..299) {
            val detail = try { JSONObject(response).optJSONObject("error")?.optString("message") ?: response.take(400) } catch (_: Exception) { response.take(400) }
            "ERROR:تعذّر إنشاء التحضير (HTTP $code): $detail"
        } else {
            val parts = JSONObject(response).optJSONArray("candidates")?.optJSONObject(0)?.optJSONObject("content")?.optJSONArray("parts")
            val output = if (parts == null) "" else (0 until parts.length()).mapNotNull { parts.optJSONObject(it)?.optString("text") }.joinToString("\n")
            if (output.isBlank()) "ERROR:لم يصل نص التحضير. تحقق من المفتاح وحاول مجددًا." else output
        }
    } catch (e: Exception) {
        "ERROR:تعذّر الاتصال. تحقق من الإنترنت ومفتاح Gemini API. التفاصيل: " + (e.message ?: "خطأ غير معروف")
    } finally { connection?.disconnect() }
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
