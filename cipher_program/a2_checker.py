"""A simple checker for types of functions in wordlock_functions.py."""

import unittest
import checker_generic
import cipher_functions as cf
from typing import List
import copy

SHOULD_MUTATE = '{} should mutate the deck, but did'
SHOULD_NOT_MUTATE = '{} should NOT mutate the deck, but did'
SHOULD_RETURN_NONE = '{} should return None, but returned {}'


class CheckTest(unittest.TestCase):
    """Sanity checker for assignment functions."""

    def setUp(self):
        """Create a tester deck"""

        self.short_deck = [1, 2, 3, 4, 5, 6]
        self.long_deck = [1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 3,
                          6, 9, 12, 15, 18, 21, 24, 27, 2, 5, 8,
                          11, 14, 17, 20, 23, 26]

    def testCleanMessage(self):
        """Function clean_message."""

        self._check_simple_type(cf.clean_message, ['A'], str)

    def testEncryptLetter(self):
        """Function encrypt_letter."""

        self._check_simple_type(cf.encrypt_letter, ['A', 1], str)

    def testDecryptLetter(self):
        """Function decrypt_letter."""

        self._check_simple_type(cf.decrypt_letter, ['B', 1], str)

    def testSwapCards(self):
        """Function swap_cards."""

        deck_copy = copy.deepcopy(self.long_deck)
        result = cf.swap_cards(self.long_deck, 1)
        self.assertEqual(result, None,
                         SHOULD_RETURN_NONE.format('swap_cards', type(result))
                         )
        self.assertNotEqual(deck_copy, self.long_deck,
                            SHOULD_MUTATE.format('swap_cards'))

    def testGetSmallJokerValue(self):
        """Function get_small_joker_value."""

        deck_copy = copy.deepcopy(self.long_deck)
        self._check_simple_type(cf.get_small_joker_value, [self.long_deck], int)
        self.assertEqual(deck_copy, self.long_deck,
                         SHOULD_NOT_MUTATE.format('get_small_joker_value'))

    def testGetBigJokerValue(self):
        """Function get_big_joker_value."""

        deck_copy = copy.deepcopy(self.long_deck)
        self._check_simple_type(cf.get_big_joker_value, [self.long_deck], int)
        self.assertEqual(deck_copy, self.long_deck,
                         SHOULD_NOT_MUTATE.format('get_big_joker_value'))

    def testMoveSmallJoker(self):
        """Function move_small_joker."""

        deck_copy = copy.deepcopy(self.long_deck)
        result = cf.move_small_joker(self.long_deck)
        self.assertEqual(result, None,
                         SHOULD_RETURN_NONE.format('move_small_joker', type(result))
                         )
        self.assertNotEqual(deck_copy, self.long_deck,
                            SHOULD_MUTATE.format('move_small_joker'))

    def testMoveBigJoker(self):
        """Function move_big_joker."""

        deck_copy = copy.deepcopy(self.long_deck)
        result = cf.move_big_joker(self.long_deck)
        self.assertEqual(result, None,
                         SHOULD_RETURN_NONE.format(
                             'move_big_joker', type(result))
                         )
        self.assertNotEqual(deck_copy, self.long_deck,
                            SHOULD_MUTATE.format('move_big_joker'))

    def testTripleCut(self):
        """Function triple_cut."""

        deck_copy = copy.deepcopy(self.long_deck)
        result = cf.triple_cut(self.long_deck)
        self.assertEqual(result, None,
                         SHOULD_RETURN_NONE.format(
                             'triple_cut', type(result))
                         )
        self.assertNotEqual(deck_copy, self.long_deck,
                            SHOULD_MUTATE.format('triple_cut'))

    def testInsertTopToBottom(self):
        """Function insert_top_to_bottom."""

        deck_copy = copy.deepcopy(self.long_deck)
        result = cf.insert_top_to_bottom(self.long_deck)
        self.assertEqual(result, None,
                         SHOULD_RETURN_NONE.format(
                             'insert_top_to_bottom', type(result))
                         )
        self.assertNotEqual(deck_copy, self.long_deck,
                            SHOULD_MUTATE.format('insert_top_to_bottom'))

    def testGetCardAtTopIndex(self):
        """Function get_card_at_top_index."""

        deck_copy = copy.deepcopy(self.long_deck)
        self._check_simple_type(cf.get_card_at_top_index, [self.short_deck], int)
        self.assertEqual(deck_copy, self.long_deck,
                            SHOULD_NOT_MUTATE.format('get_card_at_top_index'))

    def testGetNextKeystreamValue(self):
        """Function get_next_keystream_value."""

        self._check_simple_type(cf.get_next_keystream_value, [self.long_deck], int)

    def testProcessMessages(self):
        """Function process_messages."""

        self._check_simple_type(cf.process_messages,
                    [self.long_deck, ['A', 'B'], cf.ENCRYPT],
                    list)

    def testIsValidDeck(self):
        """Function is_valid_deck."""

        self._check_simple_type(cf.is_valid_deck, [self.short_deck], bool)

    def _check_simple_type(self, func: callable, args: list,
               ret_type: type) -> None:
        """Check that func called with arguments args returns a value of type
        ret_type. Display the progress and the result of the check.

        """

        print('\nChecking {}...'.format(func.__name__))
        result = checker_generic.type_check_simple(func, args, ret_type)
        self.assertTrue(result[0], result[1])
        print('  check complete')


TARGET_LEN = 79
print(''.center(TARGET_LEN, "="))
print(' Start: checking coding style '.center(TARGET_LEN, "="))
checker_generic.run_pyta('cipher_functions.py', 'pyta/a2_pyta.txt')
print(' End checking coding style '.center(TARGET_LEN, "="))

print(' Start: checking type contracts '.center(TARGET_LEN, "="))
unittest.main(exit=False)
print(' End checking type contracts '.center(TARGET_LEN, "="))

print('\nScroll up to see ALL RESULTS:')
print('  - checking coding style')
print('  - checking type contract\n')
