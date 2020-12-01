import pytest

import criterion
from course import Student, Course, sort_students
from criterion import HomogeneousCriterion, \
    HeterogeneousCriterion, LonelyMemberCriterion
from grouper import Grouping, AlphaGrouper, RandomGrouper, \
    GreedyGrouper, WindowGrouper, Group, slice_list, windows
from survey import Answer, Survey, MultipleChoiceQuestion, \
    NumericQuestion, YesNoQuestion, CheckboxQuestion

class TestStudentClass:

    def test_str_method(self) -> None:
        """ The test will test whether student class __str__method will print a
        correct name of not.
        """

        student = Student(520, 'Zara')

        assert student.__str__() == 'Zara'

    def test_str_method2(self) -> None:
        """ The test will test whether student class __str__method will print a
        correct name of not.
        """

        student2 = Student(148, 'Roy')

        assert student2.__str__() == 'Roy'

    def test_has_answer(self) -> None:
        """ The test tests whether student class has_answer method will a student has
        answer with same id whit the question and the answer is valid.
        """

        question = MultipleChoiceQuestion(1, 'Choose one', ['a', 'b'])
        answer = Answer('a')
        student = Student(520, 'Zara')
        student.set_answer(question, answer)

        assert student.has_answer(question)

    def test_has_answer2(self) -> None:
        """ The test tests whether student class has_answer method will a student has
        answer with same id whit the question and the answer is valid.
        """

        question = YesNoQuestion(1, 'Yes or No')
        answer = Answer(True)
        student = Student(520, 'Zara')
        student.set_answer(question, answer)

        assert student.has_answer(question)

    def test_set_answer(self) -> None:
        """
        The test tests whether this student has the answer to the question.
        """

        student = Student(520, 'Zara')
        question = YesNoQuestion(1, 'Yes or No')
        answer = Answer(True)
        student.set_answer(question, answer)

        assert question.id in student._answer

    def test_set_answer2(self) -> None:
        """
        The test tests whether this student has the answer to the question.
        """

        student2 = Student(148, 'Roy')
        question2 = MultipleChoiceQuestion(999, 'Choose One', ['ha', 'la', 'ma'])
        answer = Answer('ha')
        student2.set_answer(question2, answer)

        assert question2.id in student2._answer

    def test_get_answer(self) -> None:
        """
        The test tests whether the answer is in  the question or not.
        """

        student = Student(520, 'Zara')
        question = YesNoQuestion(1, 'Yes or No')
        answer = Answer(True)
        student.set_answer(question, answer)

        assert id(student.get_answer(question)) == id(answer)

    def test_get_answer2(self) -> None:
        """
        The test tests whether the answer is in  the question or not.
        """

        student2 = Student(148, 'Roy')
        question2 = MultipleChoiceQuestion(999, 'Choose One', ['ha', 'la', 'ma'])
        answer = Answer('ha')
        student2.set_answer(question2, answer)

        assert id(student2.get_answer(question2)) == id(answer)


class TestCourseClass:

    def test_enroll_students(self) -> None:
        """
        The test tests weather all students enroll into this course ot not.
        """

        lecture = Course('CSC148')
        student1 = Student(1, 'Roy')
        student2 = Student(2, 'Martin')
        students = [student1, student2]
        lecture.enroll_students(students)

        assert student1 in lecture.students and student2 in lecture.students

    def test_enroll_students_violate_ri(self) -> None:
        """
        The test tests weather all students enroll into this course ot not.
        """

        lecture = Course('CSC148')
        student1 = Student(1, '')
        student2 = Student(2, 'Martin')
        students = [student1, student2]
        lecture.enroll_students(students)

        assert student1 not in lecture.students and student2 not in lecture.students

    def test_all_answered(self) -> None:
        """ The test tests all the students enrolled in this course have a valid
        answer for every question or not.
        """

        lecture = Course('CSC148')
        questions = [MultipleChoiceQuestion(1, 'Choose One', ['a', 'b']), YesNoQuestion(2, 'YesOrNo')]
        survey1 = Survey(questions)
        s1 = Student(1, 'Roy')
        s2 = Student(2, 'Martin')
        lecture.enroll_students([s1, s2])
        s1.set_answer(MultipleChoiceQuestion(1, 'Choose One', ['a', 'b']), Answer('a'))
        s1.set_answer(YesNoQuestion(2, 'YesOrNo'), Answer(True))
        s2.set_answer(MultipleChoiceQuestion(1, 'Choose One', ['a', 'b']), Answer('b'))
        s2.set_answer(YesNoQuestion(2, 'YesOrNo'), Answer(False))

        assert lecture.all_answered(survey1)

    def test_get_students(self) -> None:
        """ The test tests whether this method gives a
        correct increasing order of students ids.
        """

        lecture = Course('CSC148')
        student1 = Student(1, 'Roy')
        student2 = Student(2, 'Martin')
        student3 = Student(999, 'Aaron')
        student4 = Student(520, 'Zara')
        students = [student1, student2, student3, student4]
        lecture.enroll_students(students)

        assert list(lecture.get_students()) == [student1, student2, student4, student3]


