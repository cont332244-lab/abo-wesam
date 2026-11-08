package com.moallem.moudamej.lessonplanner

internal fun generateEnglishOfflinePlan(
    grade: String,
    subject: String,
    lesson: String,
    method: String,
    duration: String,
    notes: String
): String {
    val gradeEn = when (grade) {
        "الصف الأول" -> "Grade 1"; "الصف الثاني" -> "Grade 2"; "الصف الثالث" -> "Grade 3"
        "الصف الرابع" -> "Grade 4"; "الصف الخامس" -> "Grade 5"; "الصف السادس" -> "Grade 6"
        else -> grade
    }
    val subjectEn = when (subject) {
        "اللغة العربية" -> "Arabic"; "الرياضيات" -> "Mathematics"; "اللغة الإنكليزية" -> "English"
        "العلوم" -> "Science"; "الاجتماعيات" -> "Social Studies"; "التربية الإسلامية" -> "Islamic Education"
        "الفنون" -> "Arts"; "الرياضة" -> "Physical Education"; "أخرى" -> "Other"
        else -> subject
    }
    val methodEn = when (method) {
        "التعلّم النشط" -> "Active Learning"; "التعلّم بالاكتشاف" -> "Discovery Learning"
        "التعلّم التعاوني" -> "Cooperative Learning"; "الطريقة الاستقرائية" -> "Inductive Method"
        "الطريقة الاستنتاجية" -> "Deductive Method"; "حلّ المشكلات" -> "Problem-Based Learning"
        "الحوار والمناقشة" -> "Discussion"; "العصف الذهني" -> "Brainstorming"
        "التعلّم باللعب" -> "Game-Based Learning"; "التعلّم بالمشروعات" -> "Project-Based Learning"
        "التعليم المتمايز" -> "Differentiated Instruction"; "الشرح والنمذجة" -> "Direct Instruction and Modelling"
        "التعلّم بالاستقصاء" -> "Inquiry-Based Learning"; else -> method
    }
    val strategy = when (method) {
        "التعلّم بالاكتشاف" -> "Present examples and guide pupils to observe, compare, and discover the main idea."
        "التعلّم التعاوني" -> "Arrange pupils in small groups, assign roles, and let each group complete and present a task."
        "حلّ المشكلات" -> "Present a problem related to the lesson and guide pupils to suggest and discuss solutions."
        "الحوار والمناقشة" -> "Use focused questions, allow thinking time, and encourage pupils to explain their answers."
        "العصف الذهني" -> "Invite pupils to suggest ideas, then organize and discuss them."
        "التعلّم باللعب" -> "Use a short educational game that practises the lesson objectives."
        "الطريقة الاستقرائية" -> "Show examples and guide pupils to notice patterns and formulate the rule."
        "الطريقة الاستنتاجية" -> "Explain the rule, model examples, and guide pupils to apply it."
        "التعليم المتمايز" -> "Use tiered tasks that respond to pupils' different learning needs."
        "الشرح والنمذجة" -> "Explain and model the skill step by step, then guide pupils through practice."
        else -> "Guide pupils from presentation to guided practice and independent application."
    }
    val aids = when (subject) {
        "اللغة الإنكليزية" -> "Coursebook, board, flashcards, pictures, word cards, and audio materials when available."
        "الرياضيات" -> "Coursebook, board, number cards, counters, and shapes."
        "العلوم" -> "Coursebook, board, pictures, safe real objects, and simple observation materials."
        else -> "Coursebook, board, markers, pictures, flashcards, and suitable classroom materials."
    }
    val presentation = when (subject) {
        "اللغة الإنكليزية" -> """1. Introduce the key words or language structure using pictures, gestures, and clear modelling.
2. Present the target language and give examples. Ask pupils to listen, repeat, and identify the meaning.
3. Practise pronunciation and meaning as a whole class, then in pairs.
4. Guide pupils to use the new language in sentences or a short dialogue.
5. Monitor participation, correct errors positively, and give pupils a short independent task."""
        "الرياضيات" -> """1. Connect the new concept to prior learning or a familiar real-life example.
2. Explain the steps and model a worked example on the board.
3. Solve another example with pupils and ask them to explain each step.
4. Give pupils graded individual or group exercises.
5. Review answers, address common mistakes, and provide feedback."""
        "اللغة العربية" -> """1. Present a short text or example related to the lesson.
2. Read the example and identify the target skill or idea.
3. Discuss the examples and explain the rule or skill with pupils.
4. Complete graded oral and written exercises.
5. Monitor pupils' work, correct errors, and reinforce correct answers."""
        "العلوم" -> """1. Introduce the topic with a question, picture, or safe real object.
2. Explain the concept and model careful observation.
3. Guide pupils to describe, compare, and record what they observe.
4. Let pupils complete an activity and discuss their findings.
5. Link the findings to the lesson objectives and correct misconceptions."""
        else -> """1. Introduce the topic and connect it to pupils' prior knowledge.
2. Explain the main points using clear examples and teacher modelling.
3. Ask guided questions and involve pupils in discussion.
4. Let pupils complete an individual or group task.
5. Review pupils' responses and provide immediate feedback."""
    }
    val noteText = if (notes.isBlank()) "No additional notes." else notes
    val englishPlan = """
LESSON PLAN

Subject: $subjectEn
Class: $gradeEn
Book: ........................................
Unit: ........................................
Page: ........................................
Date: ......... / ......... / ............

LEARNING OUTCOMES
By the end of the lesson, pupils will be able to:
1. Identify the main idea of "$lesson".
2. Explain the key information or skill presented in the lesson.
3. Apply the new learning in a suitable activity.
4. Answer questions related to the lesson accurately.

FORMATIVE ASSESSMENT
1. What is the main idea of "$lesson"?
2. Can the pupil explain or demonstrate the target skill?
3. Complete one example or task independently.
4. State one thing you have learned from the lesson.

TEACHING AIDS
$aids

STRATEGIES / METHODS
$methodEn: $strategy

WARM-UP — 5 m
Greet pupils and check readiness. Review previous learning with two or three short questions, then introduce "$lesson" through a motivating question or picture.

PRESENTATION (ACTIVITIES AND PROCEDURES) — 35 m
$presentation

ACTIVITIES FOR OUTSTANDING STUDENTS
Ask pupils to create their own examples, explain their reasoning, or complete an extension task that applies the learning in a new situation.

ACTIVITIES FOR UNDERACHIEVERS
Use pictures and flashcards, short instructions, a worked example, small task steps, guided practice, and additional support.

EVALUATION — 5 m
Ask pupils to complete a short individual task or answer oral questions linked to the objectives. Check responses, identify difficulties, and provide immediate feedback.

HOMEWORK
Complete a short practice task related to "$lesson" in the coursebook or notebook.
Teacher's notes: $noteText

CLOSURE
Ask pupils to state one thing they learned today. Review the main idea, praise participation, and explain the homework.

SELF-EVALUATION
Were the learning outcomes achieved? Which pupils need further support? Were the activities and timing effective? What should be improved in the next lesson?

Headmaster: ........................................
Supervisor Signature: ........................................
""".trimIndent()

    val arabicTranslation = """
الترجمة العربية للتحضير

خطة درس

المادة: $subject
الصف: $grade
الكتاب: ........................................
الوحدة: ........................................
الصفحة: ........................................
التاريخ: ......... / ......... / ............

الأهداف التعليمية
في نهاية الدرس، يُتوقع أن يكون التلاميذ قادرين على:
1. تحديد الفكرة الرئيسية لدرس "$lesson".
2. شرح المعلومات أو المهارة الأساسية المقدمة في الدرس.
3. تطبيق ما تعلموه في نشاط مناسب.
4. الإجابة بدقة عن الأسئلة المتعلقة بالدرس.

التقويم التكويني
1. ما الفكرة الرئيسية لدرس "$lesson"؟
2. هل يستطيع التلميذ شرح المهارة المستهدفة أو توضيحها؟
3. نفّذ مثالًا أو مهمة بصورة مستقلة.
4. اذكر أمرًا واحدًا تعلمته من الدرس.

الوسائل التعليمية
$aids

الاستراتيجيات / طرائق التدريس
$method: $strategy

التمهيد — 5 دقائق
تحية التلاميذ والتأكد من جاهزيتهم. مراجعة التعلم السابق بسؤالين أو ثلاثة، ثم تقديم درس "$lesson" من خلال سؤال محفز أو صورة مناسبة.

العرض (الأنشطة والإجراءات) — 35 دقيقة
$presentation

أنشطة التلاميذ المتفوقين
تكليف التلاميذ بإعداد أمثلة من إنشائهم، أو شرح طريقة تفكيرهم، أو تنفيذ نشاط إثرائي يوظف التعلم في موقف جديد.

أنشطة التلاميذ المتعثرين
استخدام الصور والبطاقات والتعليمات القصيرة، وتقديم مثال محلول، وتقسيم المهمة إلى خطوات صغيرة، مع التدريب الموجه والدعم الإضافي.

التقويم — 5 دقائق
تكليف التلاميذ بمهمة فردية قصيرة أو الإجابة عن أسئلة شفوية مرتبطة بالأهداف. مراجعة الإجابات، وتحديد الصعوبات، وتقديم تغذية راجعة فورية.

الواجب المنزلي
حل تدريب قصير مرتبط بدرس "$lesson" في الكتاب أو الدفتر.
ملاحظات المعلم: $notes

الإغلاق
يذكر التلاميذ أمرًا واحدًا تعلموه اليوم. مراجعة الفكرة الرئيسية، وتعزيز المشاركة، وتوضيح الواجب المنزلي.

التقييم الذاتي
هل تحققت الأهداف التعليمية؟ من التلاميذ الذين يحتاجون إلى دعم إضافي؟ هل كانت الأنشطة والزمن فعالين؟ ما الذي ينبغي تحسينه في الدرس القادم؟

مدير المدرسة: ........................................
توقيع المشرف: ........................................
""".trimIndent()

    return "$englishPlan\\n\\n----------------------------------------\\nARABIC TRANSLATION / الترجمة العربية\\n----------------------------------------\\n\\n$arabicTranslation"
}
