"""Amazing Banking Corporation functions"""
from typing import List, Tuple, Dict, TextIO

# Constants
# client_to_accounts value indexing
BALANCES = 0
INTEREST_RATES = 1

# transaction codes
WITHDRAW_CODE = -1
DEPOSIT_CODE = 1

LOAN_INTEREST_RATE = 2.2  # percent


## ------------ HELPER FUNCTIONS GIVEN BELOW -----------
# do not modify

def display_client_accounts(client_to_accounts: Dict, client: Tuple) -> None:
    """ Display the indicated client's account balances in a human-friendly
    format, using the client_to_account dictionary.

    The first account is a chequing account, followed by subsequent savings
    account(s). A loan account, if present, is signified as the last account
    if it has a negative balance.
    """

    i = 0
    for account in client_to_accounts[client][BALANCES]:
        if i == 0:
            # the first account is always a chequing account
            print("Chequing Account")
        elif account > 0:
            print("Savings Account {}".format(i))
        else:
            print("Loan Account")
        print("$ {:.2f}".format(account))
        i += 1


def get_fv(present_value: float, r: float, n: int) -> float:
    """ Return the future value calculated using the given present value (pv)
    growing with rate of return (interest rate) r, compounded annually,
    for a total of n years.

    r is given as a percentage value.

    """
    return present_value * (1 + r / 100) ** n


def get_sd(x: List[float]) -> float:
    """
    Return the standard deviation of the values in the list x.
    """
    n = len(x)
    x_bar = (sum(x)) / n

    sd = 0
    for x_i in x:
        sd += (x_i - x_bar) ** 2

    return (sd / n) ** 0.5


### ----------- END OF PROVIDED HELPER FUNCTIONS --------------

def load_financial_data(file: TextIO) -> \
        Dict[Tuple[str, int], List[List[float]]]:
    """ Return a client to accounts dictionary buy input file.
    """

    diction, index = {}, 0
    acc = file.readlines()
    for line in acc:
        if acc[index].strip() == '' or index == 0:
            if acc[index].strip() == '':
                index += 1
            key_1 = line.strip()
            index += 1
            key_2 = int(float(acc[index].strip().replace(' ', '')))
            index += 1
            key, diction[key] = (key_1, key_2), [[], []]
        elif line.strip() == '' or line.strip()[0] == 'C' \
                or line.strip()[0] == 'S':
            index += 1
        elif line.strip()[0] == 'B':
            diction[key][0].append(float(acc[index].strip().split()[-1]))
            index += 1
        elif line.strip()[0] == 'I':
            diction[key][1].append(float(acc[index].strip().split()[-1]))
            index += 1
    return diction


def get_num_clients(client_to_accounts: Dict[Tuple[str, int],
                                             List[List[float]]]) -> int:
    """ Returns the number of clients present in the
    client_to_accounts dictionary.

    >>> get_num_clients({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0], [2.0]]})
    2

    >>> get_num_clients({('Bob', 555555555): [[100.0], [1.0]], \
                          ('Sally', 123123123): [[250.0], [2.0]],\
                          ('Niko', 555552555): [[110.0], [1.1]]})
    3
    """

    return len(client_to_accounts.keys())


def validate_identity(refer: Dict[Tuple[str, int],
                                  List[List[float]]],
                      name: str, sin: int) -> bool:
    """ Return True iff the name and sin are valid client in refer.

    >>> validate_identity({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0], [2.0]]}, 'Bob', 555555555)
    True
    >>> validate_identity({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0], [2.0]]}, 'Bob', 555234555)
    False
    """

    identity = (name, sin)
    return identity in refer.keys()


def get_num_accounts(refer: Dict[Tuple[str, int],
                                 List[List[float]]],
                     valid: Tuple[str, int]) -> int:
    """ Return number of the total number of accounts
    the valid has open without loan accounts.

    >>> get_num_accounts({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0], [2.0]]}, ('Bob', 555555555))
    1
    >>> get_num_accounts({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0, 223.0], [2.0, 1.5]]}, ('Sally', 123123123))
    2
    >>> get_num_accounts({('Bob', 555555555): [[100.-1], [1.0]], \
    ('Sally', 123123123): [[250.0], [2.0]]}, ('Bob', 555255555))
    0
    """

    acc = 0
    if valid in refer:
        for item in refer[valid][0]:
            if item >= 0:
                acc += 1
    return acc


def get_account_balance(refer: Dict[Tuple[str, int], List[List[float]]],
                        valid: Tuple[str, int], num: int) -> float:
    """ Return relative number of money in refer based
    on valid_client and its account number.

    >>> get_account_balance({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0, 223.0], [2.0, 1.5]]},\
    ('Sally', 123123123), 0)
    250.0
    >>> get_account_balance({('Bob', 555555555): [[100.0], [1.0]], \
    ('Sally', 123123123): [[250.0, 223.0], [2.0, 1.5]]},\
    ('Sally', 123123123), 1)
    223.0
    """

    if valid in refer:
        return refer[valid][0][num]
    else:
        return 0


