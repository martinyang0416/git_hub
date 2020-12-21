import sys

# student files
sys.path.insert(0, "pyta")
import python_ta
from word import Entry, Definition
from dictionary_class import MyDictionary


print("==================== Start: checking coding style ===================")

python_ta.check_all("word.py", config="pyta/a4_pyta.txt")
python_ta.check_all("dictionary_class.py", config="pyta/a4_pyta.txt")

print("=================== End: checking coding style ===================\n")

print("============ Start: checking class attributes ============")

print("")
word = "random"
definition = "made, done, happening, or chosen without method or conscious decision."
part_of_speech = "adjective"
# create definition instance
defn_instance = Definition(definition, part_of_speech)
# create an entry instance holding a set of definitions
definitions = [defn_instance]
entry_instance = Entry(word, definitions)
# dictionary instance
dict_instance = MyDictionary()
dict_instance.add_entries([entry_instance])

print("Checking class Definition in word.py ... ", end="")
assert hasattr(
    defn_instance, "definition"
), "Attribute 'definition' missing in class 'Definition'"
assert hasattr(
    defn_instance, "part_of_speech"
), "Attribute 'part_of_speech' missing in class 'Definition'"
print("All good!")


print("Checking class Entry in word.py ... ", end="")
assert hasattr(entry_instance, "word"), "Attribute 'word' missing in class 'Entry'"
assert hasattr(
    entry_instance, "definitions"
), "Attribute 'definitions' missing in class 'Entry'"
print("All good!")


print("Checking class MyDictionary in dictionary_class.py ... ", end="")
assert hasattr(
    dict_instance, "entries"
), "Attribute 'entries' missing in class 'MyDictionary'"
assert isinstance(
    getattr(dict_instance, "entries"), dict
), "Attribute 'entries' of class 'MyDictionary' must be of type dict"
assert hasattr(
    dict_instance, "__num_entries__"
), "Attribute '__num_entries__' missing in class 'MyDictionary'"
assert isinstance(
    getattr(dict_instance, "__num_entries__"), int
), "Attribute '__num_entries__' of class 'MyDictionary' must be of type int"

assert hasattr(dict_instance, "get_num_entries") and callable(
    getattr(dict_instance, "get_num_entries")
), "class 'MyDictionary' does not have method 'get_num_entries'"
num_entries = dict_instance.get_num_entries()
assert isinstance(
    num_entries, int
), f"'get_num_entries' method of class 'MyDictionary' must have return type int. \
        Instead, your code returns type {type(num_entries)}"
assert hasattr(dict_instance, "suggest_closest_word") and callable(
    getattr(dict_instance, "suggest_closest_word")
), "class 'MyDictionary' does not have method 'suggest_closest_word'"
closest_word = dict_instance.suggest_closest_word("apple")
assert isinstance(
    closest_word, str
), f"'suggest_closest_word' of class 'MyDictionary' must have return type str. \
        Instead your code returns type {type(closest_word)}"
print("All good!")

print("")

print("============= End: checking parameter and return types =============\n")
