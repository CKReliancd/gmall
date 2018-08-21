package com.atguigu.gmall.manage.util;

import java.util.Random;

public class Test01 {

    public static void main(String[] args) {
        Random row = new Random();
        int rowi = row.nextInt(9);
        System.out.println("行号"+(rowi+1));

        Random column = new Random();
        int columni = column.nextInt(9);
        System.out.println("列号"+(columni+1));
    }
}
