# TODO: put all your tests in this file (you can delete this line)
from typing import Any
from course import *
from criterion import *
from survey import *
from grouper import Group, Grouper, Grouping
from grouper import Group, Grouper, GreedyGrouper, AlphaGrouper, Grouping,\
    WindowGrouper, RandomGrouper, slice_list, windows


def test_str_():
    stu_1 = Student(111, 'niubi')

    assert stu_1.__str__() == 'niubi'


def test_has_answer():
    stu_1 = Student(111, 'niubi')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')

    assert stu_1.has_answer(ques_duoxuan) is False
    stu_1.set_answer(ques_duoxuan, answ_1)
    assert stu_1.has_answer(ques_duoxuan) is True


def test_set_answer():
    stu_1 = Student(111, 'niubi')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')

    stu_1.set_answer(ques_duoxuan, answ_1)

    assert stu_1._answers[1] == answ_1


def test_get_answer():
    stu_1 = Student(111, 'niubi')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    answ_1 = Answer('zhangsan')

    stu_1.set_answer(ques_duoxuan, answ_1)

    assert stu_1.get_answer(ques_duoxuan) == answ_1
    assert stu_1.get_answer(ques_dagou) is None


def test_enroll_students():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    course_1 = Course('csc148')

    course_1.enroll_students([stu_1, stu_2])

    assert stu_1 in course_1.students
    assert stu_2 in course_1.students


def test_all_answered():
    course_1 = Course('csc148')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    answ_1 = Answer('zhangsan')
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])

    stu_1.set_answer(ques_duoxuan, answ_1)
    course_1.enroll_students([stu_1, stu_2])

    assert course_1.all_answered(survey_1) is False


def test_get_students():
    course_1 = Course('csc148')
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    course_1.enroll_students([stu_1, stu_2])

    assert course_1.get_students() == (stu_1, stu_2)


def test_multiple_validate_answer():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')

    assert ques_duoxuan.validate_answer(answ_1) is True


def test_multiple_get_similarity():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer('lisi')

    assert ques_duoxuan.get_similarity(answ_1, answ_2) == 0.0


def test_yesno_validate_answer():
    ques_duicuo = YesNoQuestion(3, 'what')
    answ_1 = Answer(True)

    assert ques_duicuo.validate_answer(answ_1) is True


def test_yesno_get_similarity():
    ques_duicuo = YesNoQuestion(3, 'what')
    answ_1 = Answer(True)
    answ_2 = Answer(False)

    assert ques_duicuo.get_similarity(answ_1, answ_2) == 0.0


def test_checkbox_validate_answer():
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    answ_1 = Answer(['zhangsan'])

    assert ques_dagou.validate_answer(answ_1) is True


def test_checkbox_get_similarity():
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    answ_1 = Answer(['zhangsan', 'lisi'])
    answ_2 = Answer(['zhangsan', 'wangwu', 'zhaoliu'])

    assert ques_dagou.get_similarity(answ_1, answ_2) == 0.25


def test_numeric_validate_answer():
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    answ_1 = Answer(1)

    assert ques_shuzi.validate_answer(answ_1) is True


def test_numeric_get_similarity():
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    answ_1 = Answer(1)
    answ_2 = Answer(0)

    assert ques_shuzi.get_similarity(answ_1, answ_2) == 0.0


def test_answer_is_valid():
    answ_1 = Answer('zhangsan')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])

    assert answ_1.is_valid(ques_duoxuan) is True


def test_homo_score_answers():
    temp = HomogeneousCriterion()
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer('lisi')
    answ_3 = Answer('wangwu')
    answ_4 = Answer('shayebushi')
    answers = [answ_1, answ_2, answ_3]

    assert temp.score_answers(ques_duoxuan, answers) == 0.0


def test_heter_score_answers():
    temp = HeterogeneousCriterion()
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer('lisi')
    answ_3 = Answer('wangwu')
    answ_4 = Answer('shayebushi')
    answers = [answ_1, answ_2, answ_3]

    assert temp.score_answers(ques_duoxuan, answers) == 1.0