def open_savings_account(refer: Dict[Tuple[str, int], List[List[float]]],
                         valid: Tuple[str, int],
                         balance: float, interest: float) -> None:
    """ Update the refer dictionary of the valid client by new
    balance and interest.

    >>> refer2 = {('Bob', 555555555): [[100.0], [1.0]]}
    >>> open_savings_account(refer2, ('Bob', 555555555), 23.0 , 1.7)
    >>> refer2
    {('Bob', 555555555): [[100.0, 23.0], [1.0, 1.7]]}
    >>> refer2 = {('Bob', 555555555): [[100.0], [1.0]]}
    >>> open_savings_account(refer2, ('Bob', 555555555), 33.0 , 4.7)
    >>> refer2
    {('Bob', 555555555): [[100.0, 33.0], [1.0, 4.7]]}
    """

    if valid in refer:
        if refer[valid][0][-1] < 0:
            refer[valid][0].insert(-1, balance)
            refer[valid][-1].insert(-1, interest)
        else:
            refer[valid][0].extend([balance])
            refer[valid][-1].extend([interest])


def get_client_to_total_balance(refer: Dict[Tuple[str, int],
                                            List[List[float]]]) \
        -> Dict[Tuple[str, int], float]:
    """ Return a new dictionary with the specific format.

    >>> get_client_to_total_balance({('Bob', 555555555): [[100.0], [1.0]],\
    ('Sally', 123123123): [[250.0, 223.0], [2.0, 1.5]]})
    {('Bob', 555555555): 100.0, ('Sally', 123123123): 473.0}
    >>> get_client_to_total_balance({('Bob', 555555555): [[102.0], [1.0]],\
    ('Sally', 123123123): [[252.0, 2113.0], [2.0, 1.5]]})
    {('Bob', 555555555): 102.0, ('Sally', 123123123): 2365.0}
    """

    acc = {}
    for key in refer:
        if key not in acc:
            acc[key] = sum(refer[key][0])
    return acc


def update_balance(refer: Dict[Tuple[str, int], List[List[float]]],
                   valid: Tuple[str, int],
                   num: int, change: float,
                   code: int) -> None:
    """ Update the refer dictionary of the valid client
    by a plenty of rules from account number, change balance amount
    and code.

    >>> refer = {('Karla Hurst', 770898021): \
    [[768.0, 4399.0, -2070.0], [0.92, 2.3, 1.5]]}
    >>> update_balance(refer, ('Karla Hurst', 770898021), 1, 1.0, 1)
    >>> refer
    {('Karla Hurst', 770898021): [[768.0, 4400.0, -2070.0], [0.92, 2.3, 1.5]]}
    >>> refer = {('Karla Hurst', 770898021): \
    [[768.0, 4399.0, -2070.0], [0.92, 2.3, 1.5]]}
    >>> update_balance(refer, ('Karla Hurst', 770898021), 1, 1.0, -1)
    >>> refer
    {('Karla Hurst', 770898021): [[768.0, 4398.0, -2070.0], [0.92, 2.3, 1.5]]}
    """

    if code == WITHDRAW_CODE:
        refer[valid][0][num] = refer[valid][0][num] - change
    else:
        refer[valid][0][num] = refer[valid][0][num] + change


def get_loan_status(client_to_account: Dict[Tuple[str, int], List[List[float]]],
                    client_bal: Dict[Tuple[str, int], float],
                    client: Tuple[str, int], loan: float) -> bool:
    """ Return True iff if the loan is approved, otherwise the function
    will return False.

    >>> client_to_account = {('Dog', 111): [[100.0, 200.0], [0.5, 1.2]],\
     ('Cat', 222): [[700.0], [7.6]], ('Elephant', 333):\
      [[200.0, 200.0, 200.0, 200.0, -500.0], [0.63, 0.05, 0.34, 0.92, 0.04]]}
    >>> cli_dic= {('Dog', 111): 300.0, ('Cat', 222):\
     700.0, ('Elephant', 333): 0.0}
    >>> get_loan_status(client_to_account, cli_dic, ('Elephant', 333), 300.0)
    False
    >>> get_loan_status(client_to_account, cli_dic, ('Cat', 222), 20.0)
    True
    """

    point, acr, mean, acc = 0, 0, 0, 0
    for num in client_bal:
        acr += client_bal[num]
    mean = acr / len(client_bal)
    empty_list = []
    for a in client_bal.values():
        empty_list.append(a)
    sigma = get_sd(empty_list)
    if client_bal[client] > mean:
        point += 1
    if client_bal[client] >= loan:
        point += 1
    for each_account in client_to_account[client][0]:
        if mean - sigma > each_account:
            point -= 2
        if mean + sigma < each_account:
            point += 2
            acc += 1
    if acc >= len(client_to_account[client][1]) // 2 + 1:
        point += 5
    if point >= 5:
        client_to_account[client][0].append(-loan)
        client_to_account[client][-1].append(LOAN_INTEREST_RATE)
        return True
    else:
        return False


