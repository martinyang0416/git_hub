"""A3. Test cases for function banking_functions.open_savings_account.
"""

import unittest
import banking_functions


class TesOpenSavingsAccount(unittest.TestCase):
    """Test cases for function
    banking_functions.get_client_to_total_balance.
    """

    def test_00_one_person_one_account(self):
        param = {('Bob Bob', 786543210):[[1.0], [1.5]]}
        banking_functions.open_savings_account(param, ('Bob Bob', 786543210), 2.0, 1.1)
        expected = {('Bob Bob', 786543210): [[1.0, 2.0], [1.5, 1.1]]}
        msg = "Expected {}, but got {}".format(expected, param)
        self.assertEqual(param, expected, msg)

    def test_1(self):
        param = {('Peter', 777):[[3.0, 4.9], [2.0, 2.5]]}
        banking_functions.open_savings_account(param, ('Peter', 777), 4.4, 3.3)
        expected = {('Peter', 777):[[3.0, 4.9, 4.4], [2.0, 2.5, 3.3]]}
        msg = "Expected {}, but got {}".format(expected, param)
        self.assertEqual(param, expected, msg)

    def test_3(self):
        param = {('Peter', 777):[[3.0, -4.9], [2.0, 2.5]]}
        banking_functions.open_savings_account(param, ('Peter', 777), 4.4, 3.3)
        expected = {('Peter', 777):[[3.0, 4.4, -4.9], [2.0, 3.3, 2.5]]}
        msg = "Expected {}, but got {}".format(expected, param)
        self.assertEqual(param, expected, msg)



if __name__ == '__main__':
    unittest.main(exit=False)
