package com.banktask.infrastructure.bank;

import com.banktask.domain.bank.BankProfile;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class BankDirectory {
    private final Map<String, BankProfile> banksById = new ConcurrentHashMap<>();

    public void register(BankProfile profile) {
        banksById.put(profile.getBankId(), profile);
    }

    public Optional<BankProfile> findById(String bankId) {
        return Optional.ofNullable(banksById.get(bankId));
    }

    public Optional<BankProfile> findByName(String bankName) {
        return banksById.values().stream()
                .filter(profile -> profile.getBankName().equalsIgnoreCase(bankName))
                .findFirst();
    }

    public boolean authenticateStaff(String username, String password) {
        return banksById.values().stream().anyMatch(bank -> bank.authenticate(username, password));
    }

    public Collection<BankProfile> snapshot() {
        return banksById.values();
    }
}
