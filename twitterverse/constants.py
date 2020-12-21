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

from typing import Dict, List, Union


# Twitterverse dictionary:
#     - each key is a username(a str)
#     - each value is a dict with items as follows:
#         - key NAME, value represents a user's name(a str)
#         - key LOCATION, value represents a user's location(a str)
#         - key WEB, value represents a user's website(a str)
#         - key BIO, value represents a user's bio(a str)
#         - key FOLLOWING, value usernames of users this user is following
#           (a list of str)
TwitterverseDict = Dict[str, Dict[str, Union[str, List[str]]]]

# Search specification dictionary:
#     - key USERNAME, value represents username to begin search at(a str)
#     - key OPERATIONS, value represents the operations to perform
#       (a list of str)
SearchDict = Dict[str, Union[str, List[str]]]

# Filter specification dictionary:
#     - key FOLLOWING might exist, value represents a username(a str)
#     - key FOLLOWER might exist, value represents a username(a str)
#     - key NAME_INCLUDES might exist, value represents str to match
#       (a case-insensitive match)
#     - key LOCATION_INCLUDES might exist, value represents str to match
#       (a case-insensitive match)
FilterDict = Dict[str, str]

# Presentation specification dictionary:
#     - key SORT_BY, value represents how to sort results(a str)
#     - key FORMAT, value represents how to format results(a str)
PresentDict = Dict[str, str]

# Query dictionary:
#     - key SEARCH, value represents a search specification dictionary
#     - key FILTER, value represents a filter specification dictionary
#     - key PRESENT, value represents a presentation specification dictionary
QueryDict = Dict[str, Union[SearchDict, FilterDict, PresentDict]]

# Twitter data
USERNAME = 'username'
NAME = 'name'
LOCATION = 'location'
WEB = 'web'
BIO = 'bio'
FOLLOWING = 'following'
ENDBIO = 'ENDBIO'
END = 'END'

# Query specifications
SEARCH = 'SEARCH'
FILTER = 'FILTER'
PRESENT = 'PRESENT'

OPERATIONS = 'operations'
FOLLOWER = 'follower'
FOLLOWERS = 'followers'

NAME_INCLUDES = 'name-includes'
LOCATION_INCLUDES = 'location-includes'

SORT_BY = 'sort-by'
POPULARITY = 'popularity'
FORMAT = 'format'

LONG = 'long'
