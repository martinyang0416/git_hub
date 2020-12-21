"""FILE FOR ASSIGNMENT"""
from typing import List


class Definition:
    """ A class that shows definition of a word."""

    def __init__(self, definition: str, part_of_speech: str) -> None:
        self.definition = definition
        self.part_of_speech = part_of_speech

    def __repr__(self) -> str:
        s = self.part_of_speech.upper()
        s += ". "
        s += self.definition

        return s


class Entry:
    """ A class that shows entries with word and definitions. """

    def __init__(self, word: str, definitions: List[Definition]) -> None:
        self.word = word
        self.definitions = definitions

    def __repr__(self) -> str:
        i = 1
        s = self.word
        s += ":\n"

        for item in self.definitions:
            s += "{}. {}\n".format(i, str(item))
            i += 1

        return s
