package com.dhh.ragBasic.service;

import reactor.core.publisher.Mono;

public interface ChatService {


    String call(String question, String context);
}