def test_lonely_score_answers():
    temp = LonelyMemberCriterion()
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer('lisi')
    answ_3 = Answer('wangwu')
    answers = [answ_1, answ_2, answ_3]

    assert temp.score_answers(ques_duoxuan, answers) == 0.0


def test_group_len():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    temp = Group([stu_1, stu_2])

    assert temp.__len__() == 2


def test_group_contains():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    temp = Group([stu_1, stu_2])

    assert temp.__contains__(stu_2) is True


def test_group_get_members():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    temp = Group([stu_1, stu_2])

    assert temp.get_members() == [stu_1, stu_2]


def test_group_add_member():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    temp = Group([stu_1])

    temp._add_member(stu_2)

    assert temp.__contains__(stu_2) is True


def test_grouping_len():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    group_1 = Group([stu_1, stu_2])
    group_2 = Group([stu_3, stu_4])
    grouping_1 = Grouping()
    grouping_1.add_group(group_1)
    grouping_1.add_group(group_2)

    assert grouping_1.__len__() == 2


def test_grouping_add_group():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    group_1 = Group([stu_1, stu_2])
    group_2 = Group([stu_3, stu_4])
    grouping_1 = Grouping()
    grouping_1.add_group(group_1)
    grouping_1.add_group(group_2)

    assert grouping_1.get_groups() == [group_1, group_2]


def test_grouping_get_groups():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    group_1 = Group([stu_1, stu_2])
    group_2 = Group([stu_3, stu_4])
    grouping_1 = Grouping()
    grouping_1.add_group(group_1)
    grouping_1.add_group(group_2)

    assert grouping_1.get_groups() == [group_1, group_2]


def test_survey_len():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])

    assert survey_1.__len__() == 4


def test_survey_contains():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])

    assert survey_1.__contains__(ques_duoxuan) is True


def test_survey_get_questions():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])

    assert survey_1.get_questions() == [ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou]


def test_survey_set_criterion():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = HeterogeneousCriterion()

    assert survey_1.set_criterion(temp, ques_duoxuan) is True


def test_survey_set_weight():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = 2

    assert survey_1.set_weight(temp, ques_duoxuan) is True


def test_get_criterion():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = HeterogeneousCriterion()
    survey_1.set_criterion(temp, ques_duoxuan)

    assert survey_1._get_criterion(ques_duoxuan) == temp


def test_get_weight():
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = 2
    survey_1.set_weight(temp, ques_duoxuan)

    assert survey_1._get_weight(ques_duoxuan) == 2


def test_survey_score_student():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = HeterogeneousCriterion()
    survey_1.set_criterion(temp, ques_duoxuan)
    survey_1.set_criterion(temp, ques_duicuo)
    survey_1.set_criterion(temp, ques_dagou)
    survey_1.set_criterion(temp, ques_shuzi)
    answ_1 = Answer('zhangsan')
    answ_2 = Answer(True)
    answ_3 = Answer(0)
    answ_4 = Answer(['zhangsan', 'lisi'])
    answ_5 = Answer('zhangsan')
    answ_6 = Answer(False)
    answ_7 = Answer(1)
    answ_8 = Answer(['zhangsan', 'wangwu'])
    stu_1.set_answer(ques_duoxuan, answ_1)
    stu_1.set_answer(ques_shuzi, answ_3)
    stu_1.set_answer(ques_dagou, answ_4)
    stu_1.set_answer(ques_duicuo, answ_2)
    stu_2.set_answer(ques_duoxuan, answ_5)
    stu_2.set_answer(ques_dagou, answ_8)
    stu_2.set_answer(ques_shuzi, answ_7)
    stu_2.set_answer(ques_duicuo, answ_6)

    assert round(survey_1.score_students([stu_1, stu_2]), 2) == 0.67


