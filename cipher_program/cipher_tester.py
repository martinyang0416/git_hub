"""Use this file to organize your A2 testing.

This module is for your own use. You will not hand this in. You can do
anything you like in here and nobody will notice.  :-)

Here are some suggested uses:
+ Create constants for decks, plaintext, and encrypted messages
+ Write a set of tests for each function
  - We recommend you try to mimic our clean_message testing structure below
  - If you do, it's easy to comment and uncomment the tests you want to run
    at the bottom of this module.
+ If you can't figure out why one of your tests is failing, bring this to
  office hours!

If this starts to get big and unwieldy, you might find it more manageable
if you create a test module for each of the A2 functions.
"""

# This makes function clean_message available in this module.
from cipher_functions import clean_message


def test_clean_message(dirty_msg: str, expected: str):
    """Test whether function call clean_message(dirty_msg) produces value
    expected. Print an error message if the test fails; otherwise exit
    silently."""
    if clean_message(dirty_msg) != expected:
        print('Error: clean_message("{}") != "{}"'.format(dirty_msg, expected))


def test_clean_messages():
    """Run the clean_message tests."""

    # From the doctests in the starter code.
    test_clean_message('Hello world!', 'HELLOWORLD')
    test_clean_message("Python? It's my favourite language.",
                       'PYTHONITSMYFAVOURITELANGUAGE')
    test_clean_message('88test', 'TEST')

    # Other tests.
    test_clean_message('', '')
    test_clean_message('???', '')

    # The next one fails. Feel free to delete it.
    test_clean_message('Bad test!', 'BAD TEST!')


if __name__ == '__main__':
    test_clean_messages()
