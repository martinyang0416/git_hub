"""A simple checker for types of functions in twitterverse_functions.py."""

from typing import Any, Dict, List
from io import StringIO
import unittest
import checker_generic
import twitterverse_functions as tf

FILENAME = 'twitterverse_functions.py'
PYTA_CONFIG = 'pyta/a3_pyta.txt'
TARGET_LEN = 79
SEP = '='

CONSTANTS = {
    'USERNAME': 'username',
    'NAME': 'name',
    'LOCATION': 'location',
    'WEB': 'web',
    'BIO': 'bio',
    'FOLLOWING': 'following',
    'ENDBIO': 'ENDBIO',
    'END': 'END',
    'SEARCH': 'SEARCH',
    'FILTER': 'FILTER',
    'PRESENT': 'PRESENT',
    'OPERATIONS': 'operations',
    'FOLLOWER': 'follower',
    'FOLLOWERS': 'followers',
    'NAME_INCLUDES': 'name-includes',
    'LOCATION_INCLUDES': 'location-includes',
    'SORT_BY': 'sort-by',
    'POPULARITY': 'popularity',
    'FORMAT': 'format',
    'LONG': 'long'
}

DATA_FILE = """tomCruise
Tom Cruise
Los Angeles, CA
http://www.tomcruise.com
Official TomCruise.com crew tweets. We love you guys! 
Visit us at Facebook!
ENDBIO
katieH
END
katieH
Katie Holmes

www.tomkat.com
ENDBIO
END
"""

QUERY_FILE = """SEARCH
tomCruise
following
followers
FILTER
following katieH
name-includes tom
location-includes CA 
PRESENT
sort-by username
format long
"""

TWITTER_DATA = {'tomCruise': {'name': 'Tom Cruise',
                              'location': 'Los Angeles, CA',
                              'web': 'http://www.tomcruise.com',
                              'bio': 'Official TomCruise.com crew tweets. ' +
                              'We love you guys!\nVisit us at Facebook!',
                              'following': ['katieH']},
                'katieH': {'name': 'Katie Holmes', 'location': '',
                           'web': 'www.tomkat.com', 'bio': '', 'following': []}}

QUERY = {'SEARCH': {'username': 'tomCruise',
                    'operations': ['following', 'followers']},
         'FILTER': {'following': 'katieH',
                    'name-includes': 'tom', 'location-includes': 'CA'},
         'PRESENT': {'sort-by': 'username', 'format': 'long'}}

LONG_RESULT = """----------
katieH
name: Katie Holmes
location: 
website: www.tomkat.com
bio:

following: []
----------
tomCruise
name: Tom Cruise
location: Los Angeles, CA
website: http://www.tomcruise.com
bio:
Official TomCruise.com crew tweets. We love you guys!
Visit us at Facebook!
following: ['katieH']
----------
"""