def get_financial_range_to_clients(refer: Dict[Tuple[str, int], float],
                                   ranges: List[Tuple[float, float]]) -> \
        Dict[Tuple[float, float], List[Tuple[str, int]]]:
    """ Return a new dictionary with the a lot of asked format.

    >>> refer2 = {('Bob', 555555555): 20.0 ,('Sally', 123123123): 100.0}
    >>> ranges2 = [(0.0, 20.0), (30.0, 140.0)]
    >>> get_financial_range_to_clients(refer2, ranges2)
    {(0.0, 20.0): [('Bob', 555555555)], (30.0, 140.0): [('Sally', 123123123)]}
     >>> refer3 = {('Bob', 555555555): 17.0 ,('Sally', 123123123): 18.0}
    >>> ranges3 = [(0.0, 20.0), (30.0, 140.0)]
    >>> get_financial_range_to_clients(refer3, ranges3)
    {(0.0, 20.0): [('Bob', 555555555), ('Sally', 123123123)]}
    """

    acc = {}
    for item in ranges:
        if item not in acc:
            acc[item] = []
    for key in refer:
        for key2 in acc:
            if key2[0] <= refer[key] <= key2[-1]:
                acc[key2].append(key)
    invalid = []
    for key3 in acc:
        if not acc[key3]:
            invalid.append(key3)
    for key4 in invalid:
        acc.pop(key4)
    return acc


def get_fv_from_accounts(balances: List[float],
                         rates: List[float], time: int) -> float:
    """ Return the sum of the future amount of money
     of all the account balances
     given their respective interest rates.

    >>> balances = [768.0, 2070.0]
    >>> rates = [0.92, 1.5]
    >>> get_fv_from_accounts(balances, rates, 2)
    2914.761953519999
    >>> balances2 = [78.0, 20.0]
    >>> rates2 = [0.22, 1.2]
    >>> get_fv_from_accounts(balances2, rates2, 2)
    98.82645751999998
    """

    acc = 0
    for index in range(len(balances)):
        acc += get_fv(balances[index], rates[index], time)
    return acc


def time_to_client_goal(refer: Dict[Tuple[str, int],
                                    List[List[float]]],
                        valid: Tuple[str, int],
                        amount: float) -> int:
    """ Return the smallest number of years it would
    spend for the client's total balance in order to
     get or overcome the goal that set before.

    >>> refer = {('Karla Hurst', 770898021):\
     [[768.0, 2070.0], [0.92, 1.5]],\
      ('Pamela Dickson', 971875372):\
       [[36358866.0, 5395448.0, 23045442.0,\
        14316660.0, 45068981.0, 4438330.0,\
         16260321.0, 7491204.0, 23330669.0],\
          [2.3, 2.35, 2.25, 2.35, 2.05,\
           2.1, 2.45, 2.4, 2.0]],\
           ('Roland Lozano', 853887123):\
            [[1585.0, 1170.0, 1401.0,\
             3673.0], [0.63, 0.05,\
              0.34, 0.92]]}
    >>> valid = ('Karla Hurst', 770898021)
    >>> time_to_client_goal(refer, valid, 100000.0)
    255
    >>> refer2 = {('Karla Hurst', 770898021):\
     [[100.0, 100.0], [2.0, 2.0]],\
      ('Pamela Dickson', 971875372):\
       [[36358866.0, 5395448.0, 23045442.0,\
        14316660.0, 45068981.0, 4438330.0,\
         16260321.0, 7491204.0, 23330669.0],\
          [2.3, 2.35, 2.25, 2.35, 2.05,\
           2.1, 2.45, 2.4, 2.0]],\
           ('Roland Lozano', 853887123):\
            [[1585.0, 1170.0, 1401.0,\
             3673.0], [0.63, 0.05,\
              0.34, 0.92]]}
    >>> valid2 = ('Karla Hurst', 770898021)
    >>> time_to_client_goal(refer2, valid2, 500.0)
    47
    """

    if sum(refer[valid][0]) >= amount:
        return 0
    year = 1
    while True:
        money = 0
        for index in range(len(refer[valid][0])):
            money += get_fv(refer[valid][0][index],
                            refer[valid][-1][index], year)
        if money >= amount:
            return year
        else:
            year += 1
    return year


def average(refer: Dict[Tuple[str, int], float]) -> float:
    """ Return average value of refer.

    >>> {('Karla Hurst', 770898021): 2838.0}
    {('Karla Hurst', 770898021): 2838.0}
    """

    amount, count = 0, 0
    for key in refer:
        count += 1
        amount += refer[key]
    return amount / count


if __name__ == "__main__":
    import doctest
    import python_ta
    python_ta.check_all()

    # uncomment the following line to run your docstring examples
    doctest.testmod()
