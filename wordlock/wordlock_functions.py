"""Starter code for CSC108 Assignment 1 Winter 2020"""

# Game setting constants
SECTION_LENGTH = 3
ANSWER = 'CATDOGFOXEMU'

# Move constants
SWAP = 'S'
ROTATE = 'R'
CHECK = 'C'


def get_section_start(num: int) -> int:
    """ Return the first index of the section matches to num.

    >>> get_section_start(1)
    0
    >>> get_section_start(4)
    9
    """

    return SECTION_LENGTH * (num - 1)


def is_valid_move(move: str) -> bool:
    """ Return True iff move represents a valid move

    >>> is_valid_move('S')
    True
    >>> is_valid_move('ABCDEF')
    False
    """
    return move == SWAP or move == ROTATE or move == CHECK


def is_valid_section(num: int) -> bool:
    """ Return True iff num is a valid section.
    >>> is_valid_section(2)
    True
    >>> is_valid_section(10000000)
    False
    """
    return (0 < num <= len(ANSWER) / SECTION_LENGTH)


def check_section(game: str, num: int) -> bool:
    """ Return True iff game is matched the num of ANSWER.
    >>> check_section('CATDOGFOXEMU', 1)
    True
    >>> check_section('lolyyhnb123', 1)
    False
    """
    
    return (ANSWER[get_section_start(num) : get_section_start(num)
                   + SECTION_LENGTH] == game[get_section_start(num) : 
                                             get_section_start(num) + 
                                             SECTION_LENGTH])


def change_state(state: str, num: int, step: str) -> str:
    """ Return a new string which is uploaded state with given num
    from step that user chooses.
    >>> change_state('ACTDOGFOXEMU', 2, 'S')
    'ACTGODFOXEMU'
    >>> change_state('ACTDOGFOXEMU', 2, 'R')
    'ACTGDOFOXEMU'
    """
    go1 = get_section_start(num)
    what = state[go1 : go1 + SECTION_LENGTH]
    if step == SWAP:
        swap_string = what[-1] + what[1:len(what)-1] + what[0]
        state = state[0 : go1] + swap_string + state[go1 + SECTION_LENGTH:]
        return state
    else:
        rotate_string = what[-1] + what[:-1]
        state = state[0 : go1] + rotate_string + state[go1 + SECTION_LENGTH:]
        return state


def get_move_hint(game: str, num: int) -> str:
    """
    Return a move choice that will help the game get the right ANSWER 
    in a specific num.
    >>> get_move_hint('ACTDOGFOXEMU', 3)
    'R'
    >>> get_move_hint('TACDOGFOXEMU', 4)
    'R'
    >>> get_move_hint('TACDOGFOXEMU', 1)
    'S'
    """
    sec_str = game[3 * (num - 1) : 3 * (num - 1)+3]
    if sec_str[0] == ANSWER[3 * (num - 1) : 3 * (num - 1) +3][-1] and\
       sec_str[-1] == ANSWER[3 * (num - 1) : 3 * (num - 1) +3][0]:
        return SWAP
    else:
        return ROTATE
