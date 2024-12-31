# Copyright Ilie Vlad Alexandru 325CAb 2024 - 2025

# Project Assignment POO  - J; POO Morgan - Phase One

## Structure

Two packages, one for executing commands, one for bank data;

- **bankData**
  - `Account` 
    -class for keeping data about an account;
  - `Card` 
    -class for keeping data about a card and for generating a new one time card;
  - `Exchange` 
    -class for all the things related to payments or transfers;
    -has methods for calculating exchange rates, keeping logs on transactions;
  - `User` 
    -class for keeping data about an user;
    -has methods for adding and getting aliases for accounts;
- **commands**
  - `CommandFactory`
    -standard design pattern used to generate all the commands necesary;
  - `AddAccount`, `AddFunds`, `CreateCard`, `CreateOneTimeCard`
    -commands for populating the program with data based on input;
    -similar functionality;
    -are logged into the user's transaction history;
  - `PrintUsers`, `PrintTransactions`
    -commands to print logs up untill a point in the program;
  - `DeleteAccount`, `DeleteCard`
    -similar to the creation commands, but for deletion;
    -are logged into the user's transaction history;
  - `PayOnline`, `SendMoney`, `SplitPayment`
    -commands for simulating money flow;
    -use Exchange's class methods to check, calculate and procces transactions;

## Overview

After initializing the users, the program parses the commands, generates the corresponding class for each one then procceses them in order. The program keeps logs of most of the actions to facilitate debugging and recognize invalid or unauthorized transactions.