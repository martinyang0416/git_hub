"""CSC108/A08: Fall 2020 -- Assignment 3: Twitterverse

This code is provided solely for the personal and private use of
students taking the CSC108 course at the University of
Toronto. Copying for purposes other than this use is expressly
prohibited. All forms of distribution of this code, whether as given
or with any changes, are expressly prohibited.

All of the files in this directory and all subdirectories are:
Copyright (c) 2020 Mario Badr, Jennifer Campbell, Tom Fairgrieve,
Diane Horton, Michael Liut, Jacqueline Smith, and Anya Tafliovich.

"""

from typing import Callable, List, TextIO
from constants import (TwitterverseDict, SearchDict, FilterDict,
                       PresentDict, QueryDict)
from constants import (NAME, LOCATION, WEB, BIO, FOLLOWING, USERNAME,
                       OPERATIONS, FOLLOWER, FOLLOWERS, NAME_INCLUDES,
                       LOCATION_INCLUDES, SORT_BY, FORMAT, SEARCH,
                       FILTER, PRESENT, POPULARITY, END, ENDBIO, LONG)


HANDOUT_DATA = {
    'tomCruise': {
        'name': 'Tom Cruise',
        'bio': ('Official TomCruise.com crew tweets. We love you guys!\n' +
                'Visit us at Facebook!'),
        'location': 'Los Angeles, CA',
        'web': 'http://www.tomcruise.com',
        'following': ['katieH', 'NicoleKidman']},
    'PerezHilton': {
        'name': 'Perez Hilton',
        'bio': ('Perez Hilton is the creator and writer of one of the most ' +
                'famous websites\nin the world. And he also loves music -' +
                'a lot!'),
        'location': 'Hollywood, California',
        'web': 'http://www.PerezH...',
        'following': ['tomCruise', 'katieH', 'NicoleKidman']}}

HANDOUT_QUERY = {
    'SEARCH': {'username': 'tomCruise',
               'operations': ['following', 'followers']},
    'FILTER': {'following': 'katieH'},
    'PRESENT': {'sort-by': 'username',
                'format': 'short'}
}

############################################################################


############################################################################
# Provided helper functions
############################################################################


def tweet_sort(twitter_data: TwitterverseDict,
               usernames: List[str],
               sort_spec: str) -> None:
    """Sort usernames based on the sorting specification in sort_spec
    using the data in twitter_data.

    >>> usernames = ['tomCruise', 'PerezHilton']
    >>> tweet_sort(HANDOUT_DATA, usernames, 'username')
    >>> usernames == ['PerezHilton', 'tomCruise']
    True
    >>> tweet_sort(HANDOUT_DATA, usernames, 'popularity')
    >>> usernames == ['tomCruise', 'PerezHilton']
    True
    >>> tweet_sort(HANDOUT_DATA, usernames, 'name')
    >>> usernames == ['PerezHilton', 'tomCruise']
    True

    """

    usernames.sort()  # sort by username first
    if sort_spec in SORT_FUNCS:
        SORT_FUNCS[sort_spec](twitter_data, usernames)


def by_popularity(twitter_data: TwitterverseDict, usernames: List[str]) -> None:
    """Sort usernames in descending order based on popularity (number of
    users that follow a gien user) in twitter_data.

    >>> usernames = ['PerezHilton', 'tomCruise']
    >>> by_popularity(HANDOUT_DATA, usernames)
    >>> usernames == ['tomCruise', 'PerezHilton']
    True

    """

    def get_popularity(username: str) -> int:
        return len(all_followers(twitter_data, username))

    usernames.sort(key=get_popularity, reverse=True)


def by_name(twitter_data: TwitterverseDict, usernames: List[str]) -> None:
    """Sort usernames in ascending order based on name in twitter_data.

    >>> usernames = ['tomCruise', 'PerezHilton']
    >>> by_name(HANDOUT_DATA, usernames)
    >>> usernames == ['PerezHilton', 'tomCruise']
    True

    """

    def get_name(username: str) -> str:
        return twitter_data.get(username, {}).get(NAME, '')

    usernames.sort(key=get_name)


def format_report(twitter_data: TwitterverseDict,
                  usernames: List[str],
                  format_spec: str) -> str:
    """Return a string representing usernames presented as specified by
    the format specification format_spec.

    Precondition: each username in usernames is in twitter_data
    """

    if format_spec == LONG:
        result = '-' * 10 + '\n'
        for user in usernames:
            result += format_details(twitter_data, user)
            result += '-' * 10 + '\n'
        return result
    return str(usernames)


def format_details(twitter_data: TwitterverseDict, username: str) -> str:
    """Return a string representing the long format of username's info in
    twitter_data.

    Precondition: username is in twitter_data
    """

    user_data = twitter_data[username]
    return ("{}\nname: {}\nlocation: {}\nwebsite: {}\nbio:\n{}\n" +
            "following: {}\n").format(username, user_data[NAME],
                                      user_data[LOCATION],
                                      user_data[WEB], user_data[BIO],
                                      user_data[FOLLOWING])


############################################################################


SORT_FUNCS = {POPULARITY: by_popularity,
              NAME: by_name}


if __name__ == '__main__':
    import doctest
    doctest.testmod()
