import unittest
from course import *


class MyTestCase(unittest.TestCase):
    def setUp(self) -> None:
        self.c1 = Course("CSC148")
        self.s1 = Student(1, "A")
        self.s2 = Student(2, "B")
        self.s3 = Student(3, "C")
        self.scopy = Student(1, "D")

    def tearDown(self) -> None:
        self.c1 = Course("CSC148")
        self.s1 = Student(1, "A")
        self.s2 = Student(2, "B")
        self.s3 = Student(3, "C")
        self.scopy = Student(1, "D")

    def test_duplicate_input(self):
        self.c1.enroll_students([self.s1, self.scopy])
        self.assertTrue(self.c1.students == [])

    def test_duplicate_input2(self):
        self.c1.enroll_students([self.s1, self.s2, self.s3, self.scopy])
        self.assertTrue(self.c1.students == [])


if __name__ == '__main__':
    unittest.main(exit=False)
