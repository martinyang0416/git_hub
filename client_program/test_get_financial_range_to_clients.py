"""A3. Test cases for function banking_functions.get_financial_range_to_clients.
"""

import unittest
import banking_functions


class TestGetFinancialRangeToClients(unittest.TestCase):
    """Test cases for function
    banking_functions.get_financial_range_to_clients.
    """

    def test_00_empty(self):
        param1 = {}
        param2 = [()]
        actual = banking_functions.get_financial_range_to_clients(param1, param2)
        expected = {}
        msg = "Expected {}, but returned {}".format(expected, actual)
        self.assertEqual(actual, expected, msg)

    def test_01_one_person_within_one_range(self):
        param1 = {('Bob Bob', 786543210): 10.0}
        param2 = [(0, 100000)]
        actual = banking_functions.get_financial_range_to_clients(param1, param2)
        expected = {(0, 100000): [('Bob Bob', 786543210)]}
        msg = "Expected {}, but returned {}".format(expected, actual)
        self.assertEqual(actual, expected, msg)

    def test2(self):
        param1 = {('Peter', 333): 90.0}
        param2 = [(50, 100000), (100001, 200000)]
        actual = banking_functions.get_financial_range_to_clients(param1, param2)
        expected = {(50, 100000): [('Peter', 333)]}
        msg = "Expected {}, but returned {}".format(expected, actual)
        self.assertEqual(actual, expected, msg)

    def test3(self):
        param1 = {('Peter', 333): 90.0, ('John', 444): 200.0}
        param2 = [(2, 100), (101, 300)]
        actual = banking_functions.get_financial_range_to_clients(param1, param2)
        expected = {(2, 100): [('Peter', 333)], (101, 300): [('John', 444)]}
        msg = "Expected {}, but returned {}".format(expected, actual)
        self.assertEqual(actual, expected, msg)

if __name__ == '__main__':
    unittest.main(exit=False)
