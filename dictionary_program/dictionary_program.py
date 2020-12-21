from typing import List
from dictionary_class import MyDictionary
from word import Entry, Definition

SUGGEST_WORD = True


def show_options(options: List[str]) -> None:
    """ Return None by a series of calculations inside function.
    """
    s = ""

    for i in range(len(options)):
        s += "({}) {}\n".format(i + 1, options[i])
    print(s)


if __name__ == "__main__":
    import pickle

    my_dictionary = pickle.load(open("english_dictionary.p", "rb"))

    options = [
        "Look up word",
        "Show neighboring words",
        "Show number of dictionary entrie",
        "Exit",
    ]
    curr_option = None

    while curr_option != len(options):
        print("**Indicate the number of the option you would like to select**")
        show_options(options)

        curr_option = input(">>>>>>>>>>> ")

        if not curr_option.isnumeric():
            print("Invalid option")

            continue
        else:
            curr_option = int(curr_option)

        if curr_option == 1:
            print("Enter the word you would like to look up.")
            word = input(">>>>>>>>>>> ")
            i = my_dictionary.get_word_index(word)

            if i != -1:
                print('\nEntry "{}" found.\n'.format(word))
                print(my_dictionary.entries[word[0]][i])
            else:
                print('\nEntry "{}" not found.'.format(word))
                # suggest closest word

                if SUGGEST_WORD:
                    closest_word = my_dictionary.suggest_closest_word(word)
                    print('Did you mean "{}"?\n\n'.format(closest_word))

        elif curr_option == 2:
            print("Enter the word you would like to show neighboring words for.")
            word = input(">>>>>>>>>>> ")
            print("Enter the number of words you would like to display.")
            number = int(input(">>>>>>>>>>> "))
            entries = my_dictionary.get_entries_by_window(word, number)
            s = "\n"

            for entry in entries:
                if entry.word == word:
                    s += "------------------------\n"
                s += str(entry)
                s += "\n"

                if entry.word == word:
                    s += "------------------------\n"

            print(s)
            print("------{} entries found------\n".format(len(entries)))

            if len(entries) == 0 and SUGGEST_WORD:
                closest_word = my_dictionary.suggest_closest_word(word)
                print('Did you mean "{}"?\n\n'.format(closest_word))

        elif curr_option == 3:
            print(
                "\n\nThe dictionary has {} entries.\n\n".format(
                    my_dictionary.get_num_entries()
                )
            )

        elif curr_option == 4:
            print("Exiting dictionary. Goodbye.")
        else:
            print("Invalid option")