def test_survey_score_grouping():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    temp = HeterogeneousCriterion()
    survey_1.set_criterion(temp, ques_duoxuan)
    survey_1.set_criterion(temp, ques_duicuo)
    survey_1.set_criterion(temp, ques_dagou)
    survey_1.set_criterion(temp, ques_shuzi)
    answ_1 = Answer('zhangsan')
    answ_2 = Answer(True)
    answ_3 = Answer(0)
    answ_4 = Answer(['zhangsan', 'lisi'])
    answ_5 = Answer('zhangsan')
    answ_6 = Answer(False)
    answ_7 = Answer(1)
    answ_8 = Answer(['zhangsan', 'wangwu'])
    stu_1.set_answer(ques_duoxuan, answ_1)
    stu_1.set_answer(ques_shuzi, answ_3)
    stu_1.set_answer(ques_dagou, answ_4)
    stu_1.set_answer(ques_duicuo, answ_2)
    stu_2.set_answer(ques_duoxuan, answ_5)
    stu_2.set_answer(ques_dagou, answ_8)
    stu_2.set_answer(ques_shuzi, answ_7)
    stu_2.set_answer(ques_duicuo, answ_6)

    grouping_1 = Grouping()
    group_1 = Group([stu_1, stu_2])

    assert survey_1.score_grouping(grouping_1) == 0.0

    grouping_1.add_group(group_1)

    assert round(survey_1.score_grouping(grouping_1), 2) == 0.67


def test_slice_list():
    temp = [1, 2, 3, 4, 5, 6]

    assert slice_list(temp, 2) == [[1, 2], [3, 4], [5, 6]]
    assert slice_list(temp, 3) == [[1, 2, 3], [4, 5, 6]]
    assert slice_list(temp, 4) == [[1, 2, 3, 4], [5, 6]]


def test_windows():
    temp = [1, 2, 3, 4, 5, 6]

    assert windows(temp, 2) == [[1, 2], [2, 3], [3, 4], [4, 5], [5, 6]]
    assert windows(temp, 3) == [[1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]]
    assert windows(temp, 6) == [[1, 2, 3, 4, 5, 6]]


def test_alpha_make_grouping():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    temp = AlphaGrouper(2)
    course = Course('csc148')
    course.enroll_students([stu_1, stu_2, stu_3, stu_4])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])

    assert temp.make_grouping(course, survey_1).get_groups()[0].get_members() == [stu_4, stu_2]
    assert temp.make_grouping(course, survey_1).get_groups()[1].get_members() == [stu_1, stu_3]


def test_random_make_grouping():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 1)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    temp = RandomGrouper(2)
    course = Course('csc148')
    course.enroll_students([stu_1, stu_2, stu_3, stu_4])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    result = temp.make_grouping(course, survey_1)

    if stu_1 in result.get_groups()[0].get_members():
        assert stu_1 not in result.get_groups()[1].get_members()
    if stu_2 in result.get_groups()[0].get_members():
        assert stu_2 not in result.get_groups()[1].get_members()
    if stu_3 in result.get_groups()[0].get_members():
        assert stu_3 not in result.get_groups()[1].get_members()
    if stu_4 in result.get_groups()[0].get_members():
        assert stu_4 not in result.get_groups()[1].get_members()


