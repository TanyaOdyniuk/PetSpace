package com.netcracker.service.util;

import org.springframework.stereotype.Service;

@Service
public class PageCounterService {
    public int getPageCount(int pageCapacity, int countDbRows) {
        int result = 0;
        if(pageCapacity != 0){
            result = countDbRows / pageCapacity;
            int remainder = countDbRows % pageCapacity;
            if(remainder != 0){
                result += 1;
            }
        }
        return result;
    }
}
