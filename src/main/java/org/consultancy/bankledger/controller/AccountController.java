package org.consultancy.bankledger.controller;

import org.consultancy.bankledger.dto.TransferRequest;
import org.consultancy.bankledger.model.Account;
import org.consultancy.bankledger.repository.AccountRepository;
import org.consultancy.bankledger.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferService transferService;

    @GetMapping
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account){
        return accountRepository.save(account);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account Not Found."));
    }

    @PostMapping("/transfer")
    public String  transferAmount(@RequestBody TransferRequest request){
        transferService.transfer(request.getFromAccountId(), request.getToAccountId(),request.getAmount());
        return "Transfer Successful.";
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id){
        accountRepository.deleteById(id);
        return "Account Deleted Successfully";
    }
}