def test_greedy_make_grouping():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 10)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    temp = GreedyGrouper(2)
    course = Course('csc148')
    course.enroll_students([stu_1, stu_2, stu_3, stu_4])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer(True)
    answ_3 = Answer(1)
    answ_4 = Answer(['zhangsan', 'lisi'])
    answ_5 = Answer('zhangsan')
    answ_6 = Answer(False)
    answ_7 = Answer(2)
    answ_8 = Answer(['zhangsan', 'wangwu'])
    answ_9 = Answer('wangwu')
    answ_10 = Answer(True)
    answ_11 = Answer(4)
    answ_12 = Answer(['zhangsan', 'lisi', 'wangwu'])
    answ_13 = Answer('zhangsan')
    answ_14 = Answer(False)
    answ_15 = Answer(8)
    answ_16 = Answer(['zhangsan', 'zhaoliu'])
    stu_1.set_answer(ques_duoxuan, answ_1)
    stu_1.set_answer(ques_shuzi, answ_3)
    stu_1.set_answer(ques_dagou, answ_4)
    stu_1.set_answer(ques_duicuo, answ_2)
    stu_2.set_answer(ques_duoxuan, answ_5)
    stu_2.set_answer(ques_dagou, answ_8)
    stu_2.set_answer(ques_shuzi, answ_7)
    stu_2.set_answer(ques_duicuo, answ_6)
    stu_3.set_answer(ques_duoxuan, answ_9)
    stu_3.set_answer(ques_shuzi, answ_11)
    stu_3.set_answer(ques_dagou, answ_12)
    stu_3.set_answer(ques_duicuo, answ_10)
    stu_4.set_answer(ques_duoxuan, answ_13)
    stu_4.set_answer(ques_dagou, answ_16)
    stu_4.set_answer(ques_shuzi, answ_15)
    stu_4.set_answer(ques_duicuo, answ_14)

    assert temp.make_grouping(course, survey_1).get_groups()[0].get_members() == [stu_1, stu_3]
    assert temp.make_grouping(course, survey_1).get_groups()[1].get_members() == [stu_2, stu_4]


def test_window_make_grouping():
    stu_1 = Student(111, 'niubi')
    stu_2 = Student(222, 'newbee')
    stu_3 = Student(333, 'vicky')
    stu_4 = Student(444, 'eric')
    ques_duoxuan = MultipleChoiceQuestion(1, 'what', ['zhangsan', 'lisi', 'wangwu'])
    ques_shuzi = NumericQuestion(2, 'what', 0, 10)
    ques_duicuo = YesNoQuestion(3, 'what')
    ques_dagou = CheckboxQuestion(4, 'what', ['zhangsan', 'lisi', 'wangwu', 'zhaoliu'])
    temp = WindowGrouper(2)
    course = Course('csc148')
    course.enroll_students([stu_1, stu_2, stu_3, stu_4])
    survey_1 = Survey([ques_duoxuan, ques_shuzi, ques_duicuo, ques_dagou])
    answ_1 = Answer('zhangsan')
    answ_2 = Answer(True)
    answ_3 = Answer(1)
    answ_4 = Answer(['zhangsan', 'lisi'])
    answ_5 = Answer('zhangsan')
    answ_6 = Answer(False)
    answ_7 = Answer(2)
    answ_8 = Answer(['zhangsan', 'wangwu'])
    answ_9 = Answer('wangwu')
    answ_10 = Answer(True)
    answ_11 = Answer(4)
    answ_12 = Answer(['zhangsan', 'lisi', 'wangwu'])
    answ_13 = Answer('zhangsan')
    answ_14 = Answer(False)
    answ_15 = Answer(8)
    answ_16 = Answer(['zhangsan', 'zhaoliu'])
    stu_1.set_answer(ques_duoxuan, answ_1)
    stu_1.set_answer(ques_shuzi, answ_3)
    stu_1.set_answer(ques_dagou, answ_4)
    stu_1.set_answer(ques_duicuo, answ_2)
    stu_2.set_answer(ques_duoxuan, answ_5)
    stu_2.set_answer(ques_dagou, answ_8)
    stu_2.set_answer(ques_shuzi, answ_7)
    stu_2.set_answer(ques_duicuo, answ_6)
    stu_3.set_answer(ques_duoxuan, answ_9)
    stu_3.set_answer(ques_shuzi, answ_11)
    stu_3.set_answer(ques_dagou, answ_12)
    stu_3.set_answer(ques_duicuo, answ_10)
    stu_4.set_answer(ques_duoxuan, answ_13)
    stu_4.set_answer(ques_dagou, answ_16)
    stu_4.set_answer(ques_shuzi, answ_15)
    stu_4.set_answer(ques_duicuo, answ_14)

    assert temp.make_grouping(course, survey_1).get_groups()[0].get_members() == []


if __name__ == '__main__':
    import pytest
    pytest.main(['tests.py'])