class CheckTest(unittest.TestCase):
    """Type checker for assignment functions."""

    def test_process_data(self) -> None:
        """Test function process_data."""

        data_keys = ['name', 'location', 'web', 'bio', 'following']
        msg = 'process_data should return a TwitterverseDict'
        open_data_file = StringIO(DATA_FILE)
        result = tf.process_data(open_data_file)
        for user in result:
            self.assertTrue(isinstance(user, str), msg)
            self._has_these_keys(result[user], data_keys, msg)
            for key in result[user]:
                if key == 'following':
                    self.assertTrue(isinstance(result[user][key], list), msg)
                    for item in result[user][key]:
                        self.assertTrue(isinstance(item, str), msg)
                else:
                    self.assertTrue(isinstance(result[user][key], str), msg)

    def test_process_query(self) -> None:
        """Test function process_query."""

        query_keys = ['SEARCH', 'FILTER', 'PRESENT']
        msg = 'process_query should return a valid QueryDict'
        open_query_file = StringIO(QUERY_FILE)
        result = tf.process_query(open_query_file)
        self._has_these_keys(result, query_keys, msg)

        # Search spec
        self._has_these_keys(result['SEARCH'], ['username', 'operations'], msg)
        self.assertTrue(isinstance(result['SEARCH']['operations'], list), msg)
        for item in result['SEARCH']['operations']:
            self.assertTrue(isinstance(item, str), msg)
        self.assertTrue(isinstance(result['SEARCH']['username'], str), msg)

        # Filter spec
        filter_keys = ['following', 'follower', 'name-includes',
                       'location-includes', 'bio-includes']
        self._has_these_keys(result['FILTER'], filter_keys, msg)
        self._is_dict_of_Ks_and_Vs(result['FILTER'], str, str, msg)

        # Sorting spec
        self._has_these_keys(result['PRESENT'], ['sort-by', 'format'], msg)
        self._is_dict_of_Ks_and_Vs(result['PRESENT'], str, str, msg)

    def test_get_search_results(self) -> None:
        """Test function get_search_results."""

        self._test_returns_list_of(tf.get_search_results,
                                   [TWITTER_DATA, QUERY['SEARCH']], [str])

    def test_get_filter_results(self) -> None:
        """Test function get_filter_results."""

        self._test_returns_list_of(tf.get_filter_results,
                                   [TWITTER_DATA, ['tomCruise', 'katieH'],
                                    QUERY['FILTER']], [str])

    def test_get_present_string(self) -> None:
        """Test function get_present_string."""

        result = tf.get_present_string(TWITTER_DATA,
                                       ['tomCruise', 'katieH'],
                                       QUERY['PRESENT'])
        msg = '''get_present_string should return a str, but returned {}'''
        self.assertTrue(isinstance(result, str), msg.format(type(result)))

        msg = '''incorrect formatting of presentation string, expected {}\n got {}\n'''
        self.assertEqual(result, LONG_RESULT, msg.format(LONG_RESULT, result))

    def test_all_followers(self) -> None:
        """Test function all_followers."""

        self._test_returns_list_of(tf.all_followers,
                                   [TWITTER_DATA, 'katieH'], [str])

    def test_check_constants(self) -> None:
        """Values of constants."""

        print('\nChecking that constants refer to their original values')
        self._check_constants(CONSTANTS, tf)
        print('  check complete')

    def _test_returns_list_of(self, func, args, types):
        """Check that func when called with args returns a list of elements
        of typef from types.

        """

        result = checker_generic.type_check_simple(func, args, list)
        self.assertTrue(result[0], result[1])

        msg = '{} should return a list of length {}'
        self.assertEqual(len(result[1]), len(types),
                         msg.format(func.__name__, len(types)))

        msg = ('Element at index {} in the list returned by {} '
               'should be of type {}. Got {}.')
        for i, typ in enumerate(types):
            self.assertTrue(isinstance(result[1][i], typ),
                            msg.format(i, func.__name__, typ, result[1][i]))

    def _has_these_keys(self, result: object, valid_keys: List[str], msg: str):
        """Check if result is a dict with keys from a set of valid keys.
        """
        self.assertTrue(isinstance(result, dict), msg)

        for k in result:
            self.assertTrue(k in valid_keys,
                            msg + ', but key ' + str(k) + ' is not in ' +
                            str(valid_keys))

    def _is_dict_of_Ks_and_Vs(self, result: object, key_tp: type,
                              val_tp: type, msg: str):
        """Check if result is a dict with keys of type key_tp and values
         of type val_tp.
        """

        self.assertTrue(isinstance(result, dict), msg)

        for (key, val) in result.items():
            self.assertTrue(isinstance(key, key_tp),
                            (msg + ', but one or more keys is not of type '
                             + str(key_tp)))
            self.assertTrue(isinstance(val, val_tp),
                            (msg + ', but value ' + str(val) + ' is not of type '
                             + str(val_tp)))

    def _check_simple_type(self, func: callable, args: list,
                           expected: type) -> None:
        """Check that func called with arguments args returns a value of type
        expected. Display the progress and the result of the check.

        """

        print('\nChecking {}...'.format(func.__name__))
        result = checker_generic.type_check_simple(func, args, expected)
        self.assertTrue(result[0], result[1])
        print('  check complete')

    def _test_returns_list_of(self, func, args, types):
        """Check that func when called with args returns a list of elements
        of typef from types.

        """

        print('\nChecking {}...'.format(func.__name__))

        result = checker_generic.type_check_simple(func, args, list)
        self.assertTrue(result[0], result[1])

        msg = '{} should return a list of length {}'
        self.assertEqual(len(result[1]), len(types),
                         msg.format(func.__name__, len(types)))

        msg = ('Element at index {} in the list returned by get_station '
               'should be of type {}. Got {}.')
        for i, typ in enumerate(types):
            self.assertTrue(isinstance(result[1][i], typ),
                            msg.format(i, typ, result[1][i]))

        print('  check complete')

    def _check_constants(self, name2value: Dict[str, object], mod: Any) -> None:
        """Check that, for each (name, value) pair in name2value, the value of
        a variable named name in module mod is value.
        """

        for name, expected in name2value.items():
            actual = getattr(mod, name)
            msg = 'The value of constant {} should be {} but is {}.'.format(
                name, expected, actual)
            self.assertEqual(expected, actual, msg)


checker_generic.ensure_no_io('twitterverse_functions')

print(''.center(TARGET_LEN, SEP))
print(' Start: checking coding style '.center(TARGET_LEN, SEP))
checker_generic.run_pyta(FILENAME, PYTA_CONFIG)
print(' End checking coding style '.center(TARGET_LEN, SEP))

print(' Start: checking type contracts '.center(TARGET_LEN, SEP))
unittest.main(exit=False)
print(' End checking type contracts '.center(TARGET_LEN, SEP))

print('\nScroll up to see ALL RESULTS:')
print('  - checking coding style')
print('  - checking type contract\n')
