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


def open_savings_account(data: Dict[Tuple[str, int], List[List[float]]],
                         name: Tuple[str, int],
                         money: float, rate: float) -> None:

    if name not in data:
        return None
    else:
        last = data[name][0][-1]
        if last >= 0:
            data[name][0].append(money)
            data[name][-1].append(rate)
        else:
            data[name][0].insert(len(data[name][0]) - 1, money)
            data[name][-1].insert(len(data[name][0]) - 1, rate)


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


# def get_loan_status(refer: Dict[Tuple[str, int],
#                                 List[List[float]]],
#                     balance: Dict[Tuple[str, int], float],
#                     valid: Tuple[str, int], loan: float) -> bool:
#     """ Return True iff if the loan is approved, otherwise the function
#     will return False.
#
#     >>> >>> refer = {('Karla Hurst', 770898021): [[768.0, 2070.0], [0.92, 1.5]], \
#     ('Pamela Dickson', 971875372): [[333.0, 222.0, 111.0, 100000.0], \
#     [2.1, 2.45, 2.4, 2.0]], ('Roland Lozano', 853887123): \
#     [[1500.0, 1100.0, 1400.0, 3500.0, -500.0], [0.63, 0.05, 0.34, 0.92, 0.04]]}
#     >>> balance = {('Karla Hurst', 770898021): 2838.0, \
#     ('Pamela Dickson', 971875372): 100666.0, \
#     ('Roland Lozano', 853887123): 7000.0}
#     >>> get_loan_status(refer, balance, ('Roland Lozano', 853887123), 800.0)
#     False
#     >>> get_loan_status(refer, balance, ('Pamela Dickson', 971875372), 500.0)
#     True
#     """
#
#     avg, acc, point, half, count = \
#         average(balance), [], 0, round(len(refer[valid][0]) / 2), 0
#     for key in balance:
#         acc.append(balance[key])
#     sigma = get_sd(acc)
#     if sum(refer[valid][0]) <= 0 or refer[valid][0][-1] < 0:
#         return False
#     if sum(refer[valid][0]) > avg:
#         point += 1
#     if refer[valid][0][-1] < 0:
#         if sum(refer[valid][0]) > refer[valid][0][-1]:
#             point += 1
#     if balance[valid] < avg - sigma:
#         point -= 2
#     if balance[valid] > avg + sigma:
#         point += 2
#     if not refer[valid][0][-1] < 0:
#         point += 5
#     for item in refer[valid][0]:
#         if item > avg + sigma:
#             count += 1
#     if count > half:
#         point += 5
#     return point >= 5


def get_financial_range_to_clients(data: Dict[Tuple[str, int], float],
                                   chosen: List[Tuple[float, float]]) -> \
        Dict[Tuple[float, float], List[Tuple[str, int]]]:

    acc = {}
    for key_1 in chosen:
        acc[key_1] = []
    for key_2 in data:
        for key2 in acc:
            lower = key2[0]
            upper = key2[-1]
            if lower <= data[key_2] <= upper:
                acc[key2].append(key_2)
    acc2 = {}
    for key8 in acc:
        if not acc[key8] == []:
            acc2[key8] = acc[key8]
    return acc2



def get_fv_from_accounts(list1: List[float],
                         list2: List[float], year: int) -> float:
    amount = 0
    for index in range(len(list1)):
        value = get_fv(list1[index], list2[index], year)
        amount += value
    return amount


def time_to_client_goal(data: Dict[Tuple[str, int],
                                   List[List[float]]],
                        name: Tuple[str, int],
                        standard: float) -> int:
    amount = 0
    year = 1
    flag = True
    for item in data[name][0]:
        amount += item
    if not amount >= standard:
        while flag:
            for position in range(len(data[name][0])):
                num1 = data[name][0][position]
                num2 = data[name][-1][position]
                amount += get_fv(num1, num2, year)
            if not amount >= standard:
                year += 1
                amount = 0
            else:
                return year
        return year
    else:
        return 0


if __name__ == "__main__":
    import doctest

    # uncomment the following line to run your docstring examples
    doctest.testmod()
