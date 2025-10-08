package com.banktask.service.model;

import com.banktask.domain.account.TransactionRecord;

public record TransferReceipt(TransactionRecord sourceRecord, TransactionRecord targetRecord) {
}
