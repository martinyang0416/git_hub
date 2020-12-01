"""CSC148 Assignment 2

=== CSC148 Winter 2020 ===
Department of Computer Science,
University of Toronto

This code is provided solely for the personal and private use of
students taking the CSC148 course at the University of Toronto.
Copying for purposes other than this use is expressly prohibited.
All forms of distribution of this code, whether as given or with
any changes, are expressly prohibited.

Authors: Diane Horton, David Liu, Mario Badr, Sophia Huynh, Misha Schwartz,
and Jaisie Sin

All of the files in this directory and all subdirectories are:
Copyright (c) Diane Horton, David Liu, Mario Badr, Sophia Huynh,
Misha Schwartz, and Jaisie Sin

=== Module Description ===

This file contains the hierarchy of Goal classes.
"""
from __future__ import annotations
import random
from typing import List, Tuple
from block import Block
from settings import colour_name, COLOUR_LIST


def generate_goals(num_goals: int) -> List[Goal]:
    """Return a randomly generated list of goals with length num_goals.

    All elements of the list must be the same type of goal, but each goal
    must have a different randomly generated colour from COLOUR_LIST. No two
    goals can have the same colour.

    Precondition:
        - num_goals <= len(COLOUR_LIST)
    """
    random.shuffle(COLOUR_LIST)
    goals = [[PerimeterGoal(COLOUR_LIST[i]) for i in range(num_goals)],
             [BlobGoal(COLOUR_LIST[i]) for i in range(num_goals)]]
    return random.choice(goals)


def _flatten(block: Block) -> List[List[Tuple[int, int, int]]]:
    """Return a two-dimensional list representing <block> as rows and columns of
    unit cells.

    Return a list of lists L, where,
    for 0 <= i, j < 2^{max_depth - self.level}
        - L[i] represents column i and
        - L[i][j] represents the unit cell at column i and row j.

    Each unit cell is represented by a tuple of 3 ints, which is the colour
    of the block at the cell location[i][j]

    L[0][0] represents the unit cell in the upper left corner of the Block.
    """
    if len(block.children) == 0:
        length = 2 ** (block.max_depth - block.level)
        return [[block.colour for _ in range(length)] for _ in range(length)]
    else:
        flatten_lst = []
        for i in range(len(_flatten(block.children[1]))):
            flatten_lst.append(_flatten(block.children[1])[i] +
                               _flatten(block.children[2])[i])
        for j in range(len(_flatten(block.children[0]))):
            flatten_lst.append(_flatten(block.children[0])[j] +
                               _flatten(block.children[3])[j])
        return flatten_lst


class Goal:
    """A player goal in the game of Blocky.

    This is an abstract class. Only child classes should be instantiated.

    === Attributes ===
    colour:
        The target colour for this goal, that is the colour to which
        this goal applies.
    """
    colour: Tuple[int, int, int]

    def __init__(self, target_colour: Tuple[int, int, int]) -> None:
        """Initialize this goal to have the given target colour.
        """
        self.colour = target_colour

    def score(self, board: Block) -> int:
        """Return the current score for this goal on the given board.

        The score is always greater than or equal to 0.
        """
        raise NotImplementedError

    def description(self) -> str:
        """Return a description of this goal.
        """
        raise NotImplementedError


class PerimeterGoal(Goal):
    """A player goal in the game of Blocky which the player's score is
    calculated based on the goal colour on the outer perimeter of the board.
    The total score will be the total number of unit cells of the colour that
    are on the perimeter. The corner cell count as twice towards the score.
    """
    def score(self, board: Block) -> int:
        """Return score according to the number of unit cell of the target
        colour in the outer perimeter of the <board>. The corner cells are
        count twice towards the score.
        """
        flatten_board = _flatten(board)
        score = 0
        for i in range(len(flatten_board)):
            if flatten_board[0][i] == self.colour:
                if i in [0, len(flatten_board) - 1]:
                    score += 2
                else:
                    score += 1
            if flatten_board[-1][i] == self.colour:
                if i in [0, len(flatten_board) - 1]:
                    score += 2
                else:
                    score += 1
        for j in range(1, len(flatten_board) - 1):
            if flatten_board[j][0] == self.colour:
                score += 1
            if flatten_board[j][-1] == self.colour:
                score += 1
        return score

    def description(self) -> str:
        """Return a description of perimeter goal.
        """
        return 'Player must aim to put the most possible units of {0} on the ' \
               'outer perimeter of the board. '.format(colour_name(self.colour))


class BlobGoal(Goal):
    """A player goal in the game of Blocky which the player's score is
    calculated based on total number of the unit cells of the goal colour in
    the biggest blob in the board. A blob is a group of connected blocks with
    the same colour. Two blocks are connected if their sides touch; touching
    corners does not count.
    """
    def score(self, board: Block) -> int:
        """Return a score according to size of the largest connected block of
        the target colour on the <board>.
        """
        flat = _flatten(board)
        visited = [[-1 for _ in range(len(flat))] for _ in range(len(flat))]
        score = 0
        for i in range(len(flat)):
            for j in range(len(flat)):
                size = self._undiscovered_blob_size((i, j), flat, visited)
                if size > score:
                    score = size
        return score

    def _undiscovered_blob_size(self, pos: Tuple[int, int],
                                board: List[List[Tuple[int, int, int]]],
                                visited: List[List[int]]) -> int:
        """Return the size of the largest connected blob that (a) is of this
        Goal's target colour, (b) includes the cell at <pos>, and (c) involves
        only cells that have never been visited.

        If <pos> is out of bounds for <board>, return 0.

        <board> is the flattened board on which to search for the blob.
        <visited> is a parallel structure that, in each cell, contains:
            -1 if this cell has never been visited
            0  if this cell has been visited and discovered
               not to be of the target colour
            1  if this cell has been visited and discovered
               to be of the target colour

        Update <visited> so that all cells that are visited are marked with
        either 0 or 1.
        """
        if pos[0] >= len(board) or pos[1] >= len(board) or \
                pos[0] < 0 or pos[1] < 0:
            return 0
        elif board[pos[0]][pos[1]] == self.colour:
            visited[pos[0]][pos[1]] = 1
            size = 1
            position = [(pos[0] + 1, pos[1]), (pos[0] - 1, pos[1]),
                        (pos[0], pos[1] + 1), (pos[0], pos[1] - 1)]
            for pos_x, pos_y in position:
                if len(visited) > pos_x >= 0 and len(visited) > pos_y >= 0 and \
                        visited[pos_x][pos_y] == -1:
                    size += self._undiscovered_blob_size((pos_x, pos_y), board,
                                                         visited)
            return size
        else:
            visited[pos[0]][pos[1]] = 0
            return 0

    def description(self) -> str:
        """Return a description of blob goal.
        """
        return 'Player must aim for the largest group of connected blocks ' \
               'with the colour {0}'.format(colour_name(self.colour))


if __name__ == '__main__':
    import python_ta

    python_ta.check_all(config={
        'allowed-import-modules': [
            'doctest', 'python_ta', 'random', 'typing', 'block', 'settings',
            'math', '__future__'
        ],
        'max-attributes': 15
    })
