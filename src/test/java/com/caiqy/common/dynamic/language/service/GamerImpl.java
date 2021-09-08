package com.caiqy.common.dynamic.language.service;

import org.springframework.stereotype.Service;

/**
 * Created on 2021/6/25.
 *
 * @author caiqy
 */
@Service
public class GamerImpl implements Gamer{
    @Override
    public void play() {
        System.out.println("i'm playing");
    }
}
