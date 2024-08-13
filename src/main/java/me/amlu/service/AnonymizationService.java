package me.amlu.service;

import me.amlu.model.User;

public interface AnonymizationService {
    boolean anonymizeUser(User user);
}