package com.moallem.moudamej.lessonplanner

internal fun generateEnglishOfflinePlan(
    grade: String,
    subject: String,
    lesson: String,
    method: String,
    duration: String,
    notes: String
): String {
    val noteText = if (notes.isBlank()) "No additional notes." else notes
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
    val methodText = when (method) {
        "التعلّم بالاكتشاف" -> "Present examples and guide pupils to observe, compare, and discover the key idea before discussing their conclusions."
        "التعلّم التعاوني" -> "Organize pupils into small groups with clear roles. Give each group a task and invite them to share their findings."
        "حلّ المشكلات" -> "Present a lesson-related problem and guide pupils to suggest, test, and discuss possible solutions."
        "الحوار والمناقشة" -> "Ask scaffolded questions, allow thinking time, encourage pupils to explain their ideas, and address misconceptions."
        "العصف الذهني" -> "Invite pupils to share ideas about a key question, then organize and discuss their suggestions."
        "التعلّم باللعب" -> "Use a short educational game connected to the learning objective, then discuss what pupils learned."
        "الطريقة الاستقرائية" -> "Provide varied examples and guide pupils to identify patterns and formulate the rule."
        "الطريقة الاستنتاجية" -> "Explain the rule, model worked examples, and guide pupils in applying it to new examples."
        "التعليم المتمايز" -> "Provide tiered tasks: additional scaffolding for pupils who need support, a core task for all, and an extension task for advanced pupils."
        "الشرح والنمذجة" -> "Model the skill step by step, think aloud through an example, and then guide pupils towards independent practice."
        else -> "Introduce the concept through a question or example, explain it in manageable steps, and move from guided to independent practice."
    }
    val activity = when (subject) {
        "الرياضيات" -> "Model one example on the board, then provide graduated problems and ask pupils to explain their solutions."
        "اللغة العربية" -> "Read a short example, identify the target language skill, and practise it orally and in written sentences."
        "اللغة الإنكليزية" -> "Practise the target vocabulary or language structure, then use it in meaningful sentences or a short dialogue."
        "العلوم" -> "Observe a safe example or phenomenon, record observations, and connect them to the target scientific concept."
        "الاجتماعيات" -> "Use a short text, map, or picture. Ask pupils to locate information, explain it, and draw simple conclusions."
        "التربية الإسلامية" -> "Discuss the key value or idea and connect it to an appropriate everyday situation."
        "الرياضة" -> "Begin with a suitable warm-up, then practise the skill progressively while observing safety rules."
        else -> "Complete a short practical task related to the lesson title and discuss the results."
    }
    return """
LESSON PLAN

1. LESSON INFORMATION
Grade: $grade
Subject: $subject
Lesson title: $lesson
Teaching method: $method
Lesson duration: $duration
Teacher notes: $noteText

2. LEARNING OBJECTIVES
By the end of the lesson, pupils should be able to:
1. Explain the main idea or skill related to $lesson.
2. Identify key elements of the lesson.
3. Apply what they have learned in an age-appropriate example.
4. Answer assessment questions and correct their mistakes.

3. PRIOR KNOWLEDGE
Review relevant previous learning through brief questions and identify any concepts that need clarification.

4. WARM-UP AND MOTIVATION
Ask a short introductory question or show a picture or familiar real-life example related to the lesson. Explain what pupils are expected to learn.

5. TEACHING AIDS
Board and markers, textbook when available, flashcards, pictures, examples, and suitable classroom materials.

6. TEACHING AND LEARNING PROCEDURES
1. Starter and review — 5 minutes: Activate prior knowledge and ask an introductory question.
2. Presentation — 10 minutes: $methodText
3. Guided practice — 10 minutes: Complete an example together and check pupils' understanding.
4. Practical activity — 15 minutes: $activity
5. Assessment and closure — 5 minutes: Ask short review questions and summarize the main idea.

Timing note: This is a sample structure for a 45-minute lesson. Adjust it to the actual lesson duration ($duration).

7. TEACHER AND PUPIL ROLES
Teacher: Organizes learning, gives clear instructions, asks questions, monitors participation, and provides feedback.
Pupils: Observe, discuss, take part in activities, explain their thinking, and review their answers.

8. ASSESSMENT
1. What is the main idea of $lesson?
2. Give an example that shows what you have learned.
3. Which part would you like to practise more?
Adapt these questions to the exact lesson content. This offline plan is generated from the lesson title and does not reproduce a specific textbook passage.

9. DIFFERENTIATION AND INCLUSION
- Pupils who need support: Give short instructions, a worked example, visual prompts, and tasks divided into small steps.
- Core learning: Provide the main task with teacher monitoring.
- Advanced pupils: Offer an extension question or a new example that requires explanation.
- Where needed: Allow additional time and check understanding discreetly and positively.

10. SUMMARY AND HOMEWORK
Ask pupils to summarize the lesson in one sentence or give one example. Set a short, relevant practice task from the textbook, when available, or prepared by the teacher.

Prepared by: Teacher
In collaboration with Abu Mohammad, Abu Abdullah, and Essam
Syrian Jazira Teachers Platform
""".trimIndent()
}
