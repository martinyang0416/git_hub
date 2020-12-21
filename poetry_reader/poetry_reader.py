"""Functions for reading the pronouncing dictionary and the poetry forms files
"""
from typing import TextIO

from poetry_constants import (
    # CLEAN_POEM, WORD_PHONEMES, LINE_PRONUNCIATION, POEM_PRONUNCIATION,
    PRONOUNCING_DICTIONARY, POETRY_FORM, POETRY_FORMS)

SAMPLE_POETRY_FORM_FILE = '''Limerick
8 A
8 A
5 B
5 B
8 A

Haiku
5 *
7 * 
5 *
'''
EXPECTED_POETRY_FORMS = {
    'Haiku': ([5, 7, 5], ['*', '*', '*']),
    'Limerick': ([8, 8, 5, 5, 8], ['A', 'A', 'B', 'B', 'A'])
}

SAMPLE_DICTIONARY_FILE = ''';;; Comment line
ABSINTHE  AE1 B S IH0 N TH
HEART  HH AA1 R T
FONDER  F AA1 N D ER0
'''

EXPECTED_DICTIONARY = {
    'ABSINTHE': ['AE1', 'B', 'S', 'IH0', 'N', 'TH'],
    'HEART': ['HH', 'AA1', 'R', 'T', ],
    'FONDER': ['F', 'AA1', 'N', 'D', 'ER0']
}

SAMPLE_POEM_FILE = '''  Is this mic on?

Get off my lawn.
'''


def read_and_trim_whitespace(poem_file: TextIO) -> str:
    """Return a string containing the poem in poem_file, with
    blank lines and leading and trailing whitespace removed.

    >>> import io
    >>> poem_file = io.StringIO(SAMPLE_POEM_FILE)
    >>> read_and_trim_whitespace(poem_file)
    'Is this mic on?\\nGet off my lawn.'
    """

    # 宝贝，你看到之后把你自己写的第一个补充上去，底下的两个我帮你写完了！么么！


def read_pronouncing_dictionary(
        pronunciation_file: TextIO) -> PRONOUNCING_DICTIONARY:
    """Read pronunciation_file, which is in the format of the CMU Pronouncing
    Dictionary, and return the pronunciation dictionary.

    >>> import io
    >>> dict_file = io.StringIO(SAMPLE_DICTIONARY_FILE)
    >>> result = read_pronouncing_dictionary(dict_file)
    >>> result == EXPECTED_DICTIONARY
    True
    """

    accumulator = {}
    for line in pronunciation_file.readlines():
        right_now = line.split()
        if right_now[0].isupper():
            accumulator[right_now[0]] = []
            for i in range(1, len(right_now)):
                accumulator[right_now[0]].append(right_now[i])
    return accumulator


def read_poetry_form_descriptions(
        poetry_forms_file: TextIO) -> POETRY_FORMS:
    """Return a dictionary of poetry form name to poetry pattern for the poetry
    forms in poetry_forms_file.

    >>> import io
    >>> form_file = io.StringIO(SAMPLE_POETRY_FORM_FILE)
    >>> result = read_poetry_form_descriptions(form_file)
    >>> result == EXPECTED_POETRY_FORMS
    True
    """

    result = {}
    line = poetry_forms_file.readline()
    while line != '':
        key = None
        if line[0].isalpha():
            key = line.strip()
            result[key] = ([], [])
        elif line != '\n':
            result[key][0].append(int(line.strip().split()[0]))
            result[key][1].append(line.strip().split()[1])
        line = poetry_forms_file.readline()
    return result


if __name__ == '__main__':
    import doctest
    import python_ta
    python_ta.check_all()
    doctest.testmod()
