import unittest
import wordlock_functions

class TestWordLockFunctions(unittest.TestCase):

    def test_get_section_start_0(self):
        actual = wordlock_functions.get_section_start(1)
        expected = 0
        self.assertEqual(actual, expected, "get_section_start for section 1 is wrong!")
    
    def test_get_section_start_1(self):
        wordlock_functions.SECTION_LENGTH = 4
        actual = wordlock_functions.get_section_start(10)
        expected = 36
        self.assertEqual(actual, expected, "get_section_start does not work for general SECTION_LENGTH!")
        
    def test_is_valid_move_0(self):
        actual = wordlock_functions.is_valid_move("S")
        self.assertTrue(actual, "S should be valid move!")
        
    def test_is_valid_move_1(self):
        actual = wordlock_functions.is_valid_move("A")
        self.assertFalse(actual, "A should not be valid move!")
        
    def test_is_valid_move_2(self):
        NEW_CHECK = "CCC"
        wordlock_functions.CHECK = NEW_CHECK
        actual = wordlock_functions.is_valid_move(NEW_CHECK)
        self.assertTrue(actual, "is_valid_move does not work for general SECTION_LENGTH!")
    
    def test_is_valid_section_0(self):
        wordlock_functions.ANSWER = "wordlockgamewordlockgame"
        wordlock_functions.SECTION_LENGTH = 4
        actual = wordlock_functions.is_valid_section(6)
        self.assertTrue(actual, "is_valid_section does not work for general ANSWER and SECTION_LENGTH!")
    
    def test_is_valid_section_1(self):
        section_number = 0
        actual = wordlock_functions.is_valid_section(section_number)
        self.assertFalse(actual, "0 should not be a valid section number!")
        
    def test_check_section_0(self):
        actual = wordlock_functions.check_section("CATDOGFOXEMU", 1)
        self.assertTrue(actual, "extract wrong section 1!")

    def test_check_section_1(self):
        wordlock_functions.ANSWER = "wordlockgame"
        wordlock_functions.SECTION_LENGTH = 4
        actual = wordlock_functions.check_section("wodrlockgmae", 2)
        self.assertTrue(actual, "check_section wrong for general answer and section length!")
        
    def test_change_state_0(self):
        wordlock_functions.SECTION_LENGTH = 4
        actual = wordlock_functions.change_state("wordlockgame", 1, "S")
        expected = "dorwlockgame"
        self.assertEqual(actual, expected, "change_state swap not work for general case!")
        
    def test_change_state_1(self):
        wordlock_functions.SECTION_LENGTH = 4
        actual = wordlock_functions.change_state("wordlockgame", 2, "R")
        expected = "wordklocgame"
        self.assertEqual(actual, expected, "change_state rotate not work for general case!")  
        
    def test_get_move_hint_0(self):
        wordlock_functions.SECTION_LENGTH = 3
        wordlock_functions.ANSWER = "DOGFOX"
        actual = wordlock_functions.get_move_hint("DGOFXO", 2)
        expected = "R"
        self.assertEqual(actual, expected, "get_move_hint 0 does not work!")
        
    def test_get_move_hint_1(self):
        wordlock_functions.SECTION_LENGTH = 3
        wordlock_functions.ANSWER = "DOGFOX"
        actual = wordlock_functions.get_move_hint("GODFXO", 1)
        expected = "S"
        self.assertEqual(actual, expected, "get_move_hint 1 does not work!")
        
    def test_get_move_hint_2(self):
        wordlock_functions.SECTION_LENGTH = 3
        wordlock_functions.ANSWER = 'CATDOGFOXEMU'
        actual = wordlock_functions.get_move_hint("CATDOGFOXMUE", 4)
        expected = "R"
        self.assertEqual(actual, expected, "get_move_hint 2 does not work!")     
        
        
        
    
    

if __name__ == '__main__':
    unittest.main()