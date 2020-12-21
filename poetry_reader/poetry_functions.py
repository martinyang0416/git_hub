"""Helper functions for the poetry.py program.
"""

from typing import List
from typing import Tuple
from typing import Dict

from poetry_constants import (
    CLEAN_POEM, WORD_PHONEMES, LINE_PRONUNCIATION, POEM_PRONUNCIATION,
    PRONOUNCING_DICTIONARY)


# ===================== Helper Functions =====================


def clean_word(s: str) -> str:
    """Return a new string based on s in which all letters have been converted
    to uppercase and whitespace and punctuation characters have been stripped
    from both ends. Inner punctuation and whitespace is left untouched.

    >>> clean_word('Birthday!!!')
    'BIRTHDAY'
    >>> clean_word('  "Quoted?"\\n\\n\\n')
    'QUOTED'
    """

    punctuation = """!"'`@$%^&_-+={}|\\/,;:.-?)([]<>*#\n\t\r """
    cleaned = s.upper().strip(punctuation)
    return cleaned


def clean_poem(raw_poem: str) -> CLEAN_POEM:
    r"""Return the non-blank, non-empty lines of poem, with whitespace removed
    from the beginning and end of each line and all words capitalized.

    >>> clean_poem('The first line leads off,\n\n\nWith a gap before the next.\n    Then the poem ends.\n')
    [['THE', 'FIRST', 'LINE', 'LEADS', 'OFF'], ['WITH', 'A', 'GAP', 'BEFORE', 'THE', 'NEXT'], ['THEN', 'THE', 'POEM', 'ENDS']]
    """
    poem_line = raw_poem.split("\n")
    sentences = []

    for one_line in poem_line:
        if one_line != "":
            sentences.append(one_line[:-1].upper())

    line_to_word = []
    for one_line in sentences:
        line_to_word.append(one_line.split(" "))

    lst = []
    for line in line_to_word:
        temp = []
        for word in line:
            if word != "":
                temp.append(word)
        lst.append(temp)

    return lst


def extract_phonemes(
        cleaned_poem: CLEAN_POEM,
        word_to_phonemes: PRONOUNCING_DICTIONARY) -> POEM_PRONUNCIATION:
    """Return a list where each inner list contains the phonemes for the
    corresponding line of poem_lines.

    >>> word_to_phonemes = {'YES': ['Y', 'EH1', 'S'], 'NO': ['N', 'OW1']}
    >>> extract_phonemes([['YES'], ['NO', 'YES']], word_to_phonemes)
    [[['Y', 'EH1', 'S']], [['N', 'OW1'], ['Y', 'EH1', 'S']]]
    """
    lst = []
    for sentence in cleaned_poem:
        temp = []
        for word in sentence:
            temp.append(word_to_phonemes[word])
        lst.append(temp)

    return lst


def phonemes_to_str(poem_pronunciation: POEM_PRONUNCIATION) -> str:
    """Return a string containing all the phonemes in each word in each line in
    poem_pronunciation. The phonemes are separated by spaces, the words are
    separated by ' | ', and the lines are separated by '\n'.

    >>> phonemes_to_str([[['Y', 'EH1', 'S']], [['N', 'OW1'], ['Y', 'EH1', 'S']]])
    'Y EH1 S\\nN OW1 | Y EH1 S'
    """
    lst = ""
    for line in poem_pronunciation:
        temp_line = ""
        for word in line:
            temp_p = ""
            for phonemes in word:
                temp_p += phonemes
                temp_p += " "
            temp_line += temp_p
            temp_line += "| "
        temp_line = temp_line[:-3]
        lst += temp_line
        lst += "\n"
    lst = lst[:-1]
    return lst


def get_rhyme_scheme(poem_pronunciation: POEM_PRONUNCIATION) -> List[str]:
    """Return a list of last syllables from the poem described by
    poem_pronunction.

    Precondition: poem_pronunciation is not empty and each PHONEMES list
    contains at least one vowel phoneme.

    >>> get_rhyme_scheme([[['IH0', 'N']], [['S', 'IH0', 'N']]])
    ['A', 'A']
    """
    endings = []
    for sentence in poem_pronunciation:
        last = sentence[-1]
        if len(last[-1]) == 1 or len(last[-1]) == 2:
            endings.append(last[-2] + last[-1])
        elif len(last_word[-1]) == 3:
            endings.append(last_word[-1])

    letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    letter_dic = {}
    for item in endings:
        if item not in letter_dic:
            letter_dic[item] = letters[0]
            letters = letters[1:]

    lst = []
    for ending in endings:
        lst.append(letter_dic[ending])
    return lst


def get_num_syllables(poem_pronunciation: POEM_PRONUNCIATION) -> List[int]:
    """Return a list of the number of syllables in each poem_pronunciation
    line.
    """
    lst = []
    for line in poem_pronunciation:
        for word in line:
            i = 0
            for phoneme in word:
                if len(phoneme) == 3:
                    i += 1
            lst.append(i)
    return lst


if __name__ == '__main__':
    import doctest
    import python_ta
    python_ta.check_all()
    doctest.testmod()
