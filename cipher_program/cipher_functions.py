"""CSC108 Assignment 2 functions"""

from typing import List

# Used to determine whether to encrypt or decrypt
ENCRYPT = 'e'
DECRYPT = 'd'


def clean_message(message: str) -> str:
    """Return a string with only uppercase letters from message with non-
    alphabetic characters removed.
    
    >>> clean_message('Hello world!')
    'HELLOWORLD'
    >>> clean_message("Python? It's my favourite language.")
    'PYTHONITSMYFAVOURITELANGUAGE'
    >>> clean_message('88test')
    'TEST'
    """
    output = ""
    for char in message:
        if char in "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM":
            output = output + char
    return output.upper()


def encrypt_letter(letter: str, num: int) -> str:
    """Return the encrypted letter by shifting num places to right
    
    >>> encrypt_letter('A',3)
    'D'
    >>> encrypt_letter('C',3)
    'F'
    """
    ord_differ = ord(letter) - ord("A")
    new_char_ord = (ord_differ + num) % 26
    return chr(new_char_ord + ord("A"))


def decrypt_letter(letter: str, num: int) -> str:
    """ Return the decrypted letter by shifting num places to left
    
    >>> encrypt_letter('D',3)
    'A'
    >>> encrypt_letter('C',1)
    'B'
    """
    ord_differ = ord(letter) - ord("A")
    new_char_ord = (ord_differ - num) % 26
    return chr(new_char_ord - ord("A"))


def is_valid_deck(valid: List[int]) -> bool:
    """Return True if and only if every integer is contained in the valid deck
    
    >>> is_valid_deck([1,2,3])
    True
    >>> is_valid_deck([])
    False
    """
    if valid == []:
        return False
    for i in range(len(valid)):
        if i + 1 != (sorted(valid))[i]:
            return False
    return True


def swap_cards(valid_deck: List[int], valid_index: int) -> None:
    """Swap the cards at the valid index within the valid deck
    """
    i = valid_index
    first_index = valid_deck[0]
    chosen_index = valid_deck[i]
    last_index = valid_deck[-1]
    if i == len(valid_deck) - 1:
        valid_deck[0] = last_index
        valid_deck[-1] = first_index
    else:
        rest_index = valid_deck[i + 1]
        valid_deck[i + 1] = chosen_index
        valid_deck[i] = rest_index


def get_small_joker_value(deck: List[int]) -> int:
    """Return the index of the small joker in the <deck>.

    >>> get_small_joker_value([1, 5, 3, 2, 4])
    4
    >>> get_small_joker_value([6, 3, 4, 2, 5, 1])
    4
    """
    return deck.index(len(deck) - 1)


def get_big_joker_value(valid_deck: List[int]) -> int:
    """Return the index of the big joker in the <deck>.

    >>> get_big_joker_value([5, 4, 3, 2, 1])
    0
    >>> get_big_joker_value([1, 2, 3, 4, 5, 6])
    5
    """
    return valid_deck.index(len(valid_deck))


def move_small_joker(valid_deck: List[int]) -> None:
    """Swap the small joker with the next card
    
    >>> valid_deck = [1, 2, 3, 4, 5]
    >>> move_small_joker(valid_deck)
    >>> valid_deck
    [1, 2, 3, 5, 4]
    >>> valid_deck = [3, 2, 1, 4]
    >>> move_small_joker(valid_deck)
    >>> valid_deck
    [2, 3, 1, 4]
    """
    first_index = valid_deck.index(get_small_joker_value(valid_deck))
    swap_cards(valid_deck, first_index)


def move_big_joker(valid_deck: List[int]) -> None:
    """Moving the big joker two index to the right
    
    >>>valid_deck = [5,4,3,2,1]
    >>>move_big_joker(valid_deck)
    >>>valid_deck
    [4,3,5,2,1]
    """
    big_joker = valid_deck.index(get_big_joker_value(valid_deck))
    swap_cards(valid_deck, big_joker)
    big_joker_2 = valid_deck.index(get_big_joker_value(valid_deck))
    swap_cards(valid_deck, big_joker_2)


