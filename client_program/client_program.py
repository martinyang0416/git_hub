import banking_functions as abc   
if __name__ == "__main__":
    data_fname = input("Enter the name of the client file to use: ")

    valid_name = 0
    while not valid_name:
        try:
            clients_file = open(data_fname)
            valid_name = 1
        except:
            print("ERROR! Invalid filename.")
            data_fname = input("Enter the name of the client file to use: ")
            valid_name = 0
    
    
    client_to_accounts = abc.load_financial_data(clients_file)
    
    while True:

        #Validate user
        print("------ Welcome to ABC's automated banking service. ------")
        client_name = input("Please enter your name as <Firstname> <Lastname>: ")
        client_sin = input('''For secondary authentication,
                                please enter your SIN as ### ### ###: ''')
        client_sin = client_sin.strip()
        client_sin = int(''.join(client_sin.split()));

        is_valid_identity = abc.validate_identity(client_to_accounts,
                                                  client_name, client_sin)
    
        if not is_valid_identity:
            print("Your credentials do not match any profiles on record. Goodbye.")
        
        else:
            print('''Your credentials have been succesfully validated.
                    Please choose from the following banking options:''')
            options = ['Make a transaction', 'Check total balance',
                       'Check account balances', 'Check savings goal',
                       'Sign out']
        
            client_option = None

            while client_option != 5:
                print("\n\n")
                client = (client_name, client_sin)

                for i in range(len(options)):
                    print("({}) ".format(i+1) + options[i])

                print('''**Indicate the number of the
                        option you would like to select**''')
        
                client_option = int(input(">>>>>>>>>>> "))

                if client_option == 1:
                    print("Indicate an account to perform a transaction:")
                    abc.display_client_accounts(client_to_accounts, client)
                    account_number = int(input('''**Enter 0 for Chequing,
                                                    or the Savings Account number**
                                                    \n>>>>>>>>>>> '''))
                    
                    if account_number >= abc.get_num_accounts(client_to_accounts, client):
                            print("Invalid account number. Transaction cancelled.")
                    else:
                            account_balance = abc.get_account_balance(client_to_accounts,
                                                                      client, account_number)
                            account_type = ['chequing', 'savings'][account_number % 2]
                            print('''Your selected {} account has an
                                    available balance of {}'''.format(account_type,
                                                                      account_balance))
                            
                            transaction_code = int(input('''**Enter 1 to deposit,
                                                            -1 to withdraw** \n>>>>>>>>>>> '''))
                            
                            if not (transaction_code == 1 or transaction_code == -1):
                                print("Invalid transaction code. Transaction cancelled.")

                            else:
                                transaction_amount = float(input('''**Enter the amount you would
                                                                        like to {}**\n>>>>>>>>>>>
                                                                        '''.format(['',
                                                                                    'deposit',
                                                                                    'withdraw'][transaction_code])))
 
                                if transaction_code == abc.WITHDRAW_CODE and transaction_amount > account_balance:
                                    print("Insufficient funds. Transaction cancelled.")
                                else:
                                    abc.update_balance(client_to_accounts, client, account_number, transaction_amount, transaction_code)
                                    new_account_balance = abc.get_account_balance(client_to_accounts, client, account_number)
                                    print("Your {} account now has a balance of {}.".format(account_type, new_account_balance))
                   
                elif client_option == 2:
                    client_to_total_balance = abc.get_client_to_total_balance(client_to_accounts)
                    print("Your total balance across all accounts is {}".format(client_to_total_balance[client]))
       
                elif client_option == 3:
                    print("Your account balances are:")
                    abc.display_client_accounts(client_to_accounts, client)
                elif client_option == 4:
                    savings_goal = float(input("**Enter a desired savings amount**\n>>>>>>>>>>> "))
                    savings_period = abc.time_to_client_goal(client_to_accounts, client, savings_goal)
                    client_fv = abc.get_fv_from_accounts(client_to_accounts[client][abc.BALANCES], \
                                                         client_to_accounts[client][abc.INTEREST_RATES], savings_period)
                    print("You will reach your savings goal in {} year(s), with an amount of {:.2f}".format(savings_period, client_fv))
                elif client_option == 5:
                    print("Thank you for choosing ABC. Goodbye.")
                else:
                    print("Invalid option")



