"""Starter code for CSC108 Assignment 1 Winter 2020"""

# Game setting constants
SECTION_LENGTH = 3
ANSWER = 'CATDOGFOXEMU'

# Move constants
SWAP = 'S'
ROTATE = 'R'
CHECK = 'C'

def get_section_start(section_num: int) -> int:
    """ Return the starting index of the section corresponding to section_num.
    
    >>> get_section_start(1)
    0
    >>> get_section_start(3)
    6
    """
    return SECTION_LENGTH * (section_num - 1)


def is_valid_move(move: str) -> bool:
    """ Return True if and only if the parameter move is a valid
    move.
    
    >>> is_valid_move('R')
    True
    >>> is_valid_move('B')
    False
    """
    return move == ROTATE or move == SWAP or move == CHECK

def is_valid_section(valid_sec: int) -> bool:
    """Return True if and only if valid_sec represents a section number 
    that is valid for the current answer string and section length.
    
    >>> is_valid_section(1)
    True
    >>> is_valid_section(8)
    False
    """
    return 0 < valid_sec <= (len(ANSWER) // SECTION_LENGTH)

def check_section(play: str, num: int) -> bool:
    """Return True if and only if the specified section given by num
    in play matches the same section in the answer string.
    
    >>> check_section('BBBDOGDDDDDD',2)
    True
    >>> check_section('SSSDOGSSSSSS',1)
    False
    """
    start = get_section_start(num)
    if play[get_section_start(num):get_section_start(num)\
              + SECTION_LENGTH] == ANSWER[get_section_start(num):\
                                          get_section_start(num)+\
                                          SECTION_LENGTH]:
        return True
    else:
        return False

def change_state(count: str, section: int, move:str) -> str:
    """The function should return a new string that shows the updated game
    after executing the given move to the specified section.
    
    >>> change_state('abcdef', 2, 'S')
    'abcfed'
    >> change_state('abcdef', 1, 'R')
    'cabdef'
    """
    start = get_section_start(section)
    string = count[start:start + SECTION_LENGTH]
    if move == SWAP:
        count = count[0:start] + string[-1] + string[1:len(string)-1] +\
            string[0] + count[start+SECTION_LENGTH:]
    else:
        count = count[0:start] + string[-1] +\
            string[:-1] + count[start+SECTION_LENGTH:]
    return count

def get_move_hint(string: str, num: int) -> str:
    """This function should return a hint of how to move after 
    executing it.
    
    >>> get_move_hint("CATDOGFOXMUE", 4)
    'R'
    >>> get_move_hint("GODFXO", 1)
    'S'
    """
    
    start = get_section_start(num)
    end = start + SECTION_LENGTH 
    if string[start + 1] != ANSWER[start + 1]:
        return ROTATE
    elif string[start : end] != ANSWER[start : end]:
        return SWAP
    else:
        return None
