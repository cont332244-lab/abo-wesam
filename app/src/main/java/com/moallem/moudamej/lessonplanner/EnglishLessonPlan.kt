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
        "حلّ المشكلات" -> "Present a problem related to the lesson and guide pupils to suggest and test solutions."
        "الحوار والمناقشة" -> "Use focused questions, allow thinking time, and encourage pupils to explain their answers."
        "العصف الذهني" -> "Invite pupils to suggest ideas, record their responses, and organize them together."
        "التعلّم باللعب" -> "Use a short educational game that practises the target learning outcomes."
        "الطريقة الاستقرائية" -> "Show examples and guide pupils to notice patterns and formulate the rule."
        "الطريقة الاستنتاجية" -> "Explain the rule, model examples, and guide pupils to apply it."
        "التعليم المتمايز" -> "Use tiered tasks and adjust support and challenge according to pupils' needs."
        "الشرح والنمذجة" -> "Explain and model the skill step by step, then guide pupils through practice."
        else -> "Use $method, moving from teacher modelling to guided practice and independent pupil participation."
    }
    val aids = when (subject) {
        "اللغة الإنكليزية" -> "Coursebook, board, flashcards, pictures, word cards, and audio materials when available."
        "الرياضيات" -> "Coursebook, board, number cards, counters, shapes, and concrete learning materials."
        "العلوم" -> "Coursebook, board, pictures, safe real objects, and simple observation materials."
        else -> "Coursebook, board, markers, pictures, flashcards, and suitable classroom materials."
    }
    val presentation = when (subject) {
        "اللغة الإنكليزية" -> """1. Introduce the key words or language structure using pictures, gestures, and clear modelling.
2. Present the target language and give examples. Ask pupils to listen, repeat, and identify the meaning.
3. Practise pronunciation and meaning as a whole class, then in pairs or small groups.
4. Guide pupils to use the new language in sentences, questions, or a short dialogue.
5. Monitor participation, correct errors positively, and give pupils a short independent task."""
        "الرياضيات" -> """1. Connect the new concept to a familiar example and explain the steps.
2. Model a worked example on the board, thinking aloud.
3. Solve a second example with pupils and ask them to explain each step.
4. Give pairs or groups graded exercises, then invite pupils to share solutions.
5. Check understanding and address common mistakes."""
        "العلوم" -> """1. Introduce the scientific idea with a question, picture, or safe real object.
2. Explain the concept and model careful observation.
3. Guide pupils to describe, compare, and record what they observe.
4. Let pupils complete a short activity and discuss their findings.
5. Link the findings to the lesson outcome and correct misconceptions."""
        else -> """1. Introduce the topic and connect it to pupils' prior knowledge.
2. Explain the main points using clear examples and teacher modelling.
3. Ask guided questions and involve pupils in discussion.
4. Let pupils complete an individual or group task related to the lesson.
5. Review pupils' responses and provide immediate feedback."""
    }
    val outcome = """1. Identify the main idea of "$lesson".
2. Explain the key information or skill presented in the lesson.
3. Apply the new learning in an appropriate activity.
4. Answer questions related to the lesson accurately."""
    val assessment = """1. What is the main idea of "$lesson"?
2. Can you explain or demonstrate the target skill?
3. Complete one example or task independently.
4. Give one answer that shows what you have learned."""
    val advanced = "Ask pupils to write their own examples, explain their reasoning, or complete an extra challenge task related to $lesson."
    val support = "Use pictures, word or number cards, short instructions, a worked example, pair support, and extra guided practice."
    val homework = if (notes.isBlank()) "Complete a short practice task related to $lesson in the coursebook or in the notebook." else "Complete a short practice task related to $lesson. Teacher's note: $notes"
    return """
LESSON PLAN

Subject: $subjectEn
Class: $gradeEn
Book: ........................................
Unit: ........................................
Page: ........................................
Date: ......... / ......... / ............

LEARNING OUTCOMES
By the end of the lesson, the students will be able to:
$outcome

FORMATIVE ASSESSMENT
$assessment

TEACHING AIDS
$aids

STRATEGIES / METHODS
$methodEn: $strategy

WARM-UP — 5 m
Greet pupils, check readiness, review prior learning with two or three short questions, and introduce "$lesson" through a motivating question or picture.

PRESENTATION (ACTIVITIES AND PROCEDURES) — 35 m
$presentation

ACTIVITIES FOR OUTSTANDING STUDENTS
$advanced

ACTIVITIES FOR UNDERACHIEVERS
$support

EVALUATION — 5 m
Ask pupils to complete a short individual task or answer oral questions based on the learning outcomes. Check answers, identify difficulties, and give immediate feedback.

HOMEWORK
$homework

CLOSURE
Ask pupils to state one thing they learned today. Review the lesson's main point, praise participation, and explain the homework.

SELF-EVALUATION
Were the learning outcomes achieved? Which pupils need further support? Were the activities and timing effective? What should be improved in the next lesson?

Headmaster: ........................................
Supervisor Signature: ........................................
""".trimIndent()
}
