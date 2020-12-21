import sys
sys.path.insert(0, 'pyta')

import builtins
import banking_functions as abc
import io
import python_ta

def _mock_disallow(func_name: str):
    """Raise an Exception saying that use of function func_name is not
    allowed.

    """

    def mocker(*args):
        raise Exception(
            "The use of function {} is not allowed.".format(func_name))

    return mocker

test_module = sys.modules['banking_functions']
setattr(test_module, "input", _mock_disallow("input"))
setattr(test_module, "print", _mock_disallow("print"))

print('==================== Start: checking coding style ===================')

python_ta.check_all('banking_functions.py', config='pyta/a3_pyta.txt')

print('=================== End: checking coding style ===================\n')


# Get the initial value of the constants
constants_before = [abc.BALANCES, abc.INTEREST_RATES, \
                    abc.WITHDRAW_CODE, abc.DEPOSIT_CODE, abc.LOAN_INTEREST_RATE]

print('============ Start: checking parameter and return types ============')
# Type check abc.load_financial_data

game_file = open("client_data_1.txt", 'r')
result = abc.load_financial_data(game_file)
game_file.close()

print('Checking load_financial_data...')
assert isinstance(result, dict), \
       '''abc.load_financial_data should return a dict, but returned {0}
       .'''.format(type(result))

for key, value in result.items():
    assert isinstance(key, tuple), \
            '''abc.load_financial_data should return a dict where the keys are 
            tuples, but key {0} is of type {1}.
            '''.format(key, type(key))
    assert isinstance(key[0], str), \
           '''abc.load_financial_data should return a dict where its keys are tuples
            with type str at index 0, but {0} is of type {1}.
           '''.format(key[0], type(key[0]))
    assert isinstance(key[1], int), \
           '''bc.load_financial_data should return a dict where its keys are tuples
            with type int at index 1, but {0} is of type {1}.
           '''.format(key[1], type(key[1]))
    assert isinstance(value, list), \
           '''abc.load_financial_data should return a dict where its values are of
            of type list, but {0} is of type {1}.
           '''.format(value, type(value))           
    assert isinstance(value[0], list), \
           '''abc.load_financial_data should return a dict where its values are lists
            with type list at index 0
            of type list, but {0} is of type {1}.
           '''.format(value, type(value[0]))
    assert isinstance(value[1], list), \
           '''abc.load_financial_data should return a dict where its values are lists
            with type list at index 1
            of type list, but {0} is of type {1}.
           '''.format(value, type(value[1]))
    assert isinstance(value[0][0], float), \
           '''abc.load_financial_data should return a dict where its values are list
            of list of floats, but {0} is of type {1}.
           '''.format(value, type(value[0][0]))
    assert isinstance(value[1][0], float), \
           '''abc.load_financial_data should return a dict where its values are list
            of list of floats, but {0} is of type {1}.
           '''.format(value, type(value[1][0]))

print('  check complete')

