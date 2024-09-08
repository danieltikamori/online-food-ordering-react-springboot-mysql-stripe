package me.amlu.service;

import me.amlu.model.User;

public interface DataTransferService {

    void exportData(User user);
    void transferData(User user) throws Exception;
}