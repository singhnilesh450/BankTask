package com.banktask.infrastructure.account;

import com.banktask.domain.account.AccountProfile;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class AccountDirectory {
    private final Map<String, AccountProfile> accountsById = new ConcurrentHashMap<>();
    private final Map<String, String> accountIdByUsername = new ConcurrentHashMap<>();

    public Optional<AccountProfile> findByUsername(String username) {
        String accountId = accountIdByUsername.get(username);
        if (accountId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(accountsById.get(accountId));
    }

    public Optional<AccountProfile> findById(String accountId) {
        return Optional.ofNullable(accountsById.get(accountId));
    }

    public void register(AccountProfile profile) {
        accountsById.put(profile.getAccountId(), profile);
        accountIdByUsername.put(profile.getUserName(), profile.getAccountId());
    }

    public void delete(String accountId) {
        AccountProfile removed = accountsById.remove(accountId);
        if (removed != null) {
            accountIdByUsername.remove(removed.getUserName());
        }
    }

    public boolean existsByUsername(String username) {
        return accountIdByUsername.containsKey(username);
    }

    public Collection<AccountProfile> snapshot() {
        return accountsById.values();
    }
}