print('Checking get_num_clients...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.get_num_clients(client_to_accounts)
assert isinstance(result, int), \
       '''abc.get_num_clients should return an int, but returned {0}
       .'''.format(type(result))
print('  check complete')


print('Checking validate_identity...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.validate_identity(client_to_accounts, 'Bob Bob', 543210999)
assert isinstance(result, bool), \
       '''abc.validate_identity should return a bool, but returned {0}
       .'''.format(type(result))
print('  check complete')

print('Checking get_num_accounts...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.get_num_accounts(client_to_accounts, ('Bob Bob', 777777777))
assert isinstance(result, int), \
       '''abc.get_num_accounts should return an int, but returned {0}
       .'''.format(type(result))
print('  check complete')

print('Checking get_account_balance...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.get_account_balance(client_to_accounts, ('Bob Bob', 777777777), 0)
assert isinstance(result, float), \
       '''abc.get_num_accounts should return a float, but returned {0}
       .'''.format(type(result))
print('  check complete')


print('Checking open_savings_account...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.open_savings_account(client_to_accounts, ('Bob Bob', 777777777), 1.0, 1.0)
assert result  == None, \
       '''abc.get_num_accounts should return None, but returned {0}
       .'''.format(type(result))
print('  check complete')

## --------- get client to total balance
print('Checking get_client_to_total_balance...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.get_client_to_total_balance(client_to_accounts)
assert isinstance(result, dict), \
       '''abc.get_num_accounts should return dict, but returned {0}
       .'''.format(type(result))

# check the keys and values
for key, value in result.items():
    assert isinstance(key, tuple), \
            '''abc.load_financial_data should return a dict where the keys are 
            tuples, but key {0} is of type {1}.
            '''.format(key, type(key))
    assert isinstance(key[0], str), \
           '''abc.load_financial_data should return a dict where its keys are tuples
            with type str at index 0, but {0} is of type {1}.
           '''.format(key[0], type(key[0]))
    assert isinstance(key[1], int), \
           '''bc.load_financial_data should return a dict where its keys are tuples
            with type int at index 1, but {0} is of type {1}.
           '''.format(key[1], type(key[1]))
    assert isinstance(value, float), \
           '''abc.load_financial_data should return a dict where its values are of
            of type float, but {0} is of type {1}.
           '''.format(value, type(value))
    
print('  check complete')
## --------- end check get client to total balance

print('Checking update_balance...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.update_balance(client_to_accounts, ('Bob Bob', 777777777), 0, 1.0, abc.DEPOSIT_CODE)

assert result == None, \
       '''abc.update_balance should return None, but returned {0}
       .'''.format(type(result))
print('  check complete')

print('Checking get_loan_status...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
client_to_total_balance = {('Bob Bob', 777777777): 1.0, ('Carly Dafford', 555555555): 2.0}
result = abc.get_loan_status(client_to_accounts, client_to_total_balance, ('Bob Bob', 777777777), 10.0)

assert isinstance(result, bool), \
       '''abc.get_loan_status should return a bool, but returned {0}
       .'''.format(type(result))
print('  check complete')

# --- get_financial_range_to_clients
print('Checking get_financial_range_to_clients...')
client_to_total_balance = {('Bob Bob', 777777777):1.0, ('Carly Dafford', 555555555):1.0}
financial_ranges = [(0.0, 1.0), (1.5, 2.0)]
result = abc.get_financial_range_to_clients(client_to_total_balance, financial_ranges)

# check the keys and values
for key, value in result.items():
    assert isinstance(key, tuple), \
            '''abc.load_financial_data should return a dict where the keys are 
            tuples, but key {0} is of type {1}.
            '''.format(key, type(key))
    assert isinstance(key[0], float), \
           '''abc.load_financial_data should return a dict where its keys are tuples
            with type float at index 0, but {0} is of type {1}.
           '''.format(key[0], type(key[0]))
    assert isinstance(key[1], float), \
           '''abc.load_financial_data should return a dict where its keys are tuples
            with type float at index 1, but {0} is of type {1}.
           '''.format(key[1], type(key[1]))
    assert isinstance(value, list), \
           '''abc.load_financial_data should return a dict where its values are of
            of type list, but {0} is of type {1}.
           '''.format(value, type(value))
    assert isinstance(value[0], tuple), \
           '''abc.load_financial_data should return a dict where its values are lists
            of tuples, but {0} is of type {1}.
           '''.format(value[0], type(value[0]))
    assert isinstance(value[0][0], str), \
           '''abc.load_financial_data should return a dict where its values are lists
            of tuples with type str at index 0, but {0} is of type {1}.
           '''.format(value[0][0], type(value[0][0]))
    assert isinstance(value[0][1], int), \
           '''abc.load_financial_data should return a dict where its values are lists
            of tuples with type int at index 1, but {0} is of type {1}.
           '''.format(value[0][1], type(value[0][1]))

print('  check complete')
# --- end check get_financial_range_to_clients

print('Checking get_fv_from_accounts...')
result = abc.get_fv_from_accounts([1.0, 1.0], [1.0, 1.0], 1)
assert isinstance(result, float), \
       '''abc.get_fv_from_accounts should return float, but returned {0}
       .'''.format(type(result))
print('  check complete')

print('Checking time_to_client_goal...')
client_to_accounts = {('Bob Bob', 777777777):[[1.0], [1.0]], ('Carly Dafford', 555555555):[[2.0], [1.5]]}
result = abc.time_to_client_goal(client_to_accounts, ('Bob Bob', 777777777), 10)
assert isinstance(result, int), \
       '''abc.time_to_client_goal should return int, but returned {0}
       .'''.format(type(result))
print('  check complete')

print('============= End: checking parameter and return types =============\n')

print('========== Start: checking whether constants are unchanged ==========')

# Get the final values of the constants
constants_after = [abc.BALANCES, abc.INTEREST_RATES, \
                    abc.WITHDRAW_CODE, abc.DEPOSIT_CODE, abc.LOAN_INTEREST_RATE]

print('Checking constants...')
# Check whether the constants are unchanged.
assert constants_before == constants_after, \
       '''Your function(s) modified the value of one or more constants.
       Edit your code so that the value of the constants are not 
       changed by your functions.'''
      
print('  check complete')

print('=========== End: checking whether constants are unchanged ===========')