def triple_cut(valid_deck: List[int]) -> None:
    """Swith the positions of the cards above left joker and the cards below
    right joker (we will have three different situations)
    
    >>> valid_deck = [5,4,3,2,1]
    triple_cut(valid_deck)
    valid_deck = [3,2,1,5,4]
    """
    a = valid_deck.index(get_big_joker_value(valid_deck))
    b = valid_deck.index(get_small_joker_value(valid_deck))
    l_index = min(a, b)
    r_index = max(a, b)
    l_cards = valid_deck[:l_index]
    r_cards = valid_deck[r_index + 1:]
    m_cards = valid_deck[l_index:r_index + 1]
    for _ in range(len(valid_deck)):
        valid_deck.pop(0)
    valid_deck.extend(r_cards)
    valid_deck.extend(m_cards)
    valid_deck.extend(l_cards)


def insert_top_to_bottom(valid_deck: List[int]) -> None:
    """Insert the cards from the top to bottom
    
    >>>valid_deck = [5,4,3,2,1]
    insert_top_to_bottom(valid_deck)
    valid_deck = [4,3,2,1,5]
    """
    chosen_num = valid_deck[:valid_deck[-1]]
    other_num = valid_deck[valid_deck[-1]:-1]
    valid_deck[valid_deck[-1]:-1] = chosen_num
    valid_deck[:valid_deck[-1]] = other_num


def get_card_at_top_index(valid_deck: List[int]) -> int:
    """Return the integer in the valid deck at the index of the value
    of top cards
    
    >>>valid_deck = [2,3,1,4,5,6]
    get_card_at_top_index(valid_deck)
    1
    """
    if valid_deck[0] == get_big_joker_value(valid_deck):
        return valid_deck[valid_deck[0] - 1]
    else:
        return valid_deck[valid_deck[0]]


def get_next_keystream_value(valid_deck: List[int]) -> int:
    """Return the valid keystream value after all the algorithms are done
    
    >>> valid_deck = [7,6,5,4,3,2,1]
    get_next_keystream_value(valid_deck)
    6
    """
    move_small_joker(valid_deck)
    move_big_joker(valid_deck)
    triple_cut(valid_deck)
    insert_top_to_bottom(valid_deck)
    return get_card_at_top_index(valid_deck)


def process_messages(valid_deck: List[int], message: List[str],
                     encrypt_or_decrypt: str) -> List[str]:
    """Return a list of encrypted or decrypted message
    """
    list_a = []
    list_b = []
    list_c = message_helper(clean_message(message))

    while len(list_a) != len(list_c):
        list_a.append(get_next_keystream_value(valid_deck))
    if encrypt_or_decrypt == ENCRYPT:
        for i in range(len(list_c)):
            list_b.append((list_c[i] + list_a[i]) % len(valid_deck))
        return number_helper(list_b)
    if encrypt_or_decrypt == DECRYPT:
        for i in range(len(list_c)):
            list_b.append((list_a[i] - list_c[i]) % len(valid_deck))
        return number_helper(list_b)
    else:
        return 'Invalid'


def number_helper(number: List[int]) -> List[int]:
    """Return integers to be alphabetically ordered
    """
    n = []
    for i in number:
        ord_sum = i + ord('A')
        n.append(chr(ord_sum))
    return n


def message_helper(message: str) -> List[int]:
    """Return message from alphabets to integers
    """
    n = []
    for i in message:
        ord_difference = ord(i) - ord('A')
        n.append(ord_difference)
    return n


# This if statement should always be the last thing in the file, below all of
# your functions:
if __name__ == '__main__':
    """Did you know that you can get Python to automatically run and check
    your docstring examples? These examples are called "doctests".

    To make this happen, just run this file! The two lines below do all
    the work.

    For each doctest, Python does the function call and then compares the
    output to your expected result.

    NOTE: your docstrings MUST be properly formatted for this to work!
    In particular, you need a space after each >>>. Otherwise Python won't
    be able to detect the example.
    """

    import doctest

    doctest.testmod()