class TestMultipleChoiceQuestionClass:

    def test___str__for_multiple(self) -> None:
        """ The test tests whether method gives a string representation of this question including the
        text of the question and a description of the possible answers.
        """

        question = MultipleChoiceQuestion(1, 'choose one', ['AA', 'BB'])

        assert 'why?' not in str(question)
        assert 'choose one' in str(question)

    def test_validate_answer_for_multiple(self) -> None:
        """ The test tests whether this method correctly gives the content of answers is an integer
        between the minimum and maximum (inclusive) possible answers to this question.
        """

        question = MultipleChoiceQuestion(1, 'choose one', ['AA', 'BB'])
        answers = Answer('AA')
        assert question.validate_answer(answers)

    def test_validate_answer_for_multiple2(self) -> None:
        """ The test tests whether this method correctly gives the content of answers is an integer
        between the minimum and maximum (inclusive) possible answers to this question.
        """

        question = MultipleChoiceQuestion(1, 'choose one', ['AA', 'BB'])
        answers = Answer('bb')

        assert not question.validate_answer(answers)

    def test_get_similarity_for_multiple_0(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range of possible answers to this question.
        """

        question = MultipleChoiceQuestion(1, 'choose one', ['AA', 'BB'])
        answers = Answer('bb')
        answers2 = Answer('AA')

        assert question.get_similarity(answers, answers2) == 0.0

    def test_get_similarity_for_multiple_1(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range of possible answers to this question.
        """

        question = MultipleChoiceQuestion(1, 'choose one', ['AA', 'BB'])
        answers = Answer('AA')
        answers2 = Answer('AA')

        assert question.get_similarity(answers, answers2) == 1.0


class TestNumericQuestionClass:

    def test___str__for_numerical(self) -> None:
        """ The test tests whether method gives a string representation of this question including the
        text of the question and a description of the possible answers.
        """

        question = NumericQuestion(1, 'num game', 0, 100)

        assert '?' not in str(question)
        assert 'roy-nimasile' not in str(question)
        assert 'num game' in str(question)

    def test_validate_answer_for_numerical(self) -> None:
        """ The test tests whether this method correctly gives the content of answers is an integer
        between the minimum and maximum (inclusive) possible answers to this question.
        """

        question = NumericQuestion(1, 'num game', 0, 100)
        answers = Answer(50)

        assert question.validate_answer(answers)

    def test_validate_answer_for_numerical2(self) -> None:
        """ The test tests whether this method correctly gives the content of answers is an integer
        between the minimum and maximum (inclusive) possible answers to this question.
        """

        question = NumericQuestion(1, 'num game', 0, 100)
        answers = Answer(-1)

        assert not question.validate_answer(answers)

    def test_get_similarity_for_numerical_is_valid_1(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range between the maximum and minimum of the range given in the answer.
        """

        question = NumericQuestion(1, 'num game', 0, 100)
        answers = Answer(0)
        answers2 = Answer(100)

        assert question.get_similarity(answers, answers2) == 0.0

    def test_get_similarity_for_numerical_is_valid(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range between the maximum and minimum of the range given in the answer.
        """

        question = NumericQuestion(1, 'num game', 0, 200)
        answers = Answer(50)
        answers2 = Answer(100)
        answers3 = Answer(100)

        assert question.get_similarity(answers, answers2) == 0.75
        assert question.get_similarity(answers2, answers3) == 1.0


class TestYesOrN0QuestionClass:

    def test___str__for_yes_or_no(self) -> None:
        """ The test tests whether method gives a string representation of this question including the
        text of the question and a description of the possible answers.
        """

        question = YesNoQuestion(998, 'is True or not')

        assert 'lucky day' not in str(question)
        assert 'i love computer science' not in str(question)
        assert 'is True or not' in str(question)

    def test_validate_answer_for_yes_or_no(self) -> None:
        """ The test tests whether this method correctly gives a boolean type answer
        for this question.
        """

        question = YesNoQuestion(998, 'is True or not')
        answer = Answer('HALLO')
        answer2 = Answer(True)
        answer3 = Answer(False)
        answer4 = Answer(558)

        assert question.validate_answer(answer2)
        assert not question.validate_answer(answer)
        assert question.validate_answer(answer3)
        assert not question.validate_answer(answer4)

    def test_get_similarity_for_yes_or_no(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range between True and Fasle.
        """

        question = YesNoQuestion(998, 'is True or not')
        answer1 = Answer(True)
        answer2 = Answer(True)
        answer3 = Answer(False)
        answer4 = Answer(False)

        assert question.get_similarity(answer1, answer2) == 1.0
        assert question.get_similarity(answer2, answer3) == 0.0
        assert question.get_similarity(answer3, answer4) == 1.0
        assert question.get_similarity(answer1, answer4) == 0.0


class TestCheckBoxQuestion:

    def test___str__for_checkbox(self) -> None:
        """ The test tests whether method gives a string representation of this question including the
        text of the question and a description of the possible answers.
        """

        question = CheckboxQuestion(778, 'checkbox question', ['hall', 'computer', 'science'])
        assert 'why' not in str(question)
        assert 'computer science' not in str(question)
        assert 'is so funny' not in str(question)
        assert 'checkbox question' in str(question)
        assert 'hall' in str(question)
        assert 'computer' in str(question)
        assert 'science' in str(question)

    def test_validate_answer_for_checkbox(self) -> None:
        """ The test tests whether this method correctly gives a boolean type answer
        for this question.
        """

        question = CheckboxQuestion(778, 'checkbox question', ['hall', 'computer', 'science'])
        answer = Answer('hall')
        answer2 = Answer('computer')
        answer3 = Answer('')
        answer4 = Answer(['hall', 'computer', 'science'])
        answer5 = Answer(['hall', 'computer', 'hall'])
        answer6 = Answer(['hall'])

        assert not question.validate_answer(answer)
        assert not question.validate_answer(answer2)
        assert not question.validate_answer(answer3)
        assert not question.validate_answer(answer5)
        assert question.validate_answer(answer4)
        assert question.validate_answer(answer6)

    def test_get_similarity_for_checkbox(self) -> None:
        """ The test tests whether this method gives a correct float of the similarity between
        answers over the range of a list of answers.
        """

        question = CheckboxQuestion(778, 'checkbox question', ['hall', 'computer', 'science'])
        answer = Answer('hall')
        answer2 = Answer('computer')
        answer3 = Answer('')
        answer4 = Answer(['hall', 'computer', 'science'])
        answer5 = Answer(['hall', 'computer', 'hall'])
        answer7 = Answer(['hall', 'computer', 'science'])

        assert question.get_similarity(answer, answer2) == 0.0
        assert question.get_similarity(answer2, answer3) == 0.0
        assert question.get_similarity(answer5, answer4) == 2 / 3
        assert question.get_similarity(answer, answer4) == 0.0
        assert question.get_similarity(answer7, answer4) == 1.0


class TestAnswerClass:

    def test_is_valid(self) -> None:
        """ The test tests whether the answer is valid or not.
        """

        answer1 = Answer('happy')
        answer6 = Answer('not happy')
        answer2 = Answer(True)
        answer3 = Answer(False)
        answer4 = Answer(66)
        answer5 = Answer(['AA', 'BB'])
        answer7 = Answer(1.333)

        question1 = MultipleChoiceQuestion(1, 'CHOOSE', ['happy', 'sad'])
        question2 = NumericQuestion(2, 'NUM', 0, 100)
        question3 = YesNoQuestion(3, 'YES OR NO')
        question4 = CheckboxQuestion(4, 'CHECK CHECK', ['AA', 'BB', 'CC'])

        assert answer1.is_valid(question1)
        assert not answer6.is_valid(question1)
        assert answer2.is_valid(question3)
        assert answer3.is_valid(question3)
        assert not answer1.is_valid(question3)
        assert answer4.is_valid(question2)
        assert not answer4.is_valid(question1)
        assert not answer7.is_valid(question2)
        assert answer5.is_valid(question4)
        assert not answer5.is_valid(question2)


class TestHomogeneousCriterionClass:

    def test_score_answers_for_homo(self) -> None:
        """ The test test whether this method correctly gives a score or not that is
        between 0.0 and 1.0 indicating how similar the answers in answers are.
        """

        homo = HomogeneousCriterion()

        question1 = MultipleChoiceQuestion(1, 'CHOOSE #ONE', ['AA', 'BB', 'CC'])
        question2 = MultipleChoiceQuestion(2, 'CHOOSE #TWO', ['AA'])
        question3 = NumericQuestion(3, 'NUM #1', 50, 890)
        question4 = YesNoQuestion(5, 'YES')
        question5 = CheckboxQuestion(7, 'CHECK1', ['AA', 'BB'])

        answers1 = [Answer('aa'), Answer('AA')]
        answers2 = [Answer('AA')]
        answers3 = [Answer('MAMA'), Answer('BABA')]
        answers4 = [Answer(88), Answer(78)]
        answers5 = [Answer(50), Answer(890)]
        answers6 = [Answer(True), Answer(True)]
        answers7 = [Answer(False), Answer(True)]
        answers8 = [Answer('AA'), Answer('222')]
        answers9 = [Answer(['AA']), Answer(['BB'])]
        answers10 = [Answer(['AA'])]

        with pytest.raises(criterion.InvalidAnswerError):
            homo.score_answers(question1, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            homo.score_answers(question2, answers3)
        with pytest.raises(criterion.InvalidAnswerError):
            homo.score_answers(question3, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            homo.score_answers(question4, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            homo.score_answers(question5, answers8)

        assert homo.score_answers(question1, answers2) == 1.0
        assert homo.score_answers(question2, answers2) == 1.0
        assert homo.score_answers(question3, answers5) == 0.0
        assert homo.score_answers(question3, answers4) == 0.9880952380952381
        assert homo.score_answers(question4, answers6) == 1.0
        assert homo.score_answers(question4, answers7) == 0.0
        assert homo.score_answers(question5, answers9) == 0.0
        assert homo.score_answers(question5, answers10) == 1.0


class TestHeterogeneousCriterionClass:

    def test_score_answers_for_hetero(self) -> None:
        """ The test test whether this method correctly gives a score or not that is
        between 0.0 and 1.0 indicating how similar the answers in answers are.
        """

        hetero = HeterogeneousCriterion()

        question1 = MultipleChoiceQuestion(1, 'CHOOSE #ONE', ['AA', 'BB', 'CC'])
        question2 = MultipleChoiceQuestion(2, 'CHOOSE #TWO', ['AA'])
        question3 = NumericQuestion(3, 'NUM #1', 50, 890)
        question4 = YesNoQuestion(5, 'YES')
        question5 = CheckboxQuestion(7, 'CHECK1', ['AA', 'BB'])

        answers1 = [Answer('aa'), Answer('AA')]
        answers2 = [Answer('AA')]
        answers3 = [Answer('MAMA'), Answer('BABA')]
        answers4 = [Answer(88), Answer(78)]
        answers5 = [Answer(50), Answer(890)]
        answers6 = [Answer(True), Answer(True)]
        answers7 = [Answer(False), Answer(True)]
        answers8 = [Answer('AA'), Answer('222')]
        answers9 = [Answer(['AA']), Answer(['BB'])]
        answers10 = [Answer(['AA'])]

        with pytest.raises(criterion.InvalidAnswerError):
            hetero.score_answers(question1, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            hetero.score_answers(question2, answers3)
        with pytest.raises(criterion.InvalidAnswerError):
            hetero.score_answers(question3, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            hetero.score_answers(question4, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            hetero.score_answers(question5, answers8)

        assert hetero.score_answers(question1, answers2) == 0.0
        assert hetero.score_answers(question2, answers2) == 0.0
        assert hetero.score_answers(question3, answers5) == 1.0
        assert hetero.score_answers(question3, answers4) == 1 - 0.9880952380952381
        assert hetero.score_answers(question4, answers6) == 0.0
        assert hetero.score_answers(question4, answers7) == 1.0
        assert hetero.score_answers(question5, answers9) == 1.0
        assert hetero.score_answers(question5, answers10) == 0.0


class TestLonelyMemberCriterion:

    def test_score_answers_for_lonely(self) -> None:
        """ The test test whether this method correctly gives a score or not that is
        between 0.0 and 1.0 indicating how similar the answers in answers are.
        """

        lonely = LonelyMemberCriterion()

        question1 = MultipleChoiceQuestion(1, 'CHOOSE #ONE', ['AA', 'BB', 'CC'])
        question2 = MultipleChoiceQuestion(2, 'CHOOSE #TWO', ['AA'])
        question3 = NumericQuestion(3, 'NUM #1', 50, 890)
        question4 = YesNoQuestion(5, 'YES')
        question5 = CheckboxQuestion(7, 'CHECK1', ['AA', 'BB'])

        answers1 = [Answer('aa'), Answer('AA')]
        answers2 = [Answer('AA')]
        answers3 = [Answer('MAMA'), Answer('BABA')]
        answers4 = [Answer(88), Answer(78)]
        answers5 = [Answer(50), Answer(890)]
        answers6 = [Answer(True), Answer(True)]
        answers7 = [Answer(False), Answer(True)]
        answers8 = [Answer('AA'), Answer('222')]
        answers9 = [Answer(['AA']), Answer(['BB'])]
        answers10 = [Answer(['AA'])]

        with pytest.raises(criterion.InvalidAnswerError):
            lonely.score_answers(question1, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            lonely.score_answers(question2, answers3)
        with pytest.raises(criterion.InvalidAnswerError):
            lonely.score_answers(question3, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            lonely.score_answers(question4, answers1)
        with pytest.raises(criterion.InvalidAnswerError):
            lonely.score_answers(question5, answers8)

        assert lonely.score_answers(question1, answers2) == 0.0
        assert lonely.score_answers(question2, answers2) == 0.0
        assert lonely.score_answers(question3, answers5) == 0.0
        assert lonely.score_answers(question3, answers4) == 0.0
        assert lonely.score_answers(question4, answers6) == 1.0
        assert lonely.score_answers(question4, answers7) == 0.0
        assert lonely.score_answers(question5, answers9) == 0.0
        assert lonely.score_answers(question5, answers10) == 0.0


class TestSurveyClass:

    def test_len_method(self) -> None:
        """ The test tests whether the method correctly gives
        number of questions in this survey or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey2 = Survey([MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax'),
                          YesNoQuestion(3, 'Markus')])

        assert survey1.__len__() == 2
        assert survey2.__len__() == 3

    def test_contain_method(self) -> None:
        """ The test tests will the question in this survey or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        assert survey1.__contains__(MultipleChoiceQuestion(1, 'Choose', ['a']))
        assert survey1.__contains__(YesNoQuestion(2, 'Lax'))
        assert not survey1.__contains__(YesNoQuestion(3, '2'))

    def test_str_method(self) -> None:
        """The test tests whether str method can print a correct string representation or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        for question in questions:
            assert str(question) in str(survey1)

    def test_get_questions(self) -> None:
        """ The test tests whether this method gives a list of students in the survey or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        q_ids = set()
        for question in questions:
            q_ids.add(question.id)
        for question in survey1.get_questions():
            assert question.id in q_ids

    def test_get_questions2(self) -> None:
        """ The test tests whether this method gives a list of students in the survey or not.
        """

        questions2 = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax'),
                      YesNoQuestion(3, 'Markus')]

        survey2 = Survey(questions2)

        q_ids = set()
        for question in questions2:
            q_ids.add(question.id)
        for question in survey2.get_questions():
            assert question.id in q_ids

    def test_get_criterion(self) -> None:
        """ The test tests whether the criterion correctly associated with question in this survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey1.set_criterion(HeterogeneousCriterion(), MultipleChoiceQuestion(1, 'Choose', ['a']))

        assert isinstance(survey1._get_criterion(MultipleChoiceQuestion(1, 'Choose', ['a'])), HeterogeneousCriterion)

    def test_get_criterion2(self) -> None:
        """ The test tests whether the criterion correctly associated with question in this survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey1.set_criterion(HomogeneousCriterion(), YesNoQuestion(2, 'Lax'))

        assert isinstance(survey1._get_criterion(YesNoQuestion(2, 'Lax')), HomogeneousCriterion)

    def test_get_criterion3(self) -> None:
        """ The test tests whether the criterion correctly associated with question in this survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey1.set_criterion(HomogeneousCriterion(), YesNoQuestion(2, 'Lax'))

        assert not isinstance(survey1._get_criterion(YesNoQuestion(2, 'Lax')), HeterogeneousCriterion)

    def test_set_criterion(self) -> None:
        """ The test tests whether the criterion is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        assert survey1.set_criterion(HeterogeneousCriterion(), MultipleChoiceQuestion(1, 'Choose', ['a']))

    def test_set_criterion2(self) -> None:
        """ The test tests whether the criterion is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        assert survey1.set_criterion(HomogeneousCriterion(), YesNoQuestion(2, 'Lax'))

    def test_set_criterion3(self) -> None:
        """ The test tests whether the criterion is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a'])]
        survey1 = Survey(questions)

        assert not survey1.set_criterion(HomogeneousCriterion(), YesNoQuestion(2, 'Lax'))

    def test_set_weight(self) -> None:
        """The test tests whether the weight is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a'])]
        survey1 = Survey(questions)

        assert not survey1.set_criterion(0.5, YesNoQuestion(2, 'Lax'))

    def test_set_weight2(self) -> None:
        """The test tests whether the weight is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        assert survey1.set_criterion(0.8, YesNoQuestion(2, 'Lax'))

    def test_set_weight3(self) -> None:
        """The test tests whether the weight is successfully added into the question in survey
        or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)

        assert survey1.set_criterion(0.3, MultipleChoiceQuestion(1, 'Choose', ['a']))

    def test_get_weight(self) -> None:
        """ The test tests whether this method successfully finds out the correct weight of the
        question or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey1.set_weight(0.3, MultipleChoiceQuestion(1, 'Choose', ['a']))

        assert survey1._get_weight(MultipleChoiceQuestion(1, 'Choose', ['a'])) == 0.3

    def test_get_weight2(self) -> None:
        """ The test tests whether this method successfully finds out the correct weight of the
        question or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
        survey1 = Survey(questions)
        survey1.set_weight(1, YesNoQuestion(2, 'Lax'))

        assert survey1._get_weight(YesNoQuestion(2, 'Lax')) == 1.0

    def test_get_weight3(self) -> None:
        """ The test tests whether this method successfully finds out the correct weight of the
        question or not.
        """

        questions = [MultipleChoiceQuestion(1, 'Choose', ['a'])]
        survey1 = Survey(questions)
        survey1.set_weight(0.7, YesNoQuestion(2, 'Lax'))

        assert survey1._get_weight(YesNoQuestion(2, 'Lax')) == 1.0

    def test_score_student(self) -> None:
        """ The test tests whether this method correctly gives a quality score for students
        calculated based on their answers to the questions in this survey, and the associated
        criterion and weight for each question
        """

        questions = [YesNoQuestion(30, 'really'), MultipleChoiceQuestion(40, 'what', ['a', 'b', 'c']),
                     CheckboxQuestion(50, 'how', ['a', 'b', 'c', 'd'])]
        survey = Survey(questions)
        for x in range(len(questions)):
            survey.set_criterion(LonelyMemberCriterion(), questions[x])
            survey.set_weight(1, questions[x])
        students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
        answers = [[Answer(True), Answer('a'), Answer(['a', 'b'])],
                   [Answer(False), Answer('b'), Answer(['a', 'b'])],
                   [Answer(True), Answer('a'), Answer(['a', 'c'])],
                   [Answer(False), Answer('c'), Answer(['b', 'c'])]]
        for i in range(len(questions)):
            for j in range(len(students)):
                students[j].set_answer(questions[i], answers[j][i])
        act = survey.score_students(students)
        assert act == (1 / 3)

    def test_score_grouping(self) -> None:
        """ The test tests whether this method correctly gives a quality score
        for groups calculated based on gorups of students' answers to the
        questions in this survey, and the associated criterion and weight for
        each question
        """
        questions = [YesNoQuestion(30, 'really'), MultipleChoiceQuestion(40, 'what', ['a', 'b', 'c']),
                     CheckboxQuestion(50, 'how', ['a', 'b', 'c', 'd'])]
        survey = Survey(questions)
        for x in range(len(questions)):
            survey.set_criterion(LonelyMemberCriterion(), questions[x])
            survey.set_weight(1, questions[x])
        students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
        answers = [[Answer(True), Answer('a'), Answer(['a', 'b'])],
                   [Answer(False), Answer('b'), Answer(['a', 'b'])],
                   [Answer(True), Answer('a'), Answer(['a', 'c'])],
                   [Answer(False), Answer('c'), Answer(['b', 'c'])]]
        for i in range(len(questions)):
            for j in range(len(students)):
                students[j].set_answer(questions[i], answers[j][i])
        grouping = Grouping()
        students = slice_list(students, 2)
        for grp in students:
            grouping.add_group(Group(grp))
        act = survey.score_grouping(grouping)
        assert act == (1 / 6)


class TestSliceListMethod:

    def test_slice_list(self) -> None:
        """ The test tests whether this method correctly gives a list containing
        slices of a certain number in order. Each slice is a list of a specific size containing
        the next a specific size elements in list.
        """

        lst = [1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10]
        slice_size = 3

        assert slice_list(lst, slice_size) == [[1, 2, 3], [4, 5, 6], [7, 8, 8], [9, 10]]

    def test_slice_list2(self) -> None:
        """ The test tests whether this method correctly gives a list containing
        slices of a certain number in order. Each slice is a list of a specific size containing
        the next a specific size elements in list.
        """

        lst = [1, 'a', [2, 3], 4, False, 6, 7, True, 8, 998, 10]
        slice_size = 2

        assert slice_list(lst, slice_size) == [[1, 'a'], [[2, 3], 4], [False, 6], [7, True], [8, 998], [10]]

    def test_slice_list3_all_equal(self) -> None:
        """ The test tests whether this method correctly gives a list containing
        slices of a certain number in order. Each slice is a list of a specific size containing
        the next a specific size elements in list.
        """

        lst = [1, 'a', [778, 520], 4, False, [223, 157], 7, True, 8, 998, 10, 11]
        slice_size = 2

        assert slice_list(lst, slice_size) == [[1, 'a'], [[778, 520], 4], [False, [223, 157]], [7, True], [8, 998],
                                               [10, 11]]


class TestWindowsMethod:

    def test_windows(self) -> None:
        """ The test tests whether this method will correctly gives a list containing windows of list in order.
        Each window is a list of size a specific size  containing the elements with index i through index i + a
        specific size in the original list where i is the index of window in the returned list.
        """

        lst = [1, 2, 3, 4, 5, 6]
        slice_size = 3

        assert windows(lst, slice_size) == [[1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]]

    def test_windows2(self) -> None:
        """ The test tests whether this method will correctly gives a list containing windows of list in order.
        Each window is a list of size a specific size  containing the elements with index i through index i + a
        specific size in the original list where i is the index of window in the returned list.
        """

        lst = [1, True, False, [1, 3, 4], 'hallo']
        slice_size = 2

        assert windows(lst, slice_size) == [[1, True], [True, False], [False, [1, 3, 4]], [[1, 3, 4], 'hallo']]


class TestGroup:

    def test_len_method(self) -> None:
        """ The test tests will the mothod gives a correct length of student list or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)

        assert group.__len__() == 3

    def test_len_0_method(self) -> None:
        """ The test tests will the method gives a correct length of student list or not.
        """

        students = []
        group = Group(students)

        assert group.__len__() == 0

    def test_str_method(self) -> None:
        """ The test tests whether this method gives a correct string representation
        of all members in all groups.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)

        for student in students:
            assert student.name in str(group)

    def test_contain_method(self) -> None:
        """ The test tests whether this group contains this this name inside or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)

        assert group.__contains__(Student(1, "Martin"))
        assert group.__contains__(Student(2, 'Roy'))
        assert group.__contains__(Student(3, 'Jack'))
        assert not group.__contains__(Student(4, 'Aaron'))

    def test_get_members(self) -> None:
        """ The test tests whether this method gives a  correct list
        of members in this group or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        accumulator = set()

        for member in group.get_members():
            accumulator.add(member.id)
        assert accumulator == {1, 2, 3}

    def test_get_members2(self) -> None:
        """ The test tests whether this method gives a  correct list
        of members in this group or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        accumulator = set()

        for member in group.get_members():
            accumulator.add(member.id)
        assert accumulator != {1, 2, 4}


class TestGrouping:

    def test_len_method(self) -> None:
        """ The test tests will the method gives a correct length of student grouping or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        students2 = [Student(4, 'Zara'), Student(5, 'Sam')]
        group2 = Group(students2)
        groups = [group, group2]
        grouping = Grouping()

        assert grouping.__len__() == 0

    def test_len_method2(self) -> None:
        """ The test tests will the method gives a correct length of grouping list or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        students2 = [Student(4, 'Zara'), Student(5, 'Sam')]
        group2 = Group(students2)
        grouping = Grouping()
        grouping.add_group(group)
        grouping.add_group(group2)

        assert grouping._groups.__len__() == 2

    def test_str_method(self) -> None:
        """ The test tests whether this method gives a correct string representation
        of all members in all groups.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        students2 = [Student(4, 'Zara'), Student(5, 'Sam')]
        group2 = Group(students2)
        grouping = Grouping()
        grouping.add_group(group)
        grouping.add_group(group2)

        assert 'Martin' in grouping.__str__()
        assert 'Roy' in grouping.__str__()
        assert 'Jack' in grouping.__str__()
        assert 'Zara' in grouping.__str__()
        assert 'Sam' in grouping.__str__()
        assert 'Henry' not in grouping.__str__()

    def test_add_group(self) -> None:
        """ The test tests whether a group is added into the grouping or not.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        students2 = [Student(4, 'Zara'), Student(5, 'Sam')]
        group2 = Group(students2)
        students3 = []
        group3 = Group(students3)
        grouping = Grouping()

        assert grouping.add_group(group)
        assert grouping.add_group(group2)
        assert not grouping.add_group(group3)

    def test_get_group(self) -> None:
        """ The tess tests whether gives a correct list of all groups in this grouping.
        """

        students = [Student(1, "Martin"), Student(2, 'Roy'), Student(3, 'Jack')]
        group = Group(students)
        grouping = Grouping()

        grouping2 = Grouping()
        grouping2.add_group(group)

        assert grouping.get_groups() == []
        assert grouping2.get_groups() == [group]

    def test_alpha_grouper(self) -> None:
        """ The test tests whether this method group students alphabetically when all
        group size is less/equal/greater than group size"""

        for i in range(2, 6):
            students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
            questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
            survey = Survey(questions)
            course = Course('CSC148')
            course.enroll_students(students)
            alpha_grouper = AlphaGrouper(i)
            alpha_grouping = alpha_grouper.make_grouping(course, survey)
            lst = slice_list(sort_students(students, 'name'), i)
            grouping = Grouping()
            exp = []
            for grp in lst:
                grouping.add_group(Group(grp))
                for student in grp:
                    exp.append(student.name)
            act = []
            for grp in alpha_grouping.get_groups():
                for student in grp.get_members():
                    act.append(student.name)
            assert exp == act

    def test_random_grouper(self) -> None:
        """The test tests whether this method group students correctly when all
        group size is less/equal/greater than group size"""
        for i in range(2, 6):
            students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
            questions = [MultipleChoiceQuestion(1, 'Choose', ['a']), YesNoQuestion(2, 'Lax')]
            survey = Survey(questions)
            course = Course('CSC148')
            course.enroll_students(students)
            random_grouper = RandomGrouper(i)
            random_grouping = random_grouper.make_grouping(course, survey)
            lst = slice_list(course.students, i)
            grouping = Grouping()
            exp = []
            for grp in lst:
                grouping.add_group(Group(grp))
                for student in grp:
                    exp.append(student.name)
            act = []
            for grp in random_grouping.get_groups():
                for student in grp.get_members():
                    act.append(student.name)
            assert len(exp) == len(act)
            for item in exp:
                assert item in act
            for item in act:
                assert item in exp

    def test_greedy_grouper(self) -> None:
        """The test tests whether this method group students in a greedy way when all
        group size is less/equal/greater than group size"""
        for i in range(2, 6):
            greedy_grouper = GreedyGrouper(i)
            course = Course('CSC148')
            students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
            answers = [Answer(True), Answer(False), Answer(True), Answer(False)]
            questions = [YesNoQuestion(30, 'really')]
            for ques in questions:
                for j in range(len(students)):
                    students[j].set_answer(ques, answers[j])
            lst = slice_list([students[0], students[2], students[1], students[3]], i)
            course.enroll_students(students)
            survey = Survey(questions)
            survey.set_criterion(LonelyMemberCriterion(), questions[0])
            survey.set_weight(1, questions[0])
            greedy_grouping = greedy_grouper.make_grouping(course, survey)
            act = []
            for grp in greedy_grouping.get_groups():
                for student in grp.get_members():
                    act.append(student.name)
            grouping = Grouping()
            exp = []
            for grp in lst:
                grouping.add_group(Group(grp))
                for student in grp:
                    exp.append(student.name)
            assert act == exp

    def test_window_grouper(self) -> None:
        """The test tests whether this method group students in score of windows
         when all group size is less/equal/greater than group size"""
        for i in range(2, 6):
            window_grouper = WindowGrouper(i)
            course = Course('CSC148')
            students = [Student(1, 'Martin'), Student(2, 'Roy'), Student(3, 'Bruno'), Student(4, 'Aaron')]
            answers = [Answer(True), Answer(False), Answer(False), Answer(True)]
            questions = [YesNoQuestion(30, 'really')]
            for ques in questions:
                for j in range(len(students)):
                    students[j].set_answer(ques, answers[j])
            if i == 2:
                lst = slice_list([students[1], students[2], students[0], students[3]], i)
            else:
                lst = slice_list([students[0], students[1], students[2], students[3]], i)
            course.enroll_students(students)
            survey = Survey(questions)
            survey.set_criterion(LonelyMemberCriterion(), questions[0])
            survey.set_weight(1, questions[0])
            window_grouping = window_grouper.make_grouping(course, survey)
            act = []
            for grp in window_grouping.get_groups():
                for student in grp.get_members():
                    act.append(student.name)
            grouping = Grouping()
            exp = []
            for grp in lst:
                grouping.add_group(Group(grp))
                for student in grp:
                    exp.append(student.name)
            assert act == exp


if __name__ == '__main__':

    import coverage

    cov = coverage.coverage()
    cov.start()
    import pytest

    pytest.main(['tests.py'])
    cov.stop()
    cov.save()
    cov.report()
