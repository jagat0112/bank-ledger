package org.consultancy.bankledger.repository;

import org.consultancy.bankledger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}