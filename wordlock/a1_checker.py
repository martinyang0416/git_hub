"""A simple checker for types of functions in wordlock_functions.py."""

import unittest
import checker_generic
import wordlock_functions as wf
import wordlock_game


class CheckTest(unittest.TestCase):
    """Sanity checker for assignment functions."""

    def testGetSectionStart(self):
        """Function get_section_start."""

        self._check(wf.get_section_start, [1], int)

    def testIsValidMove(self):
        """Function is_valid_move."""

        self._check(wf.is_valid_move, ['S'], bool)

    def testIsValidSection(self):
        """Function is_valid_section."""

        self._check(wf.is_valid_section, [-1], bool)

    def testCheckSection(self):
        """Function check_section."""

        self._check(wf.check_section, ['CATDGOXOFIGP', 1], bool)

    def testChangeState(self):
        """Function change_state."""

        self._check(wf.change_state, ['TACDGOXOFIGP', 1, 'S'], str)

    def testGetMoveHint(self):
        """Function get_move_hint."""

        self._check(wf.get_move_hint, ['TACDOGFOXPIG', 1], str)

    def _check(self, func: callable, args: list,
               ret_type: type) -> None:
        """Check that func called with arguments args returns a value of type
        ret_type. Display the progress and the result of the check.

        """

        print('\nChecking {}...'.format(func.__name__))
        result = checker_generic.check(func, args, ret_type)
        self.assertTrue(result[0], result[1])
        print('  check complete')


TARGET_LEN = 79
print(''.center(TARGET_LEN, "="))
print(' Start: checking coding style '.center(TARGET_LEN, "="))
checker_generic.run_pyta('wordlock_functions.py', 'pyta/a1_pyta.txt')
print(' End checking coding style '.center(TARGET_LEN, "="))

print(' Start: checking type contracts '.center(TARGET_LEN, "="))
unittest.main(exit=False)
print(' End checking type contracts '.center(TARGET_LEN, "="))

print('\nScroll up to see ALL RESULTS:')
print('  - checking coding style')
print('  - checking type contract\n')